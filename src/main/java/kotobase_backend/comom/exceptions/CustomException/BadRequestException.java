package kotobase_backend.comom.exceptions.CustomException;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
