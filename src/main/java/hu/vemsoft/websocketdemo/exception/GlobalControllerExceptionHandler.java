package hu.vemsoft.websocketdemo.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(value = GameNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Game was not found")
	public void handleGameNotFound(HttpServletRequest req, Exception e) throws Exception {
	}

}
