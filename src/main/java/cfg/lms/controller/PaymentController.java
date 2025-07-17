package cfg.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.lms.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam Long bookingId,
                                      @RequestParam Double amount) {
        return ResponseEntity.ok(paymentService.makePayment(bookingId, amount));
    }
}