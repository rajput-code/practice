package cfg.lms.dto;

import lombok.Data;

@Data
public class ReportRequest {
    private Long userId;
    private String description;
}