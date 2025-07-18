package cfg.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.dto.PaymentRequest;
import cfg.lms.entity.Booking;
import cfg.lms.entity.Payment;
import cfg.lms.repository.BookingRepository;
import cfg.lms.repository.PaymentRepository;
import cfg.lms.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (paymentRepository.existsByBooking(booking)) {
            return ResponseEntity.ok("User already paid for the above Booking ID: " + request.getBookingId());
        }


		// Calculate the correct fare
        double expectedAmount = paymentService.calculateFare(booking);

        // Validate payment amount
        if (Double.compare(request.getAmount(), expectedAmount) != 0) {
            return ResponseEntity.badRequest().body(
                "Incorrect amount. Please pay the exact fare of ₹" + expectedAmount);
        }

        // Proceed with payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setStatus(request.getStatus() != null ? request.getStatus() : "PAID");

        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment successfully completed for Booking ID: " + request.getBookingId());
    }
}