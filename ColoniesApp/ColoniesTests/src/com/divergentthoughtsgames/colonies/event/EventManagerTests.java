package com.divergentthoughtsgames.colonies.event;

import java.util.ArrayList;

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.sound.SoundManager;

import junit.framework.TestCase;

public class EventManagerTests extends TestCase
{
	EventManager em;
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.em = new EventManager();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		this.em = null;
	}

	public void testUpdate_withoutQueuedEvents()
	{
		this.em.update(1);
	}
	
	public void testUpdate_withQueuedEvents()
	{
		this.em.subscribe(GamePausedEvent.ID, new SoundManager(new GameManager(new ArrayList<String>())));
		this.em.notify(new GamePausedEvent());
		this.em.notify(new GameUnpausedEvent());
		this.em.update(1);
	}
	
	public void testUpdate_invalidGameTicks()
	{
		try {
			this.em.update(-1);
			fail("Exception expected, but none found.");
		} catch (Exception e) {}
	}

	public void testNotifyEvent()
	{
		this.em.notify(new GamePausedEvent());
	}

	public void testSubscribe()
	{
		this.em.subscribe(GamePausedEvent.ID, this.em);
	}
	
	public void testSubscribe_twiceSameObject()
	{
		this.em.subscribe(GamePausedEvent.ID, this.em);
		this.em.subscribe(GamePausedEvent.ID, this.em);
	}
	
	public void testSubscribe_invalidEventType()
	{
		try {
			this.em.subscribe(null, this.em);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testSubscribe_invalidSubscriber()
	{
		try {
			this.em.subscribe(GamePausedEvent.ID, null);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testUnsubscribe()
	{
		this.em.subscribe(GamePausedEvent.ID, this.em);
		this.em.unsubscribe(GamePausedEvent.ID, this.em);
	}
	
	public void testUnsubscribe_invalidEventType()
	{
		try {
			this.em.unsubscribe(null, this.em);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testUnsubscribe_invalidSubscriber()
	{
		try {
			this.em.unsubscribe(GamePausedEvent.ID, null);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testEventFlow()
	{
		GamePausedEvent event = new GamePausedEvent();
		EventTester et = new EventTester(this.em);
		this.em.notify(event);
		this.em.update(1);
		assertTrue(et.getReceivedEvent());
	}
	
	public void testEventFlow_unsubscribe()
	{
		GamePausedEvent event = new GamePausedEvent();
		EventTester et = new EventTester(this.em);
		this.em.update(1);
		this.em.unsubscribe(GamePausedEvent.ID, et);
		this.em.notify(event);
		this.em.update(1);
		assertFalse(et.getReceivedEvent());
	}
	
	
	private static class EventTester implements EventListener
	{
		private boolean receivedEvent;
		
		public boolean getReceivedEvent()
		{
			return this.receivedEvent;
		}
		
		public EventTester(EventManager em)
		{
			em.subscribe(GamePausedEvent.ID, this);
		}
		
		@Override
		public void notify(GameEvent<?> event)
		{
			if (event.getEventId() == GamePausedEvent.ID)
			{
				this.receivedEvent = true;
			}
		}
	}
}
