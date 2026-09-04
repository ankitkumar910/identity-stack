package dev.ankitkumar.identitystack.config;

import dev.ankitkumar.identitystack.entity.Role;
import dev.ankitkumar.identitystack.entity.User;
import dev.ankitkumar.identitystack.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${application.admin.username}")
    private String adminUsername;
    @Value("${application.admin.password}")
    private String adminPassword;
    @Value("${application.admin.first-name}")
    private String firstName;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String @NonNull ... args) {


        if (!userRepository.existsByRoles(Role.ADMIN)) {
            {
                if (adminUsername == null || adminUsername.isBlank())
                    throw new RuntimeException("Admin username is required.");
                if (adminPassword == null || adminPassword.isBlank())
                    throw new RuntimeException("Admin password is required.");

                User user = new User();
                user.setUsername(adminUsername);
                user.setPassword(passwordEncoder.encode(adminPassword));
                user.getRoles().add(Role.ADMIN);
                user.getRoles().add(Role.USER);
                user.setFirstName(firstName);

                userRepository.save(user);
            }
        }
    }


}
