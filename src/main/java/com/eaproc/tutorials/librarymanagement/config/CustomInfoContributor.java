package com.eaproc.tutorials.librarymanagement.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        details.put("app.name", "Library Management App");
        details.put("app.description", "An application to manage a library");
        details.put("app.version", "1.0.0");
        details.put("company.name", "Eaproc");

        builder.withDetails(details);
    }
}
