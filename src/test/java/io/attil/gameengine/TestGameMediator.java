package io.attil.gameengine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.attil.gameengine.GameMediator;
import io.attil.intermediumcore.BaseMessage;
import io.attil.intermediumcore.Colleague;


@RunWith(MockitoJUnitRunner.class)
public class TestGameMediator {

	private GameMediator mediator;
	
	@Mock
	private Colleague gameObject;

	@Mock
	private Colleague recipient;

	@Mock
	private BaseMessage message;
	
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
	
	@Test
	public void testAddSameNotAllowed() {
		mediator.addObject(gameObject);
		try {
			mediator.addObject(gameObject);
			fail("Exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testAddNullNotAllowed() {
		try {
			mediator.addObject(null);
			fail("Exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testMessageReceivedByOtherObject() {
		mediator.addObject(recipient);
		mediator.addObject(gameObject);
		mediator.sendMessage(gameObject, message);
		verify(recipient, times(1)).onMessage(eq(message));
	}
	
	@Test
	public void testSenderNotRegistered() {
		try {
			mediator.sendMessage(gameObject, message);
			fail("Exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testMessageNotReceivedBySender() {
		mediator.addObject(gameObject);
		mediator.sendMessage(gameObject, message);
		verify(gameObject, times(0)).onMessage(any(BaseMessage.class));
	}
	
	@Test
	public void testSenderNull() {
		try {
			mediator.sendMessage(null, message);
			fail("Exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testRecipientNull() {
		try {
			mediator.sendMessage(gameObject, null);
			fail("Exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
}
