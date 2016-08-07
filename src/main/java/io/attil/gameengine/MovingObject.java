package io.attil.gameengine;

import io.attil.intermediumcore.Mediator;

public class MovingObject extends GameObject {

	private boolean wasCollision;
	
	public MovingObject(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void onCollision(CollisionMessage message) {
		wasCollision = true;
	}

	@Override
	public void onMove(GameObject sender, int deltaX, int deltaY) {
		if (sender.getPosX() + deltaX == getPosX() && (sender.getPosY() + deltaY == getPosY())) {
			mediator.sendMessage(this, new CollisionMessage(this, sender));
		}
	}

	@Override
	public void doAStep() {
		wasCollision = false;
		mediator.sendMessage(this, new MoveMessage(this, null, getSpeedX(), getSpeedY()));
		if (!wasCollision) {
			setPosX(getPosX() + getSpeedX());
			setPosY(getPosY() + getSpeedY());
		}
	}

}
