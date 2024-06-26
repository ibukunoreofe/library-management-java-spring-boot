package com.eaproc.tutorials.librarymanagement.config.aspect;

import com.eaproc.tutorials.librarymanagement.config.annotation.AdminOnlyEndpoint;
import com.eaproc.tutorials.librarymanagement.config.registry.AdminEndpointRegistry;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Aspect
@Component
public class AdminEndpointAspect {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final AdminEndpointRegistry adminEndpointRegistry;

    public AdminEndpointAspect(RequestMappingHandlerMapping requestMappingHandlerMapping, AdminEndpointRegistry adminEndpointRegistry) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.adminEndpointRegistry = adminEndpointRegistry;
    }

    @PostConstruct
    public void init() {
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
            if (value.hasMethodAnnotation(AdminOnlyEndpoint.class)) {
                key.getPatternValues().forEach(adminEndpointRegistry::registerAdminEndpoint);
            }
        });
    }

//    @AfterReturning("@annotation(adminOnlyEndpoint)")
//    public void registerAdminMethod(AdminOnlyEndpoint adminOnlyEndpoint) {
//        // Method level
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        Arrays.stream(stackTrace)
//                .filter(element -> element.getClassName().startsWith("com.eaproc.tutorials.librarymanagement"))
//                .findFirst()
//                .ifPresent(element -> adminEndpointRegistry.registerAdminEndpoint(element.getMethodName()));
//    }
//
//    @AfterReturning("@within(adminOnlyEndpoint)")
//    public void registerAdminClass(AdminOnlyEndpoint adminOnlyEndpoint) {
//        // Class level
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        Arrays.stream(stackTrace)
//                .filter(element -> element.getClassName().startsWith("com.eaproc.tutorials.librarymanagement"))
//                .findFirst()
//                .ifPresent(element -> adminEndpointRegistry.registerAdminEndpoint(element.getClassName()));
//    }
}
