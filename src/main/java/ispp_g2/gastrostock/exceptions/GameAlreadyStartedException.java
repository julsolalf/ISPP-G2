package ispp_g2.gastrostock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class GameAlreadyStartedException extends RuntimeException {

	private static final long serialVersionUID = -3906338266891937036L;

	public GameAlreadyStartedException(String message) {
		super(message);
	}
}
