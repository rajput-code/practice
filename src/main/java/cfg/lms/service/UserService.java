package cfg.lms.service;

import org.springframework.stereotype.Service;

import cfg.lms.controller.ResponseData;
import cfg.lms.entity.User;
import cfg.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseData register(User user) {
        ResponseData response = new ResponseData();

        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.setStatus("ERROR");
                response.setMessage("User already registered with this email.");
                response.setData(null);
            } else {
                long randomId;
                do {
                    randomId = 1000 + (long)(Math.random() * 9000); // e.g. 1000–9999
                } while (userRepository.existsById(randomId));

                user.setUserId(randomId);
                User savedUser = userRepository.save(user);

                response.setStatus("SUCCESS");
                response.setMessage("User registered successfully with User ID: " + savedUser.getUserId());
                response.setData(savedUser);
            }
        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("Registration failed: " + e.getMessage());
            response.setData(null);
        }

        return response;
    }
}

