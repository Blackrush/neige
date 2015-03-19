package neige.lang;

public class ParseException extends RuntimeException {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(String format, Object... args) {
        super(String.format(format, args));
    }

    public ParseException(Exception ex) {
        super(ex);
    }
}
