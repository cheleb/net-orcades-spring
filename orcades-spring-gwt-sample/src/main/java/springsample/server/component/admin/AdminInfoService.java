package springsample.server.component.admin;

import org.springframework.stereotype.Component;

import springsample.client.admin.IAdminInfoService;
import springsample.client.user.UserInfoDTO;

@Component
public class AdminInfoService implements IAdminInfoService {

	public UserInfoDTO showUserInfo(String username) {
		
		return new UserInfoDTO(username);
	}

}
