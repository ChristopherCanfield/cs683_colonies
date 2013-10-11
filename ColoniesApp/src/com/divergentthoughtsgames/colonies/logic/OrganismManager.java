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
import java.util.Random;

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.Updatable;
import com.divergentthoughtsgames.colonies.event.AllOrganismsRequestedEvent;
import com.divergentthoughtsgames.colonies.event.AllOrganismsRequestedResponse;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.GameEvent;
import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedData;
import com.divergentthoughtsgames.colonies.event.OrganismBornData;
import com.divergentthoughtsgames.colonies.event.OrganismBornEvent;
import com.divergentthoughtsgames.colonies.event.OrganismInfo;
import com.divergentthoughtsgames.colonies.event.OrganismInfoRequestedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismInfoRequestedResponse;
import com.divergentthoughtsgames.colonies.event.OrganismPoppedEvent;

import android.util.Log;

/**
 * Controls the actions of a group of organisms.
 * Calls update() on all organisms within the group,
 * and processes group actions.
 * @author Christopher D Canfield
 */
class OrganismManager implements Updatable, EventListener, Serializable
{
	private static final long serialVersionUID = -7880284065497114799L;

	private static final Random randomGenerator = new Random();
	
	private final long id;
	
	// The Organisms in the group.
	private final List<Organism> organisms;
	
	// A reference to the World Grid (the World Map). 
	private final WorldGrid worldGrid;
	
	private final GameManager gameManager;
	
	private long lastGameTick = 0;
	
	// The position on the grid that the group is expanding toward.
//	private GridPosition gridTarget = null;

	/**
	 * Creates a new instance of an OrganismManager, which controls a group
	 * of Organisms (i.e., a colony or species of Organisms).
	 * @param initialAttributes The initial static attributes for this set of Organisms.
	 * @param worldGrid A reference to the world grid (world map).
	 * @param GameManager A reference to the GameManager.
	 * @throws IllegalArgumentException when initialAttributes, worldGrid or gameManager is null.
	 */
	OrganismManager(NewColonyPlacedData colonyData, WorldGrid worldGrid, GameManager gameManager)
	{
		if (colonyData == null) 
			throw new IllegalArgumentException("Argument 'colonyData' cannot be null.");
		if (worldGrid == null)
			throw new IllegalArgumentException("Argument 'worldGrid' cannot be null.");
		if (gameManager == null)
			throw new IllegalArgumentException("Argument 'gameManager' cannot be null.");
		
		this.id = randomGenerator.nextLong();
		this.worldGrid = worldGrid;
		this.gameManager = gameManager;
		this.lastGameTick = colonyData.getGameTicks();
		
		EventManager eventManager = this.gameManager.getEventManager();
		this.organisms = populateValidGridPositions(this.worldGrid, this.gameManager, colonyData);
		for (final Organism org : this.organisms)
		{
			OrganismBornData data = new OrganismBornData(org.getStaticAttributes(), org.getPosition());
			OrganismBornEvent event = new OrganismBornEvent(data);
			eventManager.notify(event);
		}
		
		eventManager.subscribe(OrganismInfoRequestedEvent.ID, this);
		eventManager.subscribe(AllOrganismsRequestedEvent.ID, this);
		eventManager.subscribe(OrganismPoppedEvent.ID, this);
	}
	
	
	/** Calls update() on all Organisms within the group. Performs reproduction
	 * if at least two Organisms are ready.
	 * @param gameTicks The current game tick count.
	 * @throws IllegalArgumentException when gameTicks is negative.
	 */
	@Override
	public void update(long gameTicks)
	{
		if (gameTicks < 0) 
			throw new IllegalArgumentException("Argument 'gameTicks' cannot be negative. Found: " + gameTicks);
		
		this.lastGameTick = gameTicks;
		
		// Hold references to two Organisms that are ready to reproduce, if available.
		Organism reproducer1 = null;
		Organism reproducer2 = null;
		
		// Loop through all organisms.
		for (final Organism organism : this.organisms)
		{
			organism.update(gameTicks);
			
			// Save a reference to the Organism if it is ready to reproduce and the reproducer slots have
			// not already been filled.
			if ((reproducer1 == null || reproducer2 == null) && organism.isReadyToReproduce(gameTicks))
			{
				if (reproducer1 == null)
				{
					reproducer1 = organism;
				}
				else
				{
					reproducer2 = organism;
				}
			}
		}
		
		// If two Organisms are ready to reproduce, call the reproduce method.
		if (reproducer1 != null && reproducer2 != null)
		{
			reproduce(reproducer1, reproducer2, gameTicks);
		}
	}

	/**
	 * Gets the OrganismManager's unique ID.
	 * @return The OrganismManager's unique ID.
	 */
	public long getId()
	{
		return this.id;
	}
	
	/**
	 * Returns a list of grid positions that are empty and relevant for the new colony.
	 * @param grid The world grid.
	 * @return A list of grid positions.
	 */
	private static List<Organism> populateValidGridPositions(WorldGrid grid, GameManager gameManager, NewColonyPlacedData data)
	{
		List<Organism> organisms = new ArrayList<Organism>();
		
		int row = data.getLocation().getRow();
		int column = data.getLocation().getColumn();
		
		for (int numberRemaining = data.getCount(); numberRemaining > 0; --numberRemaining)
		{
			Log.d("populateValidGridPositions", "Number remaining: " + numberRemaining);
			
			GridPosition position = getEmptyPosition(grid, new GridPosition(row, column));
			
			if (position == null)
			{
				Log.d("populateValidGridPositions", "Encountered position == null.");				
				break;
			}
			else
			{
				Log.d("populateValidGridPositions", "Added org to " + position.getRow() + ", " + position.getColumn());		
				
				Organism org = new Organism(data.getAttributes(),
						gameManager.getEventManager(),
						grid,
						position,
						data.getGameTicks());
				org.setName(gameManager.getRandomName());
				
				organisms.add(org);
				grid.setGridEntity(position, org);
			}
		}
		
		return organisms;
	}
	
	/**
	 * Returns an empty position at or near the firstAttempt position, if possible, or
	 * null if not possible.
	 * @param grid The world grid.
	 * @param firstAttempt The first position to check.
	 * @return An empty position in the world grid, or null if not possible.
	 */
	private static GridPosition getEmptyPosition(WorldGrid grid, GridPosition firstAttempt)
	{
		if (grid.getGridEntity(firstAttempt) == null)
		{
			return firstAttempt;
		}
		
		int row = firstAttempt.getRow();
		int column = firstAttempt.getColumn();
		
		// Up one row, current column. 
		if (row > 0 && grid.getGridEntity(row - 1, column) == null)
		{
			return new GridPosition(row - 1, column);
		}
		// Same row, right one column.
		if (column <= (WorldGrid.COLUMNS - 2) && grid.getGridEntity(row, column + 1) == null)
		{
			return new GridPosition(row, column + 1);
		}
		// Up one row, right one column.
		if (column <= (WorldGrid.COLUMNS - 2) && row > 0 && grid.getGridEntity(row - 1, column + 1) == null)
		{
			return new GridPosition(row - 1, column + 1);
		}
		// Down one row, current column.
		if (row <= (WorldGrid.ROWS - 2) && grid.getGridEntity(row + 1, column) == null)
		{
			return new GridPosition(row + 1, column);
		}
		// Current row, left one column.
		if (column > 0 && grid.getGridEntity(row, column - 1) == null)
		{
			return new GridPosition(row, column - 1);
		}
		// Up one row, left one column.
		if (column > 0 && row > 0 && grid.getGridEntity(row - 1, column - 1) == null)
		{
			return new GridPosition(row - 1, column - 1);
		}
		// Down one row, right one column.
		if (column < (WorldGrid.COLUMNS - 2) && row < (WorldGrid.ROWS - 2) && grid.getGridEntity(row + 1, column + 1) == null)
		{
			return new GridPosition(row + 1, column + 1);
		}
		// Down one row, left one column.
		if (column > 0 && row < (WorldGrid.ROWS - 2) && grid.getGridEntity(row + 1, column - 1) == null)
		{
			return new GridPosition(row + 1, column - 1);
		}
			
		return null;
	}
	
	/**
	 * Creates a new Organism by blending the StaticAttributes contained within
	 * its parent Organisms, plus some randomness. The location on the world grid
	 * is determined by the current grid target of the group.
	 * @param reproducer1 An Organism that is ready to reproduce.
	 * @param reproducer2 An Organism that is ready to reproduce.
	 * @param gameTicks
	 */
	private void reproduce(Organism reproducer1, Organism reproducer2, long gameTicks)
	{
		final int MAX_ATTEMPTS = 5;
		
		for (int numberOfAttempts = 0; numberOfAttempts < MAX_ATTEMPTS; ++numberOfAttempts)
		{
			GridPosition target = identifyNewGridTarget(this.organisms);
			GridPosition emptyPosition = getEmptyPosition(this.worldGrid, target);
			
			if (emptyPosition != null)
			{
				// TODO (2013-05-10): properly carry attributes from both parents to child.
				Organism org = new Organism(reproducer1.getStaticAttributes(),
						this.gameManager.getEventManager(),
						this.worldGrid,
						emptyPosition,
						gameTicks);
				org.setName(this.gameManager.getRandomName());
				
				this.organisms.add(org);
				this.worldGrid.setGridEntity(emptyPosition, org);
				
				reproducer1.getDynamicAttributes().addChild();
				reproducer2.getDynamicAttributes().addChild();
				
				Random rand = new Random();
				reproducer1.getDynamicAttributes().setLastReproducedTicks(gameTicks + rand.nextInt(3000));
				reproducer2.getDynamicAttributes().setLastReproducedTicks(gameTicks + rand.nextInt(3000));
				
				OrganismBornData data = new OrganismBornData(org.getStaticAttributes(), org.getPosition());
				OrganismBornEvent event = new OrganismBornEvent(data);
				this.gameManager.getEventManager().notify(event);
				
				return;
			}
		}
		
	}
	
	/**
	 * Identifies a target on the world grid. The target is determined by distance,
	 * the group's preference for crowds, the availability of food, and the group's
	 * preference for heat.
	 * @param organisms The list of Organisms within this group.
	 * @return The position of the new target.
	 */
	private static GridPosition identifyNewGridTarget(List<Organism> organisms)
	{
		Random rand = new Random();
		int i = rand.nextInt(organisms.size());
		Organism org = organisms.get(i);
		
		return new GridPosition(org.getPosition());
	}

	@Override
	public void notify(GameEvent<?> event)
	{
		if (event instanceof OrganismInfoRequestedEvent)
		{
			OrganismInfoRequestedEvent infoRequestEvent = (OrganismInfoRequestedEvent)event;
			
			for (final Organism org : this.organisms)
			{
				if (org.getPosition().equals(infoRequestEvent.getData()))
				{
					OrganismInfo info = new OrganismInfo(org, this.lastGameTick);
					
					this.gameManager.getEventManager().notify(new OrganismInfoRequestedResponse(info));
				}
			}
		}
		else if (event instanceof AllOrganismsRequestedEvent)
		{
			for (final Organism org : this.organisms)
			{
				OrganismInfo data = new OrganismInfo(org, this.lastGameTick);
				this.gameManager.getEventManager().notify(new AllOrganismsRequestedResponse(data));
			}
		}
		else if (event instanceof OrganismPoppedEvent)
		{
			GridPosition gp = ((OrganismPoppedEvent)event).getData();
			this.worldGrid.setGridEntity(gp, null);
			
			int orgToRemove = -1;
			for (final Organism org : this.organisms)
			{
				if (org.getPosition().equals(gp))
				{
					++orgToRemove;
					break;
				}
				++orgToRemove;
			}
			
			if (orgToRemove != -1)
			{
				this.organisms.remove(orgToRemove);
			}
		}
		else
		{
			throw new IllegalArgumentException("Unhandled event type received: " + event);
		}
	}
}
