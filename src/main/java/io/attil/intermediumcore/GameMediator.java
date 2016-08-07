package io.attil.intermediumcore;

import java.util.LinkedList;
import java.util.List;

public class GameMediator {

	private List<GameObject> gameObjects = new LinkedList<>();
	
	public int countObjects() {
		return gameObjects.size();
	}

	public void addObject(GameObject gameObject) {
		if (null == gameObject) {
			throw new IllegalArgumentException("gameObject is null");
		}
		for (GameObject go : gameObjects) {
			if (go == gameObject) {
				throw new IllegalStateException("gameobject already present in this mediator");
			}
		}
		gameObjects.add(gameObject);
	}

	public void sendMessage(GameObject sender, GameMessage message) {
		if (null == sender) {
			throw new IllegalArgumentException("sender should not be null");
		}
		else if (null == message) {
			throw new IllegalArgumentException("message should not be null");
		}
		else if (!isRegistered(sender)) {
			throw new IllegalStateException("sender is not registered");
		}
		for (GameObject go : gameObjects) {
			if (go != sender) {
				go.onMessage(message);
			}
		}
	}

	private boolean isRegistered(GameObject gameObject) {
		for (GameObject go : gameObjects) {
			if (go == gameObject) {
				return true;
			}
		}
		return false;
	}
}
