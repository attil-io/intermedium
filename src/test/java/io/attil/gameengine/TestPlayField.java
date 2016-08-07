package io.attil.gameengine;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.attil.intermediumcore.BaseMessage;
import io.attil.intermediumcore.Mediator;

@RunWith(MockitoJUnitRunner.class)
public class TestPlayField {

	private PlayField playField;
	
	@Mock
	private Mediator mediator;
	
	@Mock
	private BaseMessage message;
	
	@Before
	public void setUp() {
		playField = new PlayField(mediator);
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
	public void testMessageSpeedUnchanged() {
		playField.onMessage(message);
		assertEquals(0, playField.getSpeedX());
		assertEquals(0, playField.getSpeedY());
	}

	@Test
	public void testMessagePositionUnchanged() {
		playField.onMessage(message);
		assertEquals(0, playField.getPosX());
		assertEquals(0, playField.getPosY());
	}

}
