package cfg.lms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingRequest {
	private Long userId;
    private String vehicleType;
    private String licensePlate;
    private LocalDateTime start;
    private LocalDateTime end;
}