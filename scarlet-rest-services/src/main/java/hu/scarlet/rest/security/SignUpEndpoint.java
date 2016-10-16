package hu.scarlet.rest.security;

import java.util.Arrays;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.config.RestMessages;
import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.auth.ajax.SignUpRequest;

@RestController
public class SignUpEndpoint {
	private Logger logger = LoggerFactory.getLogger(SignUpEndpoint.class);
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private UserService userService;
	@Autowired
	private RestMessages restMessages;

	@RequestMapping(value = SecurityConfig.SIGNUP_ENTRY_POINT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String signUp(@RequestBody SignUpRequest signupRequest) {

		Locale locale = LocaleContextHolder.getLocale();

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
			return restMessages.getBundle().getMessage("userCreated",
					restMessages.asOArr(signupRequest.getEmailAddress()),
					locale);
		} else {
			logger.info("User {} already exists", signupRequest.getEmailAddress());
			return restMessages.getBundle().getMessage("userAlreadyExists",
					restMessages.asOArr(signupRequest.getEmailAddress()), locale);
		}
	}
}
