package springsample.client.user;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserInfoServiceAsync {

	public void showUserInfo(AsyncCallback<UserInfoDTO> callback);

}
