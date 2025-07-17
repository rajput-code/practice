package cfg.lms.controller;


import cfg.lms.dto.VehicleRequest;
import cfg.lms.entity.User;
import cfg.lms.entity.Vehicle;
import cfg.lms.repository.UserRepository;
import cfg.lms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getVehicleType());

        vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Vehicle registered successfully.");
    }
}