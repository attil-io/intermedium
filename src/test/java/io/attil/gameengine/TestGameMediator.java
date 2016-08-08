package io.attil.gameengine;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.attil.gameengine.GameMediator;
import io.attil.intermediumcore.Message;
import io.attil.intermediumcore.Colleague;


@RunWith(MockitoJUnitRunner.class)
public class TestGameMediator {

	private GameMediator mediator;
	
	@Mock
	private Colleague gameObject;

	@Mock
	private Colleague recipient;

	@Mock
	private Colleague recipient2;
	
	@Mock
	private Colleague recipient3;
	
	@Mock
	private Message message;

	@Mock
	private Message message2;
	
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
		verify(gameObject, times(0)).onMessage(any(Message.class));
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
	public void testMessageNull() {
		try {
			mediator.sendMessage(gameObject, null);
			fail("Exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
	

	@Test
	public void testRemoveObject() {
		mediator.addObject(gameObject);
		mediator.addObject(recipient);
		mediator.addObject(recipient2);
		mediator.remove(recipient);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countObjects());
	}

	@Test
	public void testRemoveNonExistingObject() {
		try {
			mediator.remove(recipient);
			fail("exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
	}
	
	
	@Test
	public void testRemoveObjectDuringIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.remove(recipient);
				return null;
			}
		}).when(recipient).onMessage(eq(message));
		mediator.addObject(gameObject);
		mediator.addObject(recipient);
		mediator.addObject(recipient2);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countObjects());
	}

	@Test
	public void testRemoveNonexistingObjectDuringIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.remove(recipient);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		mediator.addObject(gameObject);
		mediator.addObject(recipient2);
		try {
			mediator.sendMessage(gameObject, message);
			fail("exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countObjects());
	}


	@Test
	public void testRemoveObjectAfterNestedIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.remove(recipient);
				mediator.sendMessage(recipient, message2);
				return null;
			}
		}).when(recipient).onMessage(eq(message));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.remove(recipient2);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		
		mediator.addObject(gameObject);
		mediator.addObject(recipient);
		mediator.addObject(recipient2);
		mediator.addObject(recipient3);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countObjects());
	}

}
