package io.attil.gameengine;

import io.attil.intermediumcore.Message;

public class MoveMessage extends Message {

	private GameObject recipient;
	private GameObject sender;
	private int deltaX;
	private int deltaY;
	
	public MoveMessage(GameObject sender, GameObject recipient, int deltaX, int deltaY) {
		super(sender);
		this.recipient = recipient;
		this.sender = sender;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	@Override
	public void dispatchMessage() {
		recipient.onMove(sender, deltaX, deltaY);
	}

}
