package cfg.lms.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cfg.lms.entity.Booking;
import cfg.lms.entity.ParkingSlot;
import cfg.lms.repository.BookingRepository;
import cfg.lms.repository.ParkingSlotRepository;
import cfg.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;

    public String bookSlot(Long userId, LocalDateTime start, LocalDateTime end) {
        List<ParkingSlot> available = slotRepository.findByStatus("AVAILABLE");
        if (available.isEmpty()) return "No available slot for selected time.";

        ParkingSlot slot = available.get(0); // take first available
        slot.setStatus("BOOKED");
        slotRepository.save(slot);

        Booking booking = new Booking();
        booking.setUser(userRepository.findById(userId).orElseThrow());
        booking.setSlot(slot);
        booking.setStartTime(start);
        booking.setEndTime(end);

        bookingRepository.save(booking);
        return "Slot booked successfully.";
    }
}