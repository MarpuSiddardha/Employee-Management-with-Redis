package com.siddardha.redisDemonstration.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AdminConfig {

    @Value("${ADMIN_USERNAME:${admin.username}}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:${admin.password}}")
    private String adminPassword;
}
