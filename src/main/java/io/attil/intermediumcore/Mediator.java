package io.attil.intermediumcore;

public interface Mediator {
	public void sendMessage(Colleague sender, BaseMessage message);
}
