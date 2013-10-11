package com.divergentthoughtsgames.colonies.event;

/*
Copyright 2013 Christopher D. Canfield


This file is part of Colonies.

Colonies is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Colonies is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Colonies.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.*;
import java.util.*;

import com.divergentthoughtsgames.colonies.Updatable;

import android.util.Log;

/**
 * Manager used to forward events to subscribed EventListeners.
 * @author Christopher D Canfield
 */
public class EventManager implements Updatable, EventListener, Serializable
{
	private static final long serialVersionUID = 2159493506231944594L;

	// A map of EventTypes with EventListeners (the subscribers to the event).
	// key: EventType; value: ArrayList of EventListener objects.
	private transient Map<Long, List<EventListener>> nonSerializableSubscribers;
	
	private final Map<Long, List<EventListener>> subscribers = 
			new HashMap<Long, List<EventListener>>();

	// A queue of events that are awaiting processing.
	private final Queue<GameEvent<?>> eventQueue = new ConcurrentLinkedQueue<GameEvent<?>>();
	
	public EventManager() 
	{
		this.nonSerializableSubscribers = new HashMap<Long, List<EventListener>>();
	}
	
	/**
	 * Loops through queued events. 
	 * Should be called once per game tick.
	 */
	@Override
	public void update(long gameTicks)
	{
		if (gameTicks < 0) throw new IllegalArgumentException("Argument 'gameTicks' cannot be negative.");
		
		final int MAX_LOOPS_PER_TICK = 5;
		for (int i = 0; i < MAX_LOOPS_PER_TICK; ++i)
		{
			final GameEvent<?> event = this.eventQueue.poll();
			if (event != null)
			{
				Log.d("EventManager", "found event: " + event.toString());
				synchronized(this.subscribers)
				{
					List<EventListener> eventSubscribers = this.subscribers.get(event.getEventId());
					if (eventSubscribers != null)
					{
						for (final EventListener subscriber : eventSubscribers)
						{
							Log.d("EventManager", "Notifying subscriber " + subscriber.getClass().getName());
							subscriber.notify(event);
						}
					}
				}
				
				synchronized(this.nonSerializableSubscribers)
				{
					List<EventListener> eventSubscribers = this.nonSerializableSubscribers.get(event.getEventId());
					if (eventSubscribers != null)
					{
						for (final EventListener subscriber : eventSubscribers)
						{
							Log.d("EventManager", "Notifying unserializable subscriber " + subscriber.getClass().getName());
							subscriber.notify(event);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Notifies the EventManager that an event has occurred. The event
	 * will be added to the event queue, and eventually propagated to all
	 * subscribers.
	 * @param event The Event that occurred.
	 * @throws IllegalArgumentException if event is null.
	 */
	@Override
	public void notify(GameEvent<?> event)
	{
		if (event == null) throw new IllegalArgumentException("Argument 'event' cannot be null.");
		Log.d("EventManager", "Event received: " + event.toString());
		this.eventQueue.add(event);
	}
	
	/**
	 * Subscribes the EventListener to the specified EventType. The EventListener's 
	 * notify(Event) method will be called when events of the specified type are received
	 * by the EventManager.
	 * @param eventType The type of Event that the EventListener will be notified about. 
	 * @param subscriber The EventListener that is subscribing to the event.
	 * @throws IllegalArgumentException if subscriber is null.
	 */
	public void subscribe(Long eventType, EventListener subscriber)
	{
		if (eventType == null) throw new IllegalArgumentException("Argument 'eventType' cannot be null.");
		if (subscriber == null) throw new IllegalArgumentException("Argument 'subscriber' cannot be null.");
		
		if (subscriber instanceof Serializable)
		{
			synchronized(this.subscribers)
			{
				List<EventListener> eventSubscribers = this.subscribers.get(eventType);
				if (eventSubscribers == null)
				{
					Log.d("EventManager", subscriber.getClass().getName() + " subscribing to " + eventType.toString());
					eventSubscribers = new ArrayList<EventListener>();
				}
				eventSubscribers.add(subscriber);
				this.subscribers.put(eventType, eventSubscribers);
			}
		}
		else
		{
			synchronized(this.nonSerializableSubscribers)
			{
				List<EventListener> eventSubscribers = this.nonSerializableSubscribers.get(eventType);
				if (eventSubscribers == null)
				{
					Log.d("EventManager", subscriber.getClass().getName() + " subscribing to " + eventType.toString());
					eventSubscribers = new ArrayList<EventListener>();
				}
				eventSubscribers.add(subscriber);
				this.nonSerializableSubscribers.put(eventType, eventSubscribers);
			}
		}
	}
		

	/**
	 * Removes an EventListener from being notified about the specified Event.
	 * @param eventType The type of Event that the EventListener will no longer 
	 * be notified about.
	 * @param subscriber The EventListener that will no longer be notified.
	 * @throws IllegalArgumentException if subscriber is null.
	 */
	public void unsubscribe(Long eventType, EventListener subscriber)
	{
		if (eventType == null) throw new IllegalArgumentException("Argument 'eventType' cannot be null.");
		if (subscriber == null) throw new IllegalArgumentException("Argument 'subscriber' cannot be null.");
		
		synchronized(this.subscribers)
		{
			List<EventListener> eventSubscribers = this.subscribers.get(eventType);
			if (eventSubscribers != null)
			{
				eventSubscribers.remove(subscriber);
				this.subscribers.put(eventType, eventSubscribers);
			}
		}
		
		synchronized(this.nonSerializableSubscribers)
		{
			List<EventListener> eventSubscribers = this.nonSerializableSubscribers.get(eventType);
			if (eventSubscribers != null)
			{
				eventSubscribers.remove(subscriber);
				this.nonSerializableSubscribers.put(eventType, eventSubscribers);
			}
		}
	}
	
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject();
		this.nonSerializableSubscribers = new HashMap<Long, List<EventListener>>();
	}
}
