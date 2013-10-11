package com.divergentthoughtsgames.colonies.logic;

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
import java.util.ArrayList;
import java.util.List;
import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.Updatable;
import com.divergentthoughtsgames.colonies.event.GameEvent;
import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.GamePausedEvent;
import com.divergentthoughtsgames.colonies.event.GameUnpausedEvent;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedData;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedEvent;


/**
 * Manages the game logic.
 * @author Christopher D Canfield
 */
public class GameLogicManager implements Updatable, EventListener, Serializable
{
	private static final long serialVersionUID = 2415510221546685135L;

	// A list of all OrganismManagers (i.e., 'species' or 'groups').
	private final List<OrganismManager> colonies = new ArrayList<OrganismManager>();
	
	private final WorldGrid worldGrid;
	
	// Specifies whether the game is paused or not.
	private boolean isPaused = false;
	
	private final GameManager gameManager;
	
	
	public GameLogicManager(GameManager gameManager)
	{
		this.gameManager = gameManager;
		this.worldGrid = new WorldGrid();
		
		// Subscribe to events.
		EventManager eventManager = this.gameManager.getEventManager();
		eventManager.subscribe(NewColonyPlacedEvent.ID, this);
		eventManager.subscribe(GamePausedEvent.ID, this);
		eventManager.subscribe(GameUnpausedEvent.ID, this);
	}
	
	/**
	 * Loops through all OrganismManagers once per game tick.
	 * @throws IllegalArgumentException When gameTicks is less than zero.
	 */
	@Override
	public void update(long gameTicks)
	{
		if (gameTicks < 0) throw new IllegalArgumentException("Argument 'gameTicks' cannot be negative.");
		
		if (!this.isPaused)
		{
			// TODO (2013-03-30): implement this.
			
			
			// Loop through all colonies (groups of Organisms).
			for (OrganismManager orgManager : this.colonies)
			{
				orgManager.update(gameTicks);
			}
		}
	}
	

	@Override
	public void notify(GameEvent<?> event)
	{
		// TODO (2013-03-30): implement this: 
		//	- NewColonyPlacedEvent
		//  - GamePausedEvent
		//  - GameUnpausedEvent
		//  - ShutdownEvent
		
		if (event instanceof NewColonyPlacedEvent)
		{
			NewColonyPlacedData data = ((NewColonyPlacedEvent)event).getData();
			
			OrganismManager manager = new OrganismManager(data, 
					this.worldGrid, 
					this.gameManager);
			
			this.colonies.add(manager);
		}
		else if (event instanceof GamePausedEvent)
		{
			this.isPaused = true;
			// TODO (2013-04-01): is additional processing required for GamePausedEvent?
		}
		else if (event instanceof GameUnpausedEvent)
		{
			this.isPaused = false;
			// TODO (2013-04-01): is additional processing required for GameUnpausedEvent?
		}
		else
		{
			throw new IllegalArgumentException(
					"Invalid Event type provided to GameLogicManager.notify(): " + event.toString() + ".");
		}
	}
}
