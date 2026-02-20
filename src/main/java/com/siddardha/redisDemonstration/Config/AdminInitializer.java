package com.siddardha.redisDemonstration.Config;

import com.siddardha.redisDemonstration.Model.Role;
import com.siddardha.redisDemonstration.Model.User;
import com.siddardha.redisDemonstration.Repository.RoleRepository;
import com.siddardha.redisDemonstration.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger logger  = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
                            RoleRepository roleRepository,
                            AdminConfig adminConfig,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.adminConfig = adminConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create ROLE_ADMIN if not exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        // Create admin user if not exists
        if (!userRepository.existsByUsername(adminConfig.getAdminUsername())) {
            User admin = new User();
            admin.setUsername(adminConfig.getAdminUsername());
            admin.setPassword(passwordEncoder.encode(adminConfig.getAdminPassword()));
            admin.setRoles(new HashSet<>());
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
            logger.info("Admin user created with username: {}", adminConfig.getAdminUsername());
        } else {
            logger.info("Admin user already exists with username: {}", adminConfig.getAdminUsername());
        }
    }
}
