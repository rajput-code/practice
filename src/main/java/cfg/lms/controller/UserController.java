package cfg.lms.controller;

import cfg.lms.entity.User;
import cfg.lms.service.UserService;
import cfg.lms.controller.ResponseData;
import cfg.lms.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@Valid @RequestBody User user) {
        ResponseData response = userService.register(user);
        return ResponseEntity.ok(response);
    }
}