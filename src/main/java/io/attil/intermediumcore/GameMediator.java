package io.attil.intermediumcore;

import java.util.LinkedList;
import java.util.List;

public class GameMediator {

	private List<GameObject> gameObjects = new LinkedList<>();
	
	public int countObjects() {
		return gameObjects.size();
	}

	public void addObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

}
