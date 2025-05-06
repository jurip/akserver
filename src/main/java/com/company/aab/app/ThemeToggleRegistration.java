package com.company.aab.app;

import io.jmix.flowui.sys.registration.ComponentRegistration;
import io.jmix.flowui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThemeToggleRegistration {

    @Bean
    public ComponentRegistration themeToggle() {
        return ComponentRegistrationBuilder.create(ThemeToggle.class)
                .withComponentLoader("themeToggle", ThemeToggleLoader.class)
                .build();
    }
}
