package hu.scarlet;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.scarlet.config.MongoConfig;
import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MongoConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class ScarletPersMongoApplicationTests extends TestCase {
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private UserService userService;

	/**
	 * TODO: A signuphoz: igy kell elmenteni a usert
	 */
	@Test
	public void testCreateJohnDoe() {
		User user = userService.createNew();
		user.setFirstName("john");
		user.setLastName("doe");
		user.setEmailAddress("jd" + System.currentTimeMillis() + "@foobar.hu");
		// a password-ot bcrypt-tel encode-olva kell betárolni
		user.setPassword(encoder.encode("alma"));
		// legalább egy role-nak lennie kell
		user.setRoles(Arrays.asList("Users"));
		userService.save(user);
	}

	@Test
	public void testCreateListUpdateAndDeleteUser() {
		assertEquals(0, userService.findAll().size());
		User user = userService.createNew();
		user.setFirstName("jozsef");
		user.setLastName("malyik");
		user.setEmailAddress("xxx@foobar.hu");
		userService.save(user);

		assertEquals(1, userService.findAll().size());
		assertEquals("xxx@foobar.hu", userService.findAll().get(0).getEmailAddress());

		User u = userService.getUserByEmailAddress("xxx@foobar.hu");
		u.setEmailAddress("xxy@foobar.hu");
		userService.update(u);

		assertEquals(1, userService.findAll().size());
		assertEquals("xxy@foobar.hu", userService.findAll().get(0).getEmailAddress());

		userService.deleteUserByEmailAddress("xxy@foobar.hu");
		assertEquals(0, userService.findAll().size());
	}
}
