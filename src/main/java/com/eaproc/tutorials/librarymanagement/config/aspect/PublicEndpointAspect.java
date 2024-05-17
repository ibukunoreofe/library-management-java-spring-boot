package com.eaproc.tutorials.librarymanagement.config.aspect;

import com.eaproc.tutorials.librarymanagement.config.annotation.PublicEndpoint;
import com.eaproc.tutorials.librarymanagement.config.registry.PublicEndpointRegistry;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Aspect
@Component
public class PublicEndpointAspect {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final PublicEndpointRegistry publicEndpointRegistry;

    public PublicEndpointAspect(RequestMappingHandlerMapping requestMappingHandlerMapping, PublicEndpointRegistry publicEndpointRegistry) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.publicEndpointRegistry = publicEndpointRegistry;
    }

    @PostConstruct
    public void init() {
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
            if (value.hasMethodAnnotation(PublicEndpoint.class)) {
                key.getPatternValues().forEach(publicEndpointRegistry::addPublicEndpoint);
            }
        });
    }
}
