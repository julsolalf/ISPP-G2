package ispp_g2.gastrostock.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongTurnException extends RuntimeException {
    public WrongTurnException(Collection<ObjectError> errors) {
        super(errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(",")));
    }

    public WrongTurnException(String message) {
        super(message);
    }
}
