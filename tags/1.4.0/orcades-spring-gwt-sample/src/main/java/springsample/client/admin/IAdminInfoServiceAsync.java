package springsample.client.admin;

import springsample.client.user.UserInfoDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IAdminInfoServiceAsync {
	
	public void showUserInfo(String username, AsyncCallback<UserInfoDTO> callback);
}
