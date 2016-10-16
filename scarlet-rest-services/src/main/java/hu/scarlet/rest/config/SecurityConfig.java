package hu.scarlet.rest.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hu.scarlet.rest.security.RestAuthenticationEntryPoint;
import hu.scarlet.rest.security.ScarletRestUserDetailsService;
import hu.scarlet.rest.security.auth.JwtAuthenticationProvider;
import hu.scarlet.rest.security.auth.JwtTokenAuthenticationProcessingFilter;
import hu.scarlet.rest.security.auth.SkipPathRequestMatcher;
import hu.scarlet.rest.security.auth.ajax.AjaxAuthenticationProvider;
import hu.scarlet.rest.security.auth.ajax.AjaxLoginProcessingFilter;
import hu.scarlet.rest.security.token.TokenExtractor;

/**
 * The security config is based on
 * https://github.com/svlada/springboot-security-jwt
 * 
 * We are using JWT authentication, which is a token based authentication
 * 
 * The user should send its credentials to /api/auth/login
 * 
 * @author jmalyik
 *
 */
@Configuration
@ComponentScan(basePackages = { "hu.scarlet.config", "hu.scarlet.rest.security", "hu.scarlet.rest.web" })
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public ScarletRestUserDetailsService getScarletRestUserDetailsService() {
		return new ScarletRestUserDetailsService();
	}

	@Autowired
	ScarletRestUserDetailsService userDetailsService;

	public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

	// signup url
	public static final String SIGNUP_ENTRY_POINT = "/auth/signup";
	// login url, expects {username: username, password:pass} using ajax and
	// http post
	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
	// everything under this requires JWT token
	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
	// token refresh entry point
	public static final String TOKEN_REFRESH_ENTRY_POINT = "/auth/token";
	// fethes the user descriptor
	public static final String PROFILE_ENTRY_POINT = "/api/me";

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	@Autowired
	private AuthenticationFailureHandler failureHandler;
	@Autowired
	private AjaxAuthenticationProvider ajaxAuthenticationProvider;
	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private TokenExtractor tokenExtractor;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler,
				failureHandler);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Bean
	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
		List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
				tokenExtractor, matcher);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	@Bean
	protected BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // We don't need CSRF for JWT based authentication
				.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)

				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().authorizeRequests().antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
				// Login end-point
				.antMatchers(TOKEN_REFRESH_ENTRY_POINT, SIGNUP_ENTRY_POINT).permitAll()
				// Token refresh and signup end-points
				.and().authorizeRequests().antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
				// Protected API protected end-points
				.and().addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class);
	}
}
