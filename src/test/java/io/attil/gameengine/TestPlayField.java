package io.attil.gameengine;

import static org.junit.Assert.assertEquals;
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

import io.attil.intermediumcore.Colleague;
import io.attil.intermediumcore.Mediator;

@RunWith(MockitoJUnitRunner.class)
public class TestPlayField {

	private PlayField playField;
	
	@Mock
	private Mediator mediator;
	
	@Mock
	private CollisionMessage collisionMessage;

	@Mock
	private GameObject gameObject;
	
	@Before
	public void setUp() {
		playField = new PlayField(mediator, 8, 5);
	}
	
	@Test
	public void testSpeedZero() {
		assertEquals(0, playField.getSpeedX());
		assertEquals(0, playField.getSpeedY());
	}

	@Test
	public void testPositionZero() {
		assertEquals(0, playField.getPosX());
		assertEquals(0, playField.getPosY());
	}

	@Test
	public void testCollisionSpeedUnchanged() {
		playField.onCollision(collisionMessage);
		assertEquals(0, playField.getSpeedX());
		assertEquals(0, playField.getSpeedY());
	}

	@Test
	public void testMessagePositionUnchanged() {
		playField.onCollision(collisionMessage);
		assertEquals(0, playField.getPosX());
		assertEquals(0, playField.getPosY());
	}

	@Test
	public void testNoCollideIfInside() throws Exception {
		when(gameObject.getPosX()).thenReturn(2);
		when(gameObject.getPosY()).thenReturn(2);
		playField.onMove(gameObject, -1, 0);
		verify(mediator, times(0)).sendMessage(any(Colleague.class), any(CollisionMessage.class));
	}
	
	@Test
	public void testCollideIfMinXReached() throws Exception {
		when(gameObject.getPosX()).thenReturn(0);
		when(gameObject.getPosY()).thenReturn(0);
		playField.onMove(gameObject, -1, 0);
		verify(mediator, times(1)).sendMessage(eq(playField), any(CollisionMessage.class));
	}

	@Test
	public void testCollideIfMinYReached() throws Exception {
		when(gameObject.getPosX()).thenReturn(0);
		when(gameObject.getPosY()).thenReturn(0);
		playField.onMove(gameObject, 0, -1);
		verify(mediator, times(1)).sendMessage(eq(playField), any(CollisionMessage.class));
	}

	@Test
	public void testCollideIfMaxXReached() throws Exception {
		when(gameObject.getPosX()).thenReturn(9);
		when(gameObject.getPosY()).thenReturn(0);
		playField.onMove(gameObject, 1, 0);
		verify(mediator, times(1)).sendMessage(eq(playField), any(CollisionMessage.class));
	}

	@Test
	public void testCollideIfMaxYReached() throws Exception {
		when(gameObject.getPosX()).thenReturn(0);
		when(gameObject.getPosY()).thenReturn(4);
		playField.onMove(gameObject, 0, 1);
		verify(mediator, times(1)).sendMessage(eq(playField), any(CollisionMessage.class));
	}
}
