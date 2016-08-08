package io.attil.gameengine;

import java.util.LinkedList;
import java.util.List;

import io.attil.intermediumcore.Colleague;
import io.attil.intermediumcore.Mediator;
import io.attil.intermediumcore.Message;

public class GameMediator implements Mediator {

	private List<Colleague> colleagues = new LinkedList<>();

	private List<Colleague> toRemove = new LinkedList<>();
	private List<Colleague> toAdd = new LinkedList<>();
	
	private int iterationLevel = 0;
	
	public int countColleagues() {
		return colleagues.size();
	}

	public void addColleague(Colleague colleague) {
		if (null == colleague) {
			throw new IllegalArgumentException("colleague is null");
		}
		if (isRegistered(colleague)) {
			throw new IllegalStateException("colleague already present in this mediator");
		}
		if (!isIterating()) {
			colleagues.add(colleague);
		}
		else {
			toAdd.add(colleague);
		}
	}

	public void removeColleague(Colleague colleague) {
		if (null == colleague) {
			throw new IllegalArgumentException("colleague is null");
		}
		boolean wasRemoved = false;
		if (!isIterating()) {
			int idx = 0;
			for (Colleague c : colleagues) {
				if (c == colleague) {
					colleagues.remove(idx);
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
		startMessageIteration();
		for (Colleague colleague : colleagues) {
			if (colleague != sender) {
				colleague.onMessage(message);
			}
		}
		stopMessageIteration();
		doRemovals();
		doAdditions();
	}

	private void doRemovals() {
		if (!isIterating()) {
			for (Colleague c : toRemove) {
				removeColleague(c);
			}
			toRemove.clear();
		}
	}
	
	private void doAdditions() {
		if (!isIterating()) {
			for (Colleague c : toAdd) {
				addColleague(c);
			}
			toAdd.clear();
		}
	}

	private void startMessageIteration() {
		++iterationLevel;
	}
	
	private void stopMessageIteration() {
		--iterationLevel;
	}

	private boolean isIterating() {
		return (0 != iterationLevel);
	}
	
	
	private boolean isRegistered(Colleague colleague) {
		for (Colleague c : colleagues) {
			if (c == colleague) {
				return true;
			}
		}
		return false;
	}

}
