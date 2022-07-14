package com.epam.spring.homework3;

import com.epam.spring.homework3.model.entity.Role;
import com.epam.spring.homework3.model.entity.User;
import com.epam.spring.homework3.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@PropertySource("classpath:security.properties")
@SpringBootApplication
@RestController
public class Application {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    protected CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            saveAdmin(passwordEncoder, userRepository);
        };
    }

    private void saveAdmin(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setRole(Role.ADMIN);
        admin.setEnable(true);

        String encodedPassword = passwordEncoder.encode(adminPassword);
        admin.setPassword(encodedPassword);

        Optional<User> possibleUser = userRepository.findByEmail(admin.getEmail());
        if (possibleUser.isEmpty()) {
            userRepository.save(admin);
        }
    }
}
