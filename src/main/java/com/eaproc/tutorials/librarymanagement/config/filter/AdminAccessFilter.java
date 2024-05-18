package com.eaproc.tutorials.librarymanagement.config.filter;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomAuthenticationProvider;
import com.eaproc.tutorials.librarymanagement.domain.model.RoleConstants;
import com.eaproc.tutorials.librarymanagement.config.registry.AdminEndpointRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.eaproc.tutorials.librarymanagement.config.providers.CustomAuthenticationProvider.authenticated;

@Component
public class AdminAccessFilter extends OncePerRequestFilter {

    private final AdminEndpointRegistry adminEndpointRegistry;

    @Autowired
    public AdminAccessFilter(AdminEndpointRegistry adminEndpointRegistry) {
        this.adminEndpointRegistry = adminEndpointRegistry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if ( authenticated() && adminEndpointRegistry.isAdminEndpoint(path)) {
            UserDetails userDetails = CustomAuthenticationProvider.auth();
            // "ROLE_"  is automatically added to role names as authority
            if (userDetails != null && userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals( "ROLE_" + RoleConstants.ADMIN_ROLE_NAME))) {

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                ObjectMapper objectMapper = new ObjectMapper();

                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("error", "Forbidden");
                errorDetails.put("message", "You do not have the necessary permission in your role to access this resource!");
                errorDetails.put("resource", request.getRequestURI());

                response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
