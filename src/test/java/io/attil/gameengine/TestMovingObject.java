package io.attil.gameengine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.attil.intermediumcore.Mediator;

@RunWith(MockitoJUnitRunner.class)
public class TestMovingObject {

	private MovingObject movingObject;
	
	@Mock
	private Mediator mediator;
	
	@Mock
	private GameObject otherGameObject;
	
	@Before
	public void setUp() {
		movingObject = new MovingObject(mediator);
	}
	
	@Test
	public void testCollideXAxis() throws Exception {
		when(otherGameObject.getPosX()).thenReturn(1);
		when(otherGameObject.getPosY()).thenReturn(0);
		movingObject.onMove(otherGameObject, -1, 0);
		verify(mediator, times(1)).sendMessage(eq(movingObject), any(CollisionMessage.class));
	}

	@Test
	public void testCollideYAxis() throws Exception {
		when(otherGameObject.getPosX()).thenReturn(0);
		when(otherGameObject.getPosY()).thenReturn(1);
		movingObject.onMove(otherGameObject, 0, -1);
		verify(mediator, times(1)).sendMessage(eq(movingObject), any(CollisionMessage.class));
	}

	@Test
	public void testNoCollide() throws Exception {
		when(otherGameObject.getPosX()).thenReturn(10);
		movingObject.onMove(otherGameObject, -1, 0);
		verify(mediator, times(0)).sendMessage(any(GameObject.class), any(CollisionMessage.class));
	}

	@Test
	public void testStepWithoutCollision() throws Exception {
		movingObject.setSpeedX(1);
		movingObject.setSpeedY(2);
		movingObject.doAStep();
		verify(mediator, times(1)).sendMessage(eq(movingObject), any(MoveMessage.class));
		assertEquals(1, movingObject.getPosX());
		assertEquals(2, movingObject.getPosY());
	}

	
	@Test
	public void testStepWithCollision() throws Exception {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				movingObject.onCollision(new CollisionMessage(otherGameObject, movingObject));
				return null;
			}
		}).when(mediator).sendMessage(eq(movingObject), any(MoveMessage.class));
		movingObject.setSpeedX(1);
		movingObject.setSpeedY(2);
		movingObject.doAStep();
		assertEquals(0, movingObject.getPosX());
		assertEquals(0, movingObject.getPosY());
	}
}
