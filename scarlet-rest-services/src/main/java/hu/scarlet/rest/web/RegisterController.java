package hu.scarlet.rest.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DuplicateKeyException;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.web.dto.CreateUserResponse;
import hu.scarlet.rest.web.dto.ResponseStatus;

/**
 * 
 * @author jmalyik
 *
 */
@RestController("/register")
public class RegisterController {
	
	private Logger logger = LoggerFactory.getLogger(RegisterController.class); 
	
	@Autowired
	private UserService userService;
	// TODO: localize
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody CreateUserResponse createUser(@RequestBody User user){
		logger.info("CreateUser called with {}", user);
		CreateUserResponse response = new CreateUserResponse();
		try {
			User createdUser = userService.save(user);
			response.setData(createdUser.getId());
		} catch (DuplicateKeyException kex) {
			logger.error("An error occured during creating a user", kex);
			response.setStatus(ResponseStatus.Error);
			response.setResponseMessage("Email address already registered!");
		} catch (Exception ex) {
			logger.error("An error occured during creating a user", ex);
			response.setStatus(ResponseStatus.Error);
			response.setResponseMessage("An error occured please try again later!");
		}
		return response;
	}
}
