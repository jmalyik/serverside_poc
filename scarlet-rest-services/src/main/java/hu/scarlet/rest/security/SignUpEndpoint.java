package hu.scarlet.rest.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.auth.ajax.SignUpRequest;

@RestController
public class SignUpEndpoint {
	private Logger logger = LoggerFactory.getLogger(SignUpEndpoint.class);
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private UserService userService;

	@RequestMapping(value = SecurityConfig.SIGNUP_ENTRY_POINT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String signUp(@RequestBody SignUpRequest signupRequest) {
		if (userService.getUserByEmailAddress(signupRequest.getEmailAddress()) == null) {
			User user = userService.createNew();
			user.setFirstName(signupRequest.getFirstName());
			user.setLastName(signupRequest.getLastName());
			user.setEmailAddress(signupRequest.getEmailAddress());
			// a password-ot bcrypt-tel encode-olva kell betárolni
			user.setPassword(encoder.encode(signupRequest.getPassword()));
			// legalább egy role-nak lennie kell
			// TODO: figure out User or Company?
			user.setRoles(Arrays.asList("Users"));
			userService.save(user);
			return String.format("User %s created.", user.getEmailAddress());
		} else {
			logger.info("User jd@foobar.hu already exists");
			return null;
		}
	}
}
