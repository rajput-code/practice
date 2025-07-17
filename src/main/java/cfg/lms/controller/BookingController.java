package cfg.lms.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.service.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<String> book(@RequestParam Long userId,
                                       @RequestParam String start,
                                       @RequestParam String end) {
        LocalDateTime s = LocalDateTime.parse(start);
        LocalDateTime e = LocalDateTime.parse(end);
        return ResponseEntity.ok(bookingService.bookSlot(userId, s, e));
    }
}