package hu.scarlet;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.scarlet.config.MongoConfig;
import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MongoConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class ScarletPersMongoApplicationTests {
	
	private static Logger logger = LoggerFactory.getLogger(ScarletPersMongoApplicationTests.class);
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testCreateUser() {
		User user = userService.createNew();
		user.setFirstName("jozsef");
		user.setLastName("malyik");
		user.setEmailAddress("xxx@foobar.hu");
		userService.save(user);
	}
	
	@Test
	public void testListAllUsers() {
		List<? extends User> users = userService.findAll();
		users.stream().forEach(x->logger.info(x.toString()));
	}
	
	@Test
	public void testDeleteUserByEmailAddress(){
		userService.deleteUserByEmailAddress("xxx@foobar.hu");
	}
	
	@Test
	public void testUpdateUserByEmailAddress(){
		User u = userService.getUserByEmailAddress("xxx@foobar.hu");
		u.setEmailAddress("xxy@foobar.hu");
		userService.update(u);
	}	
}
