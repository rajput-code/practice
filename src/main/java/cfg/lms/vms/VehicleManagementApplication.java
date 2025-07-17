package cfg.lms.vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import cfg.lms.entity.*;
import cfg.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor

@SpringBootApplication(scanBasePackages= {"cfg.lms"})
@EntityScan("cfg.lms")
@EnableJpaRepositories("cfg.lms")
public class VehicleManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepo,
                          VehicleRepository vehicleRepo,
                          ParkingSlotRepository slotRepo,
                          BookingRepository bookingRepo,
                          PaymentRepository paymentRepo) {
        return args -> {
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== VEHICLE PARKING SYSTEM ===");
            System.out.print("Enter your email to register/login: ");
            String email = scanner.nextLine().trim();

            Optional<User> userOpt = userRepo.findByEmail(email);
            User user;

            if (userOpt.isPresent()) {
                System.out.println("✅ User already registered.");
                user = userOpt.get();
            } else {
                System.out.print("New user. Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                userRepo.save(user);
                System.out.println("✅ User registered successfully.");
            }

            // Step 2: Enter vehicle details
            System.out.print("Enter vehicle license plate: ");
            String plate = scanner.nextLine();

            System.out.print("Enter vehicle type (CAR/BIKE): ");
            String type = scanner.nextLine().trim().toUpperCase();

            if (!type.equals("CAR") && !type.equals("BIKE")) {
                System.out.println("❌ Invalid vehicle type. Only CAR or BIKE allowed.");
                return;
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(plate);
            vehicle.setType(type);
            vehicle.setUser(user);
            vehicleRepo.save(vehicle);

            // Step 3: Check for available slots
            List<ParkingSlot> availableSlots = slotRepo.findByStatus("AVAILABLE");
            if (availableSlots.isEmpty()) {
                System.out.println("❌ No parking slots available.");
                return;
            } else {
                System.out.println("✅ Available slots: " + availableSlots.size());
            }

            // Step 4: Take booking time
            System.out.print("Enter booking start time (yyyy-MM-ddTHH:mm): ");
            LocalDateTime startTime = LocalDateTime.parse(scanner.nextLine());

            System.out.print("Enter booking end time (yyyy-MM-ddTHH:mm): ");
            LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());

            if (!endTime.isAfter(startTime)) {
                System.out.println("❌ End time must be after start time.");
                return;
            }

            long hours = Duration.between(startTime, endTime).toHours();
            double rate = type.equals("CAR") ? 50 : 30;
            double total = rate * hours;

            System.out.printf("✅ Total booking time: %d hours | Rate: ₹%.2f/hour | Total: ₹%.2f%n", hours, rate, total);

            // Step 5: Book the slot
            ParkingSlot slot = availableSlots.get(0);
            slot.setStatus("BOOKED");
            slotRepo.save(slot);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setSlot(slot);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            bookingRepo.save(booking);

            // Step 6: Make payment automatically
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(total);
            payment.setStatus("PAID");
            paymentRepo.save(payment);

            System.out.println("💳 Payment successful. Booking complete.");
            System.out.printf("📄 Booking ID: %d | Payment: ₹%.2f | Status: PAID%n",
                    booking.getBookingId(), total);
        };
    }
}
