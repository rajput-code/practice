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

    public double calculateFare(Booking booking) {
        long hours = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
        if (hours <= 0) hours = 1;

        String vehicleType = booking.getVehicle().getType().toUpperCase();

        double rate = switch (vehicleType) {
            case "CAR" -> 50;
            case "BIKE" -> 30;
            default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
        };

        return rate * hours;
    }
}