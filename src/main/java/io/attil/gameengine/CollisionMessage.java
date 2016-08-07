package io.attil.gameengine;

import io.attil.intermediumcore.Message;

public class CollisionMessage extends Message {

	private GameObject recipient;
	
	public CollisionMessage(GameObject sender, GameObject recipient) {
		super(sender);
		this.recipient = recipient;
	}

	@Override
	public void dispatchMessage() {
		recipient.onCollision(this);
	}

}
