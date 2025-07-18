package cfg.lms.controller;

import cfg.lms.dto.VehicleRequest;
import cfg.lms.entity.User;
import cfg.lms.entity.Vehicle;
import cfg.lms.repository.UserRepository;
import cfg.lms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerVehicle(@RequestBody VehicleRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Vehicle> existingVehicle = vehicleRepository
            .findByUserUserIdAndLicensePlate(request.getUserId(), request.getLicensePlate());

        if (existingVehicle.isPresent()) {
            return ResponseEntity.ok("Vehicle is already registered with Vehicle ID: " +
                    existingVehicle.get().getVehicleId());
        }

        // Generate unique 4-digit vehicle ID different from user ID
        long vehicleId;
        do {
            vehicleId = 1000 + (long)(Math.random() * 9000);
        } while (vehicleId == request.getUserId() || vehicleRepository.existsByVehicleId(vehicleId));

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(vehicleId);
        vehicle.setUser(user);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getVehicleType());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Vehicle registered successfully with Vehicle ID: " + savedVehicle.getVehicleId());
    }
}
