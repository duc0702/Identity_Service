package com.example.indentity_service.configuration;

import com.example.indentity_service.entity.User;
import com.example.indentity_service.enums.Role;
import com.example.indentity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var role = new HashSet<String>();
                role.add(Role.ADMIN.name());
                User user = new User().builder().
                        username("admin")
                        .password(passwordEncoder.encode("admin"))
//                      .roles(Role.ADMIN.name())
                        .build();
                userRepository.save(user);
                log.warn("Create admin user with default password: admin");
            }
        };
    }
}
