package hu.scarlet.rest.security.verifier;

public interface TokenVerifier {
	boolean verify(String jti);
}
