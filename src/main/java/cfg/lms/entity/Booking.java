package cfg.lms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema="VehicleManagement",name="Booking")
public class Booking {
    @Id @GeneratedValue
    private Long bookingId;

    @ManyToOne
    private User user;

    @ManyToOne
    private ParkingSlot slot;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}