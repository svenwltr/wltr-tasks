package eu.wltr.riker.utils.httperror;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class Http404NotFound extends RuntimeException {
	private static final long serialVersionUID = 3095254015781448757L;

	public Http404NotFound(String message) {
		super(message);

	}

}
