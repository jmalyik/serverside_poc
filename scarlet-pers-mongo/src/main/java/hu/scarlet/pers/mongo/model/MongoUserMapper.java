package hu.scarlet.pers.mongo.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import hu.scarlet.pers.model.User;

/**
 * simple mapper to switch  between User and MongoUser
 * @author jmalyik
 *
 */
@Mapper
public interface MongoUserMapper {

	MongoUserMapper INSTANCE = Mappers.getMapper(MongoUserMapper.class);
	
	MongoUser convertToMongoUser(User user);
}
