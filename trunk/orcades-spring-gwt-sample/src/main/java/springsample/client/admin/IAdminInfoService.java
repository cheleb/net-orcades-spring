package springsample.client.admin;

import net.orcades.spring.gwt.security.client.GWTSecurityException;
import springsample.client.user.UserInfoDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("admin/showInfo.gwt")
public interface IAdminInfoService extends RemoteService {
	
	public static class Util {

		public static IAdminInfoServiceAsync getInstance() {

			return GWT.create(IAdminInfoService.class);
		}
	}
	
	public UserInfoDTO showUserInfo(String username) throws GWTSecurityException;
}
