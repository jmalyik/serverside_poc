package hu.scarlet.rest.security.token;

public interface TokenExtractor {

	String extract(String payload);
}
