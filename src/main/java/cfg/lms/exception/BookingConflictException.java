package cfg.lms.exception;

public class BookingConflictException extends RuntimeException {

    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}