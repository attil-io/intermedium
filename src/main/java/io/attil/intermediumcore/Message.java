package io.attil.intermediumcore;

public abstract class Message {
	
	private Colleague sender;
	
	public Message(Colleague sender) {
		this.sender = sender;
	}
	
	public Colleague getSender() {
		return sender;
	}
	
	public abstract void dispatchMessage();
}
