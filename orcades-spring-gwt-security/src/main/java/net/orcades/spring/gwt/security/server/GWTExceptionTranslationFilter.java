package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.ExceptionTranslationFilter;

public class GWTExceptionTranslationFilter extends ExceptionTranslationFilter {

	
	
	@Override
	protected void sendStartAuthentication(ServletRequest request,
			ServletResponse response, FilterChain chain,
			AuthenticationException reason) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		super.sendStartAuthentication(request, response, chain, reason);
	}
	
	
	
}
