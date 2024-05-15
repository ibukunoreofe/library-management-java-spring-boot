package com.eaproc.tutorials.librarymanagement.aspect;

import com.eaproc.tutorials.librarymanagement.annotation.PublicEndpoint;
import com.eaproc.tutorials.librarymanagement.registry.PublicEndpointRegistry;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
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
