package cfg.lms.controller;


import cfg.lms.entity.*;
import cfg.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    // View all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // View all vehicles
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleRepository.findAll());
    }

    // View all parking slots
    @GetMapping("/slots")
    public ResponseEntity<List<ParkingSlot>> getAllSlots() {
        return ResponseEntity.ok(slotRepository.findAll());
    }

    // View all bookings
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    // View all payments
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    // Update parking slot status (example: manually release a booked slot)
    @PutMapping("/slot/{id}/status")
    public ResponseEntity<String> updateSlotStatus(@PathVariable Long id, @RequestParam String status) {
        ParkingSlot slot = slotRepository.findById(id).orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setStatus(status.toUpperCase());
        slotRepository.save(slot);
        return ResponseEntity.ok("Slot status updated to: " + status.toUpperCase());
    }

    // Delete a booking (admin override)
    @DeleteMapping("/booking/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingRepository.deleteById(id);
        return ResponseEntity.ok("Booking deleted by admin.");
    }
}
