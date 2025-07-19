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
    private final ReportRepository reportRepository;

    @GetMapping("/users")
    public ResponseEntity<ResponseData> getAllUsers() {
        List<User> users = userRepository.findAll();
        return buildSuccessResponse("Users fetched successfully", users);
    }

    @GetMapping("/vehicles")
    public ResponseEntity<ResponseData> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return buildSuccessResponse("Vehicles fetched successfully", vehicles);
    }

    @GetMapping("/slots")
    public ResponseEntity<ResponseData> getAllSlots() {
        List<ParkingSlot> slots = slotRepository.findAll();
        return buildSuccessResponse("Parking slots fetched successfully", slots);
    }

    @GetMapping("/bookings")
    public ResponseEntity<ResponseData> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return buildSuccessResponse("Bookings fetched successfully", bookings);
    }

    @GetMapping("/payments")
    public ResponseEntity<ResponseData> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return buildSuccessResponse("Payments fetched successfully", payments);
    }
    
    @GetMapping("/reports")
    public ResponseEntity<ResponseData> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return buildSuccessResponse("Reports fetched successfully", reports);
    }

    @PutMapping("/slot/{id}/status")
    public ResponseEntity<ResponseData> updateSlotStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
            slot.setStatus(status.toUpperCase());
            slotRepository.save(slot);
            return buildSuccessResponse("Slot status updated successfully", slot);
        } catch (Exception e) {
            return buildErrorResponse("Failed to update slot status: " + e.getMessage());
        }
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<ResponseData> deleteBooking(@PathVariable Long id) {
        try {
            bookingRepository.deleteById(id);
            return buildSuccessResponse("Booking deleted by admin.", null);
        } catch (Exception e) {
            return buildErrorResponse("Failed to delete booking: " + e.getMessage());
        }
    }
    @DeleteMapping("/payment/{paymentId}")
    public ResponseEntity<ResponseData> deletePayment(@PathVariable Long paymentId) {
        ResponseData response = new ResponseData();

        if (!paymentRepository.existsById(paymentId)) {
            response.setStatus("ERROR");
            response.setMessage("Payment with ID " + paymentId + " not found.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            paymentRepository.deleteById(paymentId);
            response.setStatus("SUCCESS");
            response.setMessage("Payment with ID " + paymentId + " deleted successfully.");
            response.setData(null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("ERROR");
            response.setMessage("Failed to delete payment: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(500).body(response);
        }
    }

    
    private ResponseEntity<ResponseData> buildSuccessResponse(String message, Object data) {
        ResponseData response = new ResponseData();
        response.setStatus("SUCCESS");
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<ResponseData> buildErrorResponse(String errorMessage) {
        ResponseData response = new ResponseData();
        response.setStatus("ERROR");
        response.setMessage(errorMessage);
        response.setData(null);
        return ResponseEntity.badRequest().body(response);
    }
}