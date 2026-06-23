package com.wip.helpdesk_ticketing_system.config;

import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default admin if not exists
        if (userRepository.findByEmail("admin@helpdesk.com").isEmpty()) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@helpdesk.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Default admin created: admin@helpdesk.com / admin123");
        }

        // Create default agent if not exists
        if (userRepository.findByEmail("agent@helpdesk.com").isEmpty()) {
            User agent = new User();
            agent.setName("Support Agent");
            agent.setEmail("agent@helpdesk.com");
            agent.setPasswordHash(passwordEncoder.encode("agent123"));
            agent.setRole(Role.AGENT);
            userRepository.save(agent);
            System.out.println("✅ Default agent created: agent@helpdesk.com / agent123");
        }

        // Create default end user if not exists
        if (userRepository.findByEmail("user@helpdesk.com").isEmpty()) {
            User user = new User();
            user.setName("End User");
            user.setEmail("user@helpdesk.com");
            user.setPasswordHash(passwordEncoder.encode("user123"));
            user.setRole(Role.END_USER);
            userRepository.save(user);
            System.out.println("✅ Default end-user created: user@helpdesk.com / user123");
        }
    }
}
