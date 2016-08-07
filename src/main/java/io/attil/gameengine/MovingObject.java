package io.attil.gameengine;

import io.attil.intermediumcore.Mediator;

public class MovingObject extends GameObject {

	public MovingObject(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void onCollision(CollisionMessage message) {

	}

	@Override
	public void onMove(GameObject sender, int deltaX, int deltaY) {
		if (sender.getPosX() + deltaX == getPosX() && (sender.getPosY() + deltaY == getPosY())) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
	}

}
