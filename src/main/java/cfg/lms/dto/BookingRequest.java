package cfg.lms.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private String vehicleType;
    private String licensePlate;
    private String start;
    private String end;
}