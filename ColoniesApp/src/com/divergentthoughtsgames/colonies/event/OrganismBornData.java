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
 * Data required by the OrganismBornEvent.
 * @author Christopher D Canfield
 */
public final class OrganismBornData implements Serializable
{
	private static final long serialVersionUID = -2133220975188573111L;
	
	/** The static attributes of the new organism **/
	private StaticAttributes staticAttributes;
	/** The position of the new organism **/
	private GridPosition location;
	
	/**
	 * @param staticAttributes The StaticAttributes for the new Organism.
	 * @param location The location of the new Organism.
	 * @throws IllegalArgumentException if staticAttributes or location is null;
	 */
	public OrganismBornData(StaticAttributes staticAttributes, GridPosition location)
	{
		if (staticAttributes == null)
			throw new IllegalArgumentException("Argument 'staticAttributes' cannot be null.");
		if (location == null)
			throw new IllegalArgumentException("Argument 'location' cannot be null.");
		
		this.staticAttributes = staticAttributes;
		this.location = location;
	}
	
	public StaticAttributes getAttributes()
	{
		return this.staticAttributes;
	}
	
	public GridPosition getLocation()
	{
		return this.location;
	}
}
