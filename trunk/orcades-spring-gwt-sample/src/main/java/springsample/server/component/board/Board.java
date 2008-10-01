package springsample.server.component.board;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;


import org.springframework.stereotype.Component;

import springsample.client.Message;
import springsample.client.User;



@Component
public class Board {

	private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

	
	@PostConstruct
	private void init() {
		queue.add(new Message("Helo", new User("guest"), 0));
		queue.add(new Message("Easy", new User("visitor"), 0));
	}
	
	public boolean add(Message o) {
		return queue.add(o);
	}

	public Iterator<Message> iterator() {
		return queue.iterator();
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
	
	
	
}
