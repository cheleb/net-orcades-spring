package springsample.client.user;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Login.
	 */
	private String login;

	@Deprecated
	public UserInfoDTO() {
	
	}
	
	public UserInfoDTO(String p_login) {
		this.login = p_login;
	}

	@Override
	public String toString() {

		return login;
	}

}
