package net.orcades.spring.gwt.security.client;

public interface IGWTSecurityExceptionVisitor {

	void visit(GWTSecurityException authorizationRequiredException);

	void visit(GWTAccessDeniedException accessDeniedException);

}
