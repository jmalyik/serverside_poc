package hu.scarlet;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.scarlet.config.MongoConfig;
import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import junit.framework.TestCase;

/**
 * 
 * @author jmalyik
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MongoConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class ScarletPersMongoApplicationTests extends TestCase {
	private Logger logger = LoggerFactory.getLogger(ScarletPersMongoApplicationTests.class);
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private UserService userService;

	/**
	 * TODO: A signuphoz: igy kell elmenteni a usert
	 */
	@Test
	public void testCreateJohnDoe() {
		if(userService.getUserByEmailAddress("jd@foobar.hu") == null){
		User user = userService.createNew();
		user.setFirstName("john");
		user.setLastName("doe");
		user.setEmailAddress("jd@foobar.hu");
			// a password-ot bcrypt-tel encode-olva kell betárolni
		user.setPassword(encoder.encode("alma"));
			// legalább egy role-nak lennie kell
		user.setRoles(Arrays.asList("Users"));
		userService.save(user);
		}else{
			logger.info("User jd@foobar.hu already exists");
		}
	}

	@Test
	public void testCreateListUpdateAndDeleteUser() {
		removeUser("xxx@foobar.hu");
		removeUser("xxy@foobar.hu");
		User user = userService.createNew();
		user.setFirstName("jozsef");
		user.setLastName("malyik");
		user.setEmailAddress("xxx@foobar.hu");
		userService.save(user);

		User xxx = userService.getUserByEmailAddress("xxx@foobar.hu");
		assertNotNull(xxx);
		assertEquals("jozsef", xxx.getFirstName());
		assertEquals("malyik", xxx.getLastName());
		assertEquals("xxx@foobar.hu", xxx.getEmailAddress());
		xxx.setEmailAddress("xxy@foobar.hu");
		userService.update(xxx);

		xxx = userService.getUserByEmailAddress("xxx@foobar.hu");
		assertNull(xxx);
		User xxy = userService.getUserByEmailAddress("xxy@foobar.hu");

		assertNotNull(xxy);
		assertEquals("jozsef", xxy.getFirstName());
		assertEquals("malyik", xxy.getLastName());
		assertEquals("xxy@foobar.hu", xxy.getEmailAddress());

		userService.deleteUserByEmailAddress("xxy@foobar.hu");
	}

	private void removeUser(String email) {
		User u = userService.getUserByEmailAddress(email);
		if (u != null) {
			userService.deleteUserByEmailAddress(email);
		}
	}
}
