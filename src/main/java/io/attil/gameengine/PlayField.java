package io.attil.gameengine;

import io.attil.intermediumcore.Mediator;

public class PlayField extends GameObject {

	private int maxX;
	private int maxY;
	
	public PlayField(Mediator mediator, int maxX, int maxY) {
		super(mediator);
		this.maxX = maxX;
		this.maxY = maxY;
	}

	@Override
	public void onCollision(CollisionMessage message) {
		
	}

	@Override
	public void onMove(GameObject sender, int deltaX, int deltaY) {
		if (sender.getPosX() + deltaX < 0) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
		if (sender.getPosY() + deltaY < 0) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
		if (sender.getPosX() + deltaX >= maxX) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
		if (sender.getPosY() + deltaY >= maxY) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
	}

}
