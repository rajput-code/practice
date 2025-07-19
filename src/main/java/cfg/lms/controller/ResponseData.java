package cfg.lms.controller;

import cfg.lms.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
	private String status;
    private String message;
    private Object data;
}