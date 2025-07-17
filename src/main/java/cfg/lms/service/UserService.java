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
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User is already registered!";
        }
        userRepository.save(user);
        return "User registered successfully.";
    }
}
