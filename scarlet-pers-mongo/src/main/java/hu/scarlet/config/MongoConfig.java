package hu.scarlet.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

import hu.scarlet.pers.model.UserService;
import hu.scarlet.pers.mongo.serviceimpl.MongoUserService;

/**
 * 
 * @author jmalyik
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = { "hu.scarlet.pers.mongo.repositories", "hu.scarlet.pers.mongo.model" })
public class MongoConfig {
	private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);
	@Autowired
	private Environment env;

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		// TODO: when the db will be switched to the authenticated version
		// this creds shall be introduced
		// UserCredentials userCredentials = new UserCredentials("joe",
		// "secret");
		String host = env.getProperty("mongo.host");
		int port = Integer.parseInt(env.getProperty("mongo.port"));
		String db = env.getProperty("mongo.database");
		logger.info("MongoDB: connecting to {}:{} using db {}", host, port, db);
		return new SimpleMongoDbFactory(new MongoClient(host, port), db);// , userCredentials);
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}
	
	@Bean
	public UserService userService(){
		return new MongoUserService();
	}
}
