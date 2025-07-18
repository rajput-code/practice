package cfg.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.entity.User;
import cfg.lms.repository.UserRepository;
import cfg.lms.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@RequestBody User user) {
        ResponseData response = userService.register(user);
        return response.getStatus().equalsIgnoreCase("SUCCESS")
            ? ResponseEntity.ok(response)
            : ResponseEntity.badRequest().body(response);
    }
}