package io.attil.gameengine;

import io.attil.intermediumcore.BaseMessage;
import io.attil.intermediumcore.Colleague;
import io.attil.intermediumcore.Mediator;

public abstract class GameObject implements Colleague {

	protected Mediator mediator;
	private int posX = 0;
	private int posY = 0;
	private int speedX = 0;
	private int speedY = 0;
	
	
	public GameObject(Mediator mediator) {
		this.mediator = mediator;
	}
	
	@Override
	public abstract void onMessage(BaseMessage message);

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

}
