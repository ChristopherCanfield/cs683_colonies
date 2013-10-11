package com.divergentthoughtsgames.colonies.stats;

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

import java.io.Serializable;

import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.GameEvent;


// TODO (2013-05-02): Implement this in the future.

/**
 * Tracks game statistics.
 * @author Christopher D Canfield
 */
//public class StatsTracker implements EventListener, Serializable
//{
//	private static final long serialVersionUID = -5121980696234812689L;
//	
//	private long organismsBornCount;
//	private long organismsDiedCount;
//	
//	private float averageUnhappyPct;
//	private float averageHappyPct;
//	private float averageContentPct;
//	
//	private long minutesPlayed;
//	
//	private EventManager eventManager;
//	
//	
//	public StatsTracker (EventManager eventManager)
//	{
//		this.eventManager = eventManager;
//		
//		this.eventManager.subscribe(GameStatsRequestedEvent.ID, this);
//	}
//	
//	@Override
//	public void notify(GameEvent<?> event)
//	{
//		if (event instanceof GameStatsRequestedResponseEvent)
//		{
//			// TODO: process the stats.
//		}
//		else
//		{
//			throw new IllegalArgumentException("Unhandled event type received: " + event);
//		}
//	}
//}
