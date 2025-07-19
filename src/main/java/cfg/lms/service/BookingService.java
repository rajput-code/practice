package cfg.lms.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cfg.lms.controller.ResponseData;
import cfg.lms.entity.Booking;
import cfg.lms.entity.ParkingSlot;
import cfg.lms.repository.BookingRepository;
import cfg.lms.repository.ParkingSlotRepository;
import cfg.lms.repository.UserRepository;
import cfg.lms.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public ResponseData bookSlot(Long userId, String vehicleType, String licensePlate, LocalDateTime start, LocalDateTime end) {
        ResponseData response = new ResponseData();

        try {
            // Validate time range
            if (end.isBefore(start)) {
                response.setStatus("ERROR");
                response.setMessage("Invalid time range: end time is before start time.");
                return response;
            }

            // Check for existing user bookings
            List<Booking> userBookings = bookingRepository.findUserOverlappingBookings(userId, start, end);
            if (!userBookings.isEmpty()) {
                response.setStatus("ERROR");
                response.setMessage("User already has a booking in this time frame.");
                return response;
            }

            // Check if slots are full
            List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(start, end);
            int maxSlots = 10;
            if (overlappingBookings.size() >= maxSlots) {
                response.setStatus("ERROR");
                response.setMessage("No available slot for selected time.");
                return response;
            }

            // Get available slot
            ParkingSlot slot;
            List<ParkingSlot> available = slotRepository.findByStatus("AVAILABLE");
            if (available.isEmpty()) {
                slot = new ParkingSlot(); // fallback if none exist
            } else {
                slot = available.get(0);
            }

            slot.setStatus("BOOKED");
            slot.setType(vehicleType.toUpperCase());
            slotRepository.save(slot);

            // Generate unique booking ID
            long bookingId;
            do {
                bookingId = 1000 + (long) (Math.random() * 9000);
            } while (bookingRepository.existsByBookingId(bookingId));

            // Save booking
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

            response.setStatus("SUCCESS");
            response.setMessage("Slot booked successfully!");
            response.setData(new BookingSummary(bookingId, vehicleType.toUpperCase(), hours, totalFare));

        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("Booking failed: " + e.getMessage());
            response.setData(null);
        }

        return response;
    }

    @Data
    @AllArgsConstructor
    static class BookingSummary {
        private long bookingId;
        private String vehicle;
        private long totalHours;
        private double totalFare;
    }
}
