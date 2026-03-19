package pe.com.scotiabank.domain.exception;

public class InvalidCredentialException extends RuntimeException {

    public InvalidCredentialException(String msg) {
        super(msg);
    }
}
