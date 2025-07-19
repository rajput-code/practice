package cfg.lms.exception;


import cfg.lms.controller.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors from @Valid in controllers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        ResponseData response = new ResponseData("ERROR", "Validation failed: " + errorMessage, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handles user registration duplication
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseData> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ResponseData response = new ResponseData("ERROR", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handles booking conflicts like overlapping slots or no available space
    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ResponseData> handleBookingConflict(BookingConflictException ex) {
        ResponseData response = new ResponseData("ERROR", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handles illegal inputs like wrong vehicle type or null user
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseData response = new ResponseData("ERROR", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Fallback for all other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleGeneralException(Exception ex) {
        ResponseData response = new ResponseData("ERROR", "Unexpected error: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}