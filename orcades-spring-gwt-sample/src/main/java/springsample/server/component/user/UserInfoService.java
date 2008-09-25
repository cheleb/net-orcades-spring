package springsample.server.component.user;

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
	
	public UserInfoDTO showUserInfo() {

		
		
		return new UserInfoDTO(userInfo.getLogin());
	}

}
