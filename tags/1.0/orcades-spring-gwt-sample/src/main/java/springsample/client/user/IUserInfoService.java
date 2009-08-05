package springsample.client.user;


import net.orcades.spring.gwt.security.client.GWTSecurityException;
import springsample.client.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user/showInfo.gwt")
public interface IUserInfoService extends RemoteService {

	public static class Util {

		public static IUserInfoServiceAsync getInstance() {

			return GWT.create(IUserInfoService.class);
		}
	}
	
	public UserInfoDTO showUserInfo() throws GWTSecurityException;

	
	public Boolean deleteMessage(Message message);
	
	public Message newMessage(String message);
}
