package cfg.lms.dto;

import lombok.Data;

@Data
public class VehicleRequest {
    private Long userId;
    private String licensePlate;
    private String vehicleType;
}