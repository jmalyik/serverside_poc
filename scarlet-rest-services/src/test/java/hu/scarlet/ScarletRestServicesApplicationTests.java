package hu.scarlet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.config.ScarletRestApplication;
import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.JwtSettings;
import hu.scarlet.rest.security.auth.JwtTokenAuthenticationProcessingFilter;
import hu.scarlet.rest.security.auth.ajax.AjaxLoginProcessingFilter;
import hu.scarlet.rest.security.auth.ajax.LoginRequest;
import hu.scarlet.rest.security.auth.ajax.SignUpRequest;
import hu.scarlet.rest.util.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import junit.framework.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ScarletRestApplication.class })
public class ScarletRestServicesApplicationTests extends TestCase {
	private static final String TEST_PASSWORD = "alma";
	private static final String TEST_USER = "john.smith@whatever.com";
	private static final String TEST_USER_FIRSTNAME = "John";
	private static final String TEST_USER_LASTNAME = "Smith";
	@Autowired
	private UserService userService;
	@Autowired
	private JwtSettings jwtSettings;
	private MockMvc mockMvc;
	private ObjectMapper requestMapper;
	private ObjectMapper responseMapper;
	private Logger logger = org.slf4j.LoggerFactory.getLogger(ScarletRestServicesApplicationTests.class);

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private JwtTokenAuthenticationProcessingFilter jwtFilter;

	@Autowired
	private AjaxLoginProcessingFilter loginFilter;

	@Before
	@Override
	public void setUp() {
		// setting up mock mvc
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity()).build();
		requestMapper = new ObjectMapper();
		requestMapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE, As.PROPERTY);
		requestMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		responseMapper = new ObjectMapper();
		responseMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	@Test
	public void test1SignUp() throws Exception {
		MockHttpServletRequestBuilder postReq = MockMvcRequestBuilders.post(SecurityConfig.SIGNUP_ENTRY_POINT)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		userService.deleteUserByEmailAddress(TEST_USER);
		SignUpRequest request = new SignUpRequest(TEST_USER, TEST_PASSWORD, false);
		request.setFirstName(TEST_USER_FIRSTNAME);
		request.setLastName(TEST_USER_LASTNAME);
		String jsonReq = requestMapper.writeValueAsString(request);
		logger.debug("Request: {}", jsonReq);
		postReq.content(jsonReq);
		MvcResult result = this.mockMvc
				.perform(postReq.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andReturn();
		logger.debug(result.getResponse().getContentAsString());
		User u = userService.getUserByEmailAddress(TEST_USER);
		assertNotNull(u);
		assertEquals(TEST_USER, u.getEmailAddress());
		assertEquals(TEST_USER_FIRSTNAME, u.getFirstName());
		assertEquals(TEST_USER_LASTNAME, u.getLastName());
	}

	@Test
	public void test2SignIn() {
		try {
			signIn();
		} catch (Exception e) {
			logger.error("Failed to sign in", e);
			fail(e.getMessage());
		}
	}

	private LoginResponse signIn() throws JsonProcessingException, Exception, UnsupportedEncodingException {
		MockHttpServletRequestBuilder postReq = MockMvcRequestBuilders
				.post(SecurityConfig.FORM_BASED_LOGIN_ENTRY_POINT)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		postReq.header(WebUtil.X_REQUESTED_WITH, WebUtil.XML_HTTP_REQUEST);
		LoginRequest login = new LoginRequest(TEST_USER, TEST_PASSWORD);
		String content = requestMapper.writeValueAsString(login);
		logger.debug("signin req: {}", content);
		postReq.content(content);
		MvcResult result = this.mockMvc.perform(postReq.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		logger.debug(responseBody);
		return responseMapper.readValue(responseBody.getBytes(), LoginResponse.class);
	}

	@Test
	public void test3GetProfileData() {
		try {
			LoginResponse loginResponse = signIn();
			assertNotNull("Failed to sign in", loginResponse);
			MockHttpServletRequestBuilder getReq = MockMvcRequestBuilders.get(SecurityConfig.PROFILE_ENTRY_POINT)
					.accept(MediaType.APPLICATION_JSON);
			getReq.header(WebUtil.X_REQUESTED_WITH, WebUtil.XML_HTTP_REQUEST);
			getReq.header(SecurityConfig.JWT_TOKEN_HEADER_PARAM,
					String.format("Bearer %s", loginResponse.getToken()));
			logger.debug("---> fetching profile data");
			MvcResult result = this.mockMvc.perform(getReq.accept(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk())
					.andReturn();
			String responseBody = result.getResponse().getContentAsString();
			logger.debug("---> Profile data: {}", responseBody);
		} catch (Exception e) {
			logger.error("Failed to get profile data", e);
			fail(e.getMessage());
		}
	}


	public void test3GenerateJWTKey() {
		Map<String, Object> map = new HashMap<>();
		Claims cl = new DefaultClaims(map);
		cl.setSubject(TEST_USER);
		cl.put("scopes", Arrays.asList("admin", "paraszt"));
		String compactJws = Jwts.builder().setIssuer(jwtSettings.getTokenIssuer()).setHeaderParam("typ", "JWT")
				.setClaims(cl)
				.signWith(SignatureAlgorithm.HS256, jwtSettings.getTokenSigningKey())
				.compact();
		logger.info(compactJws); // this is the output that must be use to
									// authenticate
		/**
		 * An HTTP request must contain the following HTTP header
		 * 
		 * X-Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
		 * eyJzdWIiOiJqb3pzZWYubWFseWlrQGdtYWlsLmNvbSIsInNjb3BlcyI6WyJhZG1pbiIsInBhcmFzenQiXX0
		 * .NRMJW2E8386B0bDUDDJQLqtsch3BWTSSJ4mOug4QxjQ
		 * 
		 * template: X-Authorization: Bearer<space><compactJws>
		 * 
		 */
	}

}
