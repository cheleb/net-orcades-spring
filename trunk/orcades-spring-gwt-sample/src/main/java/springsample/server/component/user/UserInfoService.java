package springsample.server.component.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;

@Component
@Scope("request")
public class UserInfoService implements IUserInfoService {

	
	@Autowired
	UserInfo userInfo;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	public UserInfoDTO showUserInfo() {
		return new UserInfoDTO(httpServletRequest.getUserPrincipal().getName());
	}

}
