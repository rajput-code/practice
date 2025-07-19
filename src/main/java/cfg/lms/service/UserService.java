package cfg.lms.service;

import org.springframework.stereotype.Service;
import cfg.lms.controller.ResponseData;
import cfg.lms.entity.User;
import cfg.lms.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import cfg.lms.controller.ResponseData;
import cfg.lms.entity.User;
import cfg.lms.exception.UserAlreadyExistsException;
import cfg.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseData register(@Valid User user) {
        // Check if user exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already registered with this email.");
        }

        // Generate unique user ID
        long randomId;
        do {
            randomId = 1000 + (long)(Math.random() * 9000);
        } while (userRepository.existsById(randomId));

        user.setUserId(randomId);
        User savedUser = userRepository.save(user);

        // Return structured success response
        return new ResponseData("SUCCESS",
                "User registered successfully with User ID: " + savedUser.getUserId(),
                savedUser);
    }
}
