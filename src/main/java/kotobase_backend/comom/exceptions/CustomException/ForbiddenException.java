package kotobase_backend.comom.exceptions.CustomException;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
