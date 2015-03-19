package neige.lang;

public class NeigeRuntimeException extends RuntimeException {
    public NeigeRuntimeException() {
    }

    public NeigeRuntimeException(String format, Object... arguments) {
        super(String.format(format, arguments));
    }

    public NeigeRuntimeException(String message) {
        super(message);
    }

    public NeigeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeigeRuntimeException(Throwable cause) {
        super(cause);
    }

    public NeigeRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
