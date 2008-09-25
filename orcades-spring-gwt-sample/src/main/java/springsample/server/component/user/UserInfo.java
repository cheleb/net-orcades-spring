package springsample.server.component.user;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String login;

	@Autowired
	public UserInfo(HttpServletRequest httpServletRequest) {
		login = httpServletRequest.getUserPrincipal().getName();
	}

	public void init() {

	}

	public String getLogin() {
		return login;
	}
}
