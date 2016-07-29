package hu.scarlet.pers.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.mongo.model.MongoUser;

public interface UserRepository extends MongoRepository<MongoUser, String>{

	void deleteByEmailAddress(String emailAddress);

	User findByEmailAddress(String emailAddress);
	
}
