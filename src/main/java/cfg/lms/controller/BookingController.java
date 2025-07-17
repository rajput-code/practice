package cfg.lms.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.dto.BookingRequest;
import cfg.lms.service.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<String> book(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(
            bookingService.bookSlot(
                request.getUserId(),
                request.getVehicleType(),
                request.getLicensePlate(),
                LocalDateTime.parse(request.getStart()),
                LocalDateTime.parse(request.getEnd())
            )
        );
    }
}
