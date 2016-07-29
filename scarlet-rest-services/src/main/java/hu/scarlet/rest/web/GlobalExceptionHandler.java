package hu.scarlet.rest.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hu.scarlet.rest.web.dto.BaseResponse;
import hu.scarlet.rest.web.dto.ResponseStatus;

/**
 * global exception handler for all controller
 * just define your exception handler here
 * @author jmalyik
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/*
	 *  sample error handler to a specific exception
	@ExceptionHandler(SQLException.class)
	public String handleSQLException(HttpServletRequest request, Exception ex){
		logger.info("SQLException occured:: URL="+request.getRequestURL());
		return "database_error";
	}*/
	
	@ExceptionHandler(Exception.class)
	public BaseResponse<String> handleException(HttpServletRequest request, Exception ex){
		logger.error("Exception occured:: URL="+request.getRequestURL(), ex);
		BaseResponse<String> resp = new BaseResponse<>();
		resp.setStatus(ResponseStatus.Error);
		resp.setData(ex.getMessage());
		return resp;
	}
}
