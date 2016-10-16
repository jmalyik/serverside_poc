package hu.scarlet.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 
 * @author jmalyik
 *
 */
@Configuration
public class RestMessages {
	@Bean(name = "rest")
	public ReloadableResourceBundleMessageSource getBundle() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:locale/scarlet-rest");
		bundle.setCacheSeconds(5);
		bundle.setDefaultEncoding("UTF-8");
		return bundle;
	}

	public Object[] asOArr(Object... objects) {
		return objects;
	}
}
