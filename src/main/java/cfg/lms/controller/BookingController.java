package cfg.lms.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.dto.BookingRequest;
import cfg.lms.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<ResponseData> bookSlot(@Valid @RequestBody BookingRequest request) {
        ResponseData response = bookingService.bookSlot(
            request.getUserId(),
            request.getVehicleType(),
            request.getLicensePlate(),
            request.getStart(),
            request.getEnd()
        );

        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}

