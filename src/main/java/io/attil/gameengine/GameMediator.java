package io.attil.gameengine;

import java.util.LinkedList;
import java.util.List;

import io.attil.intermediumcore.Colleague;
import io.attil.intermediumcore.Mediator;
import io.attil.intermediumcore.Message;

public class GameMediator implements Mediator {

	private List<Colleague> gameObjects = new LinkedList<>();
	private int iterationLevel = 0;
	private List<Colleague> toRemove = new LinkedList<>();
	
	public int countObjects() {
		return gameObjects.size();
	}

	public void addObject(Colleague gameObject) {
		if (null == gameObject) {
			throw new IllegalArgumentException("gameObject is null");
		}
		for (Colleague go : gameObjects) {
			if (go == gameObject) {
				throw new IllegalStateException("gameobject already present in this mediator");
			}
		}
		gameObjects.add(gameObject);
	}

	@Override
	public void sendMessage(Colleague sender, Message message) {
		if (null == sender) {
			throw new IllegalArgumentException("sender should not be null");
		}
		else if (null == message) {
			throw new IllegalArgumentException("message should not be null");
		}
		else if (!isRegistered(sender)) {
			throw new IllegalStateException("sender is not registered");
		}
		++iterationLevel;
		for (Colleague go : gameObjects) {
			if (go != sender) {
				go.onMessage(message);
			}
		}
		--iterationLevel;
		doRemovals();
	}

	private void doRemovals() {
		if (0 == iterationLevel) {
			for (Colleague c : toRemove) {
				remove(c);
			}
			toRemove.clear();
		}
	}
	
	private boolean isRegistered(Colleague gameObject) {
		for (Colleague go : gameObjects) {
			if (go == gameObject) {
				return true;
			}
		}
		return false;
	}

	public void remove(Colleague colleague) {
		boolean wasRemoved = false;
		final boolean isIterating = (0 != iterationLevel);
		if (!isIterating) {
			int idx = 0;
			for (Colleague go : gameObjects) {
				if (go == colleague) {
					gameObjects.remove(idx);
					wasRemoved = true;
					break;
				}
				++idx;
			}
		}
		else if (isRegistered(colleague)) {
			toRemove.add(colleague);
			wasRemoved = true;
		}
		
		if (!wasRemoved) {
			throw new IllegalStateException("object was not found");
		}
	}
}
