package hu.scarlet.rest.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.UserContext;
import hu.scarlet.rest.security.token.JwtAuthenticationToken;


/**
 * End-point for retrieving logged-in user details.
 * 
 * @author vladimir.stankovic
 *
 *         Aug 4, 2016
 */
@RestController("profileEndpoint")
public class ProfileEndpoint {
	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = SecurityConfig.PROFILE_ENTRY_POINT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> get(JwtAuthenticationToken token) throws Exception {
		if (token != null) {
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			String strUserContext = objectMapper.writeValueAsString((UserContext) token.getPrincipal());
			return new ResponseEntity<>(strUserContext, httpHeaders, HttpStatus.OK);
		} else {
			throw new Exception("Unable to read JWT token");
		}
	}
}
