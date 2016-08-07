package io.attil.intermediumcore;

public class Message {
	
	private int type;
	private Colleague sender;
	
	public Message(int type, Colleague sender) {
		this.type = type;
		this.sender = sender;
	}
	
	public int getType() {
		return type;
	}
	
	public Colleague getSender() {
		return sender;
	}
}
