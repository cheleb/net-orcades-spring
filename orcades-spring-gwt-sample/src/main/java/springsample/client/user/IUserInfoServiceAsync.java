package springsample.client.user;


import springsample.client.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserInfoServiceAsync {

	public void showUserInfo(AsyncCallback<UserInfoDTO> callback);

	
	public void deleteMessage(Message message, AsyncCallback<Boolean> callback);
	
	public void newMessage(String message, AsyncCallback<Message> callback);
}
