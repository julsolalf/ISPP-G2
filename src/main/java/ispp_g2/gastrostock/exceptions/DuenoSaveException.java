package ispp_g2.gastrostock.exceptions;

public class DuenoSaveException extends RuntimeException {

    public DuenoSaveException(String message) {
        super(message);
    }

    public DuenoSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
