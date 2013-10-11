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

import java.io.Serializable;

import com.divergentthoughtsgames.colonies.logic.GridPosition;
import com.divergentthoughtsgames.colonies.logic.StaticAttributes;

/**
 * Data required by the NewColonyPlacedEvent.
 * @author Christopher D Canfield
 */
public final class NewColonyPlacedData implements Serializable
{
	private static final long serialVersionUID = -1129065994261022482L;
	
	/** The initial attributes for the new colony. **/
	private final StaticAttributes initialAttributes;
	/** The initial location for the new colony. **/
	private final GridPosition initialLocation;
	/** The initial organisms count. **/
	private final int initialCount;
	/** The game ticks count at time of birth. **/
	private final long gameTicks;
	
	/**
	 * @param attributes The starting StaticAttributes for this colony.
	 * @param location The starting location for this colony.
	 * @param organismsCount The initial number of organisms in this colony.
	 * @param gameTicks The current game tick count.
	 * @throws IllegalArgumentException if attributes or location is null, or organismsCount is less than 2, 
	 * or gameTicks is less than 0.
	 */
	public NewColonyPlacedData(StaticAttributes attributes, GridPosition location, int organismsCount, long gameTicks)
	{
		if (attributes == null)
			throw new IllegalArgumentException("Argument 'attributes' cannot be null.");
		if (location == null)
			throw new IllegalArgumentException("Argument 'location' cannot be null.");
		if (organismsCount <= 1)
			throw new IllegalArgumentException("Argument 'organismsCount' cannot be less than 2. Found: " + organismsCount);
		if (gameTicks < 0)
			throw new IllegalArgumentException("Argument 'gameTicks' cannot be less than zero. Found: " + gameTicks);
		
		
		this.initialAttributes = attributes;
		this.initialLocation = location;
		this.initialCount = organismsCount;
		this.gameTicks = gameTicks;
	}
	
	public StaticAttributes getAttributes()
	{
		return this.initialAttributes;
	}
	
	public GridPosition getLocation()
	{
		return this.initialLocation;
	}
	
	public int getCount()
	{
		return this.initialCount;
	}
	
	public long getGameTicks()
	{
		return this.gameTicks;
	}
}
