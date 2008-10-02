package springsample.client;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String login;


	public User() {
	}
	
	public User(String string) {
		this.login = string;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getLogin() {
		return login;
	}

	
	@Override
	public String toString() {
		return getLogin();
	}
	
}
