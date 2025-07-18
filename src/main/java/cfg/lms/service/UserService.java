package cfg.lms.service;

import org.springframework.stereotype.Service;

import cfg.lms.entity.User;
import cfg.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String register(User user) {
        try {
            return userRepository.findByEmail(user.getEmail())
                .map(existingUser -> "User is already registered with User ID: " + existingUser.getUserId())
                .orElseGet(() -> {
                    long randomId;
                    do {
                    	 randomId = 1000 + (long)(Math.random() * 900); // Generates a number between 0 and 999999
                    } while (userRepository.existsById(randomId)); // Ensure uniqueness

                    user.setUserId(randomId);
                    User savedUser = userRepository.save(user);
                    return "User registered successfully with User ID: " + savedUser.getUserId();
                });

        } catch (Exception e) {
            return "An error occurred during registration: " + e.getMessage();
        }
    }
}
