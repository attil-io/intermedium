package io.attil.intermediumcore;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestGameMediator {

	private GameMediator mediator;
	
	@Before
	public void setUp() {
		mediator = new GameMediator();
	}
	
	@Test
	public void testMediatorEmptyAfterCreation() {
		assertEquals(0, mediator.countObjects());
	}
	
}
