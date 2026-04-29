package kotobase_backend.comom.exceptions.CustomException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
