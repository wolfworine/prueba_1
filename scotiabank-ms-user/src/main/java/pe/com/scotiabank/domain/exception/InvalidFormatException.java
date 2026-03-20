package pe.com.scotiabank.domain.exception;

public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String msg) {
        super(msg);
    }
}
