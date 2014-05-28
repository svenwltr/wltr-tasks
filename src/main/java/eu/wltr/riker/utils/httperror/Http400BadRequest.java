package eu.wltr.riker.utils.httperror;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class Http400BadRequest extends RuntimeException {
	private static final long serialVersionUID = 3095254015781448757L;

	public Http400BadRequest() {
		super();

	}

}
