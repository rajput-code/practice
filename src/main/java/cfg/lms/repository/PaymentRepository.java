package cfg.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.lms.entity.Booking;
import cfg.lms.entity.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	boolean existsByBooking(Booking booking);
}
