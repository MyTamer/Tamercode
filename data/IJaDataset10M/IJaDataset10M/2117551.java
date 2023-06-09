package objot.util;

public class InvalidValueException extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 5937875041908748518L;

    public InvalidValueException() {
    }

    public InvalidValueException(String s) {
        super(s);
    }

    public InvalidValueException(Throwable cause) {
        initCause(cause);
    }

    public InvalidValueException(String s, Throwable cause) {
        super(s);
        initCause(cause);
    }

    public InvalidValueException(int value) {
        super("invalid value " + value);
    }

    public InvalidValueException(long value) {
        super("invalid value " + value);
    }
}
