package hu.scarlet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.scarlet.rest.config.ScarletRestApplication;
import hu.scarlet.rest.security.JwtSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScarletRestApplication.class)
public class ScarletRestServicesApplicationTests {
	@Autowired
	private JwtSettings jwtSettings;

	Logger logger = org.slf4j.LoggerFactory.getLogger(ScarletRestServicesApplicationTests.class);
	@Test
	public void generateJWTKey() {
		Map<String, Object> map = new HashMap<>();
		Claims cl = new DefaultClaims(map);
		cl.setSubject("jozsef.malyik@gmail.com");
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
