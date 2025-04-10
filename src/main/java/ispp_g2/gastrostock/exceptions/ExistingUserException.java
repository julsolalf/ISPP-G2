package ispp_g2.gastrostock.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExistingUserException extends RuntimeException {
    public ExistingUserException(Collection<ObjectError> errors) {
        super(errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(",")));
    }

    public ExistingUserException(String message) {
        super(message);
    }
}
