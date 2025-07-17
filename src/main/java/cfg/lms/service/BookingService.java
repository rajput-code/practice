package cfg.lms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cfg.lms.entity.Booking;
import cfg.lms.entity.ParkingSlot;
import cfg.lms.repository.BookingRepository;
import cfg.lms.repository.ParkingSlotRepository;
import cfg.lms.repository.UserRepository;
import cfg.lms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public String bookSlot(Long userId, String vehicleType, String licensePlate, LocalDateTime start, LocalDateTime end) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(start, end);
        int maxSlots = 100;

        if (overlappingBookings.size() >= maxSlots) {
            return "No available slot for selected time.";
        }

        List<ParkingSlot> available = slotRepository.findByStatus("AVAILABLE");
        ParkingSlot slot;

        if (available.isEmpty()) {
            slot = new ParkingSlot();
            slot.setStatus("BOOKED");
            slotRepository.save(slot);
        } else {
            slot = available.get(0);
            slot.setStatus("BOOKED");
            slotRepository.save(slot);
        }

        Booking booking = new Booking();
        booking.setUser(userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found")));
        booking.setSlot(slot);
        booking.setStartTime(start);
        booking.setEndTime(end);

        // ✅ Fix: Set vehicle
        booking.setVehicle(vehicleRepository.findByLicensePlate(licensePlate)
        	    .orElseThrow(() -> new RuntimeException("Vehicle not found")));
        bookingRepository.save(booking);

        long hours = java.time.Duration.between(start, end).toHours();
        if (hours <= 0) hours = 1;

        double rate = switch (vehicleType.toUpperCase()) {
            case "CAR" -> 50;
            case "BIKE" -> 30;
            default -> throw new IllegalArgumentException("Invalid vehicle type. Use CAR or BIKE.");
        };

        double totalFare = rate * hours;

        return "Slot booked successfully for " + hours + " hour(s).\n" +
               "Vehicle: " + vehicleType.toUpperCase() + "\n" +
               "Total Fare: ₹" + totalFare;
    }
}