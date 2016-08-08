package io.attil.gameengine;

import java.util.LinkedList;
import java.util.List;

import io.attil.intermediumcore.Colleague;
import io.attil.intermediumcore.Mediator;
import io.attil.intermediumcore.Message;

public class GameMediator implements Mediator {

	private List<Colleague> colleagues = new LinkedList<>();
	private int iterationLevel = 0;
	private List<Colleague> toRemove = new LinkedList<>();
	private List<Colleague> toAdd = new LinkedList<>();
	
	public int countObjects() {
		return colleagues.size();
	}

	public void addObject(Colleague colleague) {
		if (null == colleague) {
			throw new IllegalArgumentException("colleague is null");
		}
		if (isRegistered(colleague)) {
			throw new IllegalStateException("colleague already present in this mediator");
		}
		final boolean isIterating = (0 != iterationLevel);
		if (!isIterating) {
			colleagues.add(colleague);
		}
		else {
			toAdd.add(colleague);
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
		++iterationLevel;
		for (Colleague colleague : colleagues) {
			if (colleague != sender) {
				colleague.onMessage(message);
			}
		}
		--iterationLevel;
		doRemovals();
		doAdditions();
	}

	private void doRemovals() {
		if (0 == iterationLevel) {
			for (Colleague c : toRemove) {
				remove(c);
			}
			toRemove.clear();
		}
	}
	
	private void doAdditions() {
		if (0 == iterationLevel) {
			for (Colleague c : toAdd) {
				addObject(c);
			}
		}
	}

	private boolean isRegistered(Colleague colleague) {
		for (Colleague c : colleagues) {
			if (c == colleague) {
				return true;
			}
		}
		return false;
	}

	public void remove(Colleague colleague) {
		if (null == colleague) {
			throw new IllegalArgumentException("colleague is null");
		}
		boolean wasRemoved = false;
		final boolean isIterating = (0 != iterationLevel);
		if (!isIterating) {
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
}
