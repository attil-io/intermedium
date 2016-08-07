package io.attil.gameengine;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

}
