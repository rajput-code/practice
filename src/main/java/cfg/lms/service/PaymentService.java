package cfg.lms.service;

import org.springframework.stereotype.Service;

import cfg.lms.entity.Booking;
import cfg.lms.entity.Payment;
import cfg.lms.repository.BookingRepository;
import cfg.lms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public String makePayment(Long bookingId, Double amount) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setStatus("PAID");

        paymentRepository.save(payment);
        return "Payment successful.";
    }
}