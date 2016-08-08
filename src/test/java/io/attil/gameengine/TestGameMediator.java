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
	private Colleague recipient4;

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
		assertEquals(0, mediator.countColleagues());
	}
	

	@Test
	public void testMediatorContainsElementAfterAdding() {
		mediator.addColleague(gameObject);
		assertEquals(1, mediator.countColleagues());
	}
	
	@Test
	public void testAddSameNotAllowed() {
		mediator.addColleague(gameObject);
		try {
			mediator.addColleague(gameObject);
			fail("Exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testAddNullNotAllowed() {
		try {
			mediator.addColleague(null);
			fail("Exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testAddDuringIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.addColleague(recipient);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient2);
		mediator.addColleague(recipient3);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		verify(recipient3, times(1)).onMessage(eq(message));
		verify(recipient, times(0)).onMessage(any(Message.class));
		assertEquals(4, mediator.countColleagues());
	}

	@Test
	public void testAddAfterNestedIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.addColleague(recipient);
				mediator.sendMessage(recipient3, message2);
				return null;
			}
		}).when(recipient3).onMessage(eq(message));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.addColleague(recipient2);
				return null;
			}
		}).when(recipient4).onMessage(eq(message));
		
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient3);
		mediator.addColleague(recipient4);
		mediator.sendMessage(gameObject, message);
		verify(recipient3, times(1)).onMessage(eq(message));
		verify(recipient4, times(1)).onMessage(eq(message));
		verify(recipient, times(0)).onMessage(any(Message.class));
		verify(recipient2, times(0)).onMessage(any(Message.class));
		assertEquals(5, mediator.countColleagues());
	}

	@Test
	public void testAddAfterTwoIterations() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.addColleague(recipient);
				return null;
			}
		}).when(recipient3).onMessage(eq(message));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.addColleague(recipient2);
				return null;
			}
		}).when(recipient4).onMessage(eq(message));
		
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient3);
		mediator.sendMessage(gameObject, message);

		mediator.removeColleague(recipient);
		mediator.removeColleague(recipient3);
		
		mediator.addColleague(recipient4);
		mediator.sendMessage(gameObject, message);

		
		verify(recipient, times(0)).onMessage(any(Message.class));
		verify(recipient2, times(0)).onMessage(any(Message.class));
		verify(recipient3, times(1)).onMessage(eq(message));
		verify(recipient4, times(1)).onMessage(eq(message));
		assertEquals(3, mediator.countColleagues());
	}
	
	@Test
	public void testMessageReceivedByOtherObject() {
		mediator.addColleague(recipient);
		mediator.addColleague(gameObject);
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
		mediator.addColleague(gameObject);
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
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient);
		mediator.addColleague(recipient2);
		mediator.removeColleague(recipient);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countColleagues());
	}

	@Test
	public void testRemoveNonExistingObject() {
		try {
			mediator.removeColleague(recipient);
			fail("exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testRemoveNullNotAllowed() {
		try {
			mediator.removeColleague(null);
			fail("exception expected");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testRemoveObjectDuringIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient);
				return null;
			}
		}).when(recipient).onMessage(eq(message));
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient);
		mediator.addColleague(recipient2);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countColleagues());
	}

	@Test
	public void testRemoveNonexistingObjectDuringIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient2);
		try {
			mediator.sendMessage(gameObject, message);
			fail("exception expected");
		}
		catch (IllegalStateException e) {
			// expected
		}
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countColleagues());
	}


	@Test
	public void testRemoveObjectAfterNestedIteration() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient);
				mediator.sendMessage(recipient, message2);
				return null;
			}
		}).when(recipient).onMessage(eq(message));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient2);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient);
		mediator.addColleague(recipient2);
		mediator.addColleague(recipient3);
		mediator.sendMessage(gameObject, message);
		verify(recipient2, times(1)).onMessage(eq(message));
		assertEquals(2, mediator.countColleagues());
	}

	@Test
	public void testRemoveObjectAfterTwoIterations() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient);
				return null;
			}
		}).when(recipient).onMessage(eq(message));
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mediator.removeColleague(recipient2);
				return null;
			}
		}).when(recipient2).onMessage(eq(message));
		
		mediator.addColleague(gameObject);
		mediator.addColleague(recipient);
		mediator.addColleague(recipient3);
		mediator.sendMessage(gameObject, message);

		mediator.addColleague(recipient2);
		mediator.sendMessage(gameObject, message);

		
		verify(recipient, times(1)).onMessage(eq(message));
		verify(recipient2, times(1)).onMessage(eq(message));
		verify(recipient3, times(2)).onMessage(eq(message));
		assertEquals(2, mediator.countColleagues());
	}

}
