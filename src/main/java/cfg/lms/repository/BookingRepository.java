package cfg.lms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cfg.lms.entity.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  
    boolean existsByBookingId(Long bookingId);

   
    @Query("SELECT b FROM Booking b WHERE " +
           "(b.startTime < :end AND b.endTime > :start)")
    List<Booking> findOverlappingBookings(LocalDateTime start, LocalDateTime end);

  
    @Query("SELECT b FROM Booking b WHERE " +
           "b.user.userId = :userId AND " +
           "(b.startTime < :end AND b.endTime > :start)")
    List<Booking> findUserOverlappingBookings(Long userId, LocalDateTime start, LocalDateTime end);
}
