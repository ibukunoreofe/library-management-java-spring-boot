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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomNotFoundFilter extends OncePerRequestFilter {

    private final Set<String> endpoints;

    private final Set<Pattern> endpointPatterns;

    @Autowired
    public CustomNotFoundFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.endpoints = new HashSet<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) ->
                endpoints.addAll(key.getPatternValues()));

        // Convert endpoint patterns to regex patterns
        this.endpointPatterns = requestMappingHandlerMapping.getHandlerMethods().keySet().stream()
                .flatMap(key -> key.getPatternValues().stream())
                .map(this::convertToRegex)
                .collect(Collectors.toSet());

        // Add health endpoints
        endpointPatterns.add(Pattern.compile("/actuator/health"));
        endpointPatterns.add(Pattern.compile("/actuator/info"));

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
        if (!isExistingEndpointWithPattern(path)) {
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

    private boolean isExistingEndpointWithPattern(String path) {
        return endpointPatterns.stream().anyMatch(pattern -> pattern.matcher(path).matches());
    }

    private Pattern convertToRegex(String pattern) {
        // Convert path variables to regex (e.g., "/api/books/{id}" to "/api/books/[^/]+")
        String regex = pattern.replaceAll("\\{[^/]+\\}", "[^/]+");
        return Pattern.compile(regex);
    }
}
