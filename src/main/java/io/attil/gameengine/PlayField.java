package io.attil.gameengine;

import io.attil.intermediumcore.Mediator;

public class PlayField extends GameObject {

	public PlayField(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void onCollision(CollisionMessage message) {
		
	}

}
