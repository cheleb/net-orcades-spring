package springsample.server.component.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import springsample.client.Message;
import springsample.client.User;
import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;
import springsample.server.component.board.Board;

@Component
@Scope("request")
public class UserInfoService implements IUserInfoService {

	
	@Autowired
	UserInfo userInfo;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	@Autowired
	private Board board;
	
	public UserInfoDTO showUserInfo() {
		return new UserInfoDTO(httpServletRequest.getUserPrincipal().getName());
	}

	public Boolean deleteMessage(Message message) {
		
		return board.remove(message);
	}

	public Message newMessage(String text) {
		Message message= new Message("di", text, new User(httpServletRequest.getUserPrincipal().getName()), "new");
		board.add(message);
		return message;
	}

}
