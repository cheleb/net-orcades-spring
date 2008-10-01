package springsample.client;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	
	private User owner;
	
	
	
	
	
	private String text;

	public Message() {
	}

	
	public Message(String text, User owner, int status) {
		super();
		this.text = text;
		this.owner = owner;
		this.status = status;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getOwner() {
		return owner;
	}
	
	
	
}
