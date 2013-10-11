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

import java.util.Random;

import android.util.Log;

import com.divergentthoughtsgames.colonies.Updatable;
import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.GameEvent;
import com.divergentthoughtsgames.colonies.event.OrganismDiedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismHappinessChangedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismInfo;
import com.divergentthoughtsgames.colonies.logic.attributes.Happiness;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;


/**
 * A single organism in the game.
 * @author Christopher D Canfield
 */
final public class Organism extends WorldGridEntity implements Updatable, EventListener
{
	private static final long serialVersionUID = -6186761110752975566L;

	private static final Random randomGenerator = new Random();
	
	// The Organism's static attributes: i.e., the attributes that it is born with.
	private final StaticAttributes staticAttributes;
	// The Organism's dynamic attributes: i.e., the attributes that change based on time and other factors.
	private final DynamicAttributes attributes;
	
	// The Organism's position in the World Grid (i.e., the World Map).
	private final GridPosition position;
	
	// The Organism's name.
	private String name;
	
	// The event manager.
	private final EventManager eventManager;
	
	private final WorldGrid worldGrid;
	
	// The time that the Organism died, in game ticks, or 0 if it has not yet died.
	private long deathTime = 0;

	/**
	 * Creates a new instance of an Organism.
	 * @param staticAttribute The Organism's static attributes.
	 * @param eventManager The event manager.
	 * @param worldGrid The world grid.
	 * @param position The Organism's position on the world grid (world map).
	 * @param gameTicks The current game tick count.
	 * @throws IllegalArgumentException if staticAttribute, eventManager or position is null,
	 * or if gameTicks is less than zero.
	 */
	Organism(StaticAttributes staticAttributes, EventManager eventManager, 
			WorldGrid worldGrid, GridPosition position, long gameTicks)
	{
		super(randomGenerator.nextLong());
		
		if (staticAttributes == null)
			throw new IllegalArgumentException("Argument 'staticAtributes' cannot be null.");
		if (eventManager == null)
			throw new IllegalArgumentException("Argument 'eventManager' cannot be null.");
		if (position == null)
			throw new IllegalArgumentException("Argument 'position' cannot be null.");
		if (gameTicks < 0)
			throw new IllegalArgumentException("Argument 'gameTicks' must be greater than or equal to zero. Found: " + gameTicks);
		
		final int MAX_AGE_RANGE_FACTOR = 15;
		this.staticAttributes = StaticAttributes.applyFactorToMaxAge(staticAttributes, MAX_AGE_RANGE_FACTOR);
		this.attributes = new DynamicAttributes(this.staticAttributes, gameTicks);
		this.eventManager = eventManager;
		this.worldGrid = worldGrid;
		this.position = position;
		
		// Ideally, this would be generated or pulled from a file.
		this.name = "Organism " + getId();
	}
		
	/**
	 * Gets the Organism's static attributes.
	 * @return The Organism's static attributes.
	 */
	public StaticAttributes getStaticAttributes()
	{
		return this.staticAttributes;
	}
	
	/**
	 * Gets the Organism's dynamic attributes.
	 * @return The Organism's dynamic attributes.
	 */
	public DynamicAttributes getDynamicAttributes()
	{
		return this.attributes;
	}
	
	/**
	 * Gets the Organism's position on the game grid.
	 * @return The Organism's position on the game grid.
	 */
	public GridPosition getPosition()
	{
		return this.position;
	}
	
	/**
	 * Gets the Organism's name.
	 * @return The Organism's name.
	 */
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Identifies whether the Organism is ready to reproduce.
	 * @param gameTicks The current game tick count.
	 * @return Whether the Organism is ready to reproduce.
	 * @throws IllegalArgumentException when gameTicks is negative.
	 */
	public boolean isReadyToReproduce(long gameTicks)
	{
		if (gameTicks < 0)
			throw new IllegalArgumentException("Argument 'gameTicks' cannot be negative. Found: " + gameTicks);
		
		long timeout = this.attributes.getLastReproducedTicks() + 
				StaticAttributes.getReproductiveTimeout(this.staticAttributes.getReproductiveFrequency());
		return (gameTicks > timeout);
	}

	/**
	 * Performs all Organism logic updates.
	 * @param gameTicks The current game tick count.
	 * @throws IllegalArgumentException when gameTicks is negative.
	 */
	@Override
	public void update(long gameTicks)
	{
		if (gameTicks < 0) 
			throw new IllegalArgumentException("Argument 'gameTicks' cannot be negative. Found: " + gameTicks);
		
		// Don't process updates if the Organism has died.
		if (this.deathTime > 0) 
		{
			return;
		}
		
		this.attributes.incrementAgeTicks();
		
		if (this.attributes.getAgeTicks() > this.staticAttributes.getMaxAgeTicks())
		{
			this.deathTime = this.attributes.getAgeTicks();
			this.eventManager.notify(new OrganismDiedEvent(this.position));
		}
		
		this.attributes.updateHappiness(gameTicks);
		if (this.attributes.isReadyToReportHappiness(gameTicks))
		{
			OrganismInfo info = new OrganismInfo(this, gameTicks);
			OrganismHappinessChangedEvent e = new OrganismHappinessChangedEvent(info);
			Log.d("Organism", "OrganismHappinessChangedEvent sent: " + info.getDynamicAttributes().getHappiness().toString());
			this.eventManager.notify(e);
		}
		
		Happiness newCrowdHappiness = calculateCrowdHappiness(
				this.staticAttributes.getCrowdPreference(), getNeighborCount(this.worldGrid, this.position));
		if (this.attributes.getCrowdHappiness() != newCrowdHappiness)
		{
			this.attributes.setCrowdHappiness(newCrowdHappiness);
		}
	}
	
	private static int getNeighborCount(WorldGrid grid, GridPosition thisPosition)
	{
		final int column = thisPosition.getColumn();
		final int row = thisPosition.getRow();
		
		int count = 0;
		
		count += (column < WorldGrid.COLUMNS - 1 && grid.getGridEntity(row, column + 1) != null) ? 1 : 0;
		count += (column > 0 && grid.getGridEntity(row, column - 1) != null) ? 1 : 0;
		
		count += (row < WorldGrid.ROWS - 1 && grid.getGridEntity(row + 1, column) != null) ? 1 : 0;
		count += (row > 0 && grid.getGridEntity(row - 1, column) != null) ? 1 : 0;
		
		count += (column < WorldGrid.COLUMNS - 1 && row < WorldGrid.ROWS - 1 && 
				grid.getGridEntity(row + 1, column + 1) != null) ? 1 : 0;
		count += (column < WorldGrid.COLUMNS - 1 && row > 0 &&
				grid.getGridEntity(row - 1, column + 1) != null) ? 1 : 0;
		count += (column > 0 && row < WorldGrid.ROWS - 1 &&
				grid.getGridEntity(row + 1, column - 1) != null) ? 1 : 0;
		count += (column > 0 && row > 0 && grid.getGridEntity(row - 1, column - 1) != null) ? 1 : 0;
		
		return count;
	}
	
	private static Happiness calculateCrowdHappiness(Preference crowdPreference, int neighborCount)
	{
		if (crowdPreference == Preference.Love)
		{
			if (neighborCount >= 7)
			{
				return Happiness.Happy;
			}
			else if (neighborCount >= 5)
			{
				return Happiness.Neutral;
			}
			else
			{
				return Happiness.Unhappy;
			}
		}
		else if (crowdPreference == Preference.Like)
		{
			if (neighborCount >= 3 && neighborCount < 7)
			{
				return Happiness.Happy;
			}
			else if (neighborCount == 8 || neighborCount > 2)
			{
				return Happiness.Neutral;
			}
			else
			{
				return Happiness.Unhappy;
			}
		}
		else if (crowdPreference == Preference.None)
		{
			return Happiness.Neutral;
		}
		else if (crowdPreference == Preference.Dislike)
		{
			if (neighborCount >= 3 && neighborCount < 7)
			{
				return Happiness.Unhappy;
			}
			else if (neighborCount == 8 || neighborCount > 2)
			{
				return Happiness.Neutral;
			}
			else
			{
				return Happiness.Happy;
			}
		}
		else if (crowdPreference == Preference.Hate)
		{
			if (neighborCount >= 7)
			{
				return Happiness.Unhappy;
			}
			else if (neighborCount >= 5)
			{
				return Happiness.Neutral;
			}
			else
			{
				return Happiness.Happy;
			}
		}
		throw new RuntimeException("Programming error: line should not be reached.");
	}

	@Override
	public void notify(GameEvent<?> event)
	{
	}

}
