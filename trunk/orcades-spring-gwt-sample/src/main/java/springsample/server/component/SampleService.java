package springsample.server.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import springsample.client.ISampleService;
import springsample.client.Message;
import springsample.client.SampleCheckedException;
import springsample.server.component.board.Board;

@Component
public class SampleService implements ISampleService {

	
	@Autowired
	private Board board;
	
	public String sayHelo(String who) {

		return "Helo " + who;
	}

	public List<Message> getMessages(){
		return board.getMessages();
	}

	public void buggy() throws SampleCheckedException{
		throw new SampleCheckedException("Something rotten !");
	}
	
	
	
}
