package com.eaproc.tutorials.librarymanagement.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomNotFoundFilter extends OncePerRequestFilter {

    private final Set<String> endpoints;

    @Autowired
    public CustomNotFoundFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.endpoints = new HashSet<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) ->
                endpoints.addAll(key.getPatternValues()));

        // health endpoints
        endpoints.add( "/actuator/health" );
        endpoints.add( "/actuator/info" );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Check if the request matches any existing endpoint
        String path = request.getRequestURI();

        // If the request path is not an existing endpoint, return a 404 Not Found
        if (!isExistingEndpoint(path)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":404,\"error\":\"Not Found\",\"message\":\"Resource not found\",\"path\":\"" + path + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExistingEndpoint(String path) {
        return endpoints.contains(path);
    }
}
