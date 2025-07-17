package cfg.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cfg.lms.dto.ReportRequest;
import cfg.lms.entity.Report;
import cfg.lms.repository.ReportRepository;
import cfg.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @PostMapping("/submit")
    public ResponseEntity<String> submitReport(@RequestBody ReportRequest request) {
        Report report = new Report();
        report.setDescription(request.getDescription());
        report.setUser(userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found")));

        reportRepository.save(report);

        return ResponseEntity.ok("Report submitted successfully.");
    }
}

