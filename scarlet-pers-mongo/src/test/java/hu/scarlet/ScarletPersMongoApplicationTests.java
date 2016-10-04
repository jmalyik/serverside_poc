package hu.scarlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	
	private static Logger logger = LoggerFactory.getLogger(ScarletPersMongoApplicationTests.class);
	
	@Autowired
	private UserService userService;
	
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
