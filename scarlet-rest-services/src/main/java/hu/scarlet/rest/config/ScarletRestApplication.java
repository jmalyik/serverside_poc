package hu.scarlet.rest.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 
 * @author jmalyik
 *
 */
@EnableConfigurationProperties
@SpringBootApplication
public class ScarletRestApplication {
	public static void main(String[] args) {
		SpringApplication.run(new Object[]{ScarletRestApplication.class}, args);
	}
}
