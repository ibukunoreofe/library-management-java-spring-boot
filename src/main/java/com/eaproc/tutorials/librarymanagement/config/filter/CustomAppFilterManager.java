package com.eaproc.tutorials.librarymanagement.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAppFilterManager extends OncePerRequestFilter {

    private final List<OncePerRequestFilter> filters;

    @Autowired
    public CustomAppFilterManager(
                    CustomNotFoundFilter customNotFoundFilter,
                    JwtAuthenticationFilter jwtAuthenticationFilter,
                    AdminAccessFilter adminAccessFilter
    ) {
        this.filters = List.of(customNotFoundFilter, jwtAuthenticationFilter, adminAccessFilter);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        FilterChain customFilterChain = new CustomFilterChain(filterChain, filters);
        customFilterChain.doFilter(request, response);
    }

    private static class CustomFilterChain implements FilterChain {

        private final FilterChain originalChain;
        private final List<OncePerRequestFilter> filters;
        private int currentPosition = 0;

        public CustomFilterChain(FilterChain originalChain, List<OncePerRequestFilter> filters) {
            this.originalChain = originalChain;
            this.filters = filters;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (currentPosition < filters.size()) {
                OncePerRequestFilter currentFilter = filters.get(currentPosition++);
                currentFilter.doFilter(request, response, this);
            } else {
                originalChain.doFilter(request, response);
            }
        }
    }
}
