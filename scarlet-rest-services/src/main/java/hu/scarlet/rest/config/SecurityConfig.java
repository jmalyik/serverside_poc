package hu.scarlet.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import hu.scarlet.pers.model.User;
import hu.scarlet.rest.security.ScarletRestUserDetailsService;

/**
 * 
 * @author jmalyik
 *
 */
@Configuration
@ComponentScan(basePackages = "hu.scarlet.config")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public ScarletRestUserDetailsService getScarletRestUserDetailsService() {
		return new ScarletRestUserDetailsService();
	}

	@Autowired
	ScarletRestUserDetailsService userDetailsService;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/register*", "/login", "/lostPassword").anonymous()
		// /user: this context path should be used by the logged in users - so the data should be returned under this path
		.antMatchers("/user", "/user/**").hasAnyRole(User.ROLE_USER, User.ROLE_ADMIN)
		// /company: this context path should be used by the logged in company users - so the data should be returned under this path
		.antMatchers("/company", "/company/**").hasAnyRole(User.ROLE_COMPANY_USER, User.ROLE_ADMIN)
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").failureUrl("/login?error=true").usernameParameter("username").passwordParameter("password").loginProcessingUrl("/security_check")
		.successHandler(new ScarletAuthenticationSuccessHandler());
		http.logout().logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessUrl("/");
	}
	
	@Autowired
	public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new ShaPasswordEncoder(256));
		builder.authenticationProvider(provider);
	}
}
