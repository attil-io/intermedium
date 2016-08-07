package io.attil.intermediumcore;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TestGameMediator {

	private GameMediator mediator;
	
	@Mock
	private GameObject gameObject;
	
	@Before
	public void setUp() {
		mediator = new GameMediator();
	}
	
	@Test
	public void testMediatorEmptyAfterCreation() {
		assertEquals(0, mediator.countObjects());
	}
	

	@Test
	public void testMediatorContainsElementAfterAdding() {
		mediator.addObject(gameObject);
		assertEquals(1, mediator.countObjects());
	}
}
