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
        // Validate time range
        if (end.isBefore(start)) {
            return "Invalid time range: end time is before start time.";
        }

        // Check if user already has a booking in the selected timeframe
        List<Booking> userBookings = bookingRepository.findUserOverlappingBookings(userId, start, end);
        if (!userBookings.isEmpty()) {
            return "User already has a booking in this time frame.";
        }

        // Check if total overlapping bookings exceed slot limit
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(start, end);
        int maxSlots = 10;
        if (overlappingBookings.size() >= maxSlots) {
            return "No available slot for selected time.";
        }

        // Find or create a parking slot
        ParkingSlot slot;
        List<ParkingSlot> available = slotRepository.findByStatus("AVAILABLE");
        if (available.isEmpty()) {
            slot = new ParkingSlot();
        } else {
            slot = available.get(0);
        }

        slot.setStatus("BOOKED");
        slot.setType(vehicleType.toUpperCase());
        slotRepository.save(slot);

        // Generate unique random 4-digit booking ID
        long bookingId;
        do {
            bookingId = 1000 + (long) (Math.random() * 9000);
        } while (bookingRepository.existsByBookingId(bookingId));

        // Create and save booking
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUser(userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found")));
        booking.setSlot(slot);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setVehicle(vehicleRepository.findByLicensePlate(licensePlate)
            .orElseThrow(() -> new RuntimeException("Vehicle not found")));

        bookingRepository.save(booking);

        // Calculate fare
        long hours = java.time.Duration.between(start, end).toHours();
        if (hours <= 0) hours = 1;

        double rate = switch (vehicleType.toUpperCase()) {
            case "CAR" -> 50;
            case "BIKE" -> 30;
            default -> throw new IllegalArgumentException("Invalid vehicle type. Use CAR or BIKE.");
        };

        double totalFare = rate * hours;

        return "Slot booked successfully! \n" +
               "Booking ID: " + booking.getBookingId() + "\n" +
               "Vehicle: " + vehicleType.toUpperCase() + "\n" +
               "Total Hours: " + hours + "\n" +
               "Total Fare: ₹" + totalFare;
    }
}