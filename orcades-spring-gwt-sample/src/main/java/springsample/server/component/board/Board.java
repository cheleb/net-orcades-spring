package springsample.server.component.board;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import springsample.client.Message;
import springsample.client.User;



@Component
public class Board {

	private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

	
	@PostConstruct
	void init() {
		
		queue.add(new Message("1", "Helo", new User("guest"), "Moderated"));
		queue.add(new Message("2", "Easy", new User("visitor"), "New"));
	}
	
	public boolean add(Message o) {
		return queue.add(o);
	}


	public Message peek() {
		return queue.peek();
	}

	public Message poll() {
		return queue.poll();
	}

	public boolean remove(Message o) {
		return queue.remove(o);
	}

	public int size() {
		return queue.size();
	}

	public List<Message> getMessages() {
		List<Message> list = new ArrayList<Message>();
		
		for (Message message : queue) {
			list.add(message);
		}
		
		return list;
	}
	
	
	
}
