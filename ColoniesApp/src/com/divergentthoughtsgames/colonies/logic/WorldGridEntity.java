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

/**
 * Objects that exist on the world grid, including Organisms and 
 * Environmental Effects, must derive from this abstract class. 
 * @author Christopher D Canfield
 */
abstract class WorldGridEntity implements Serializable
{
	private static final long serialVersionUID = -1014115305100012605L;
	
	private final long id;
	
	WorldGridEntity(long id)
	{
		this.id = id;
	}
	
	/**
	 * Gets the entity's unique ID. 
	 * @return The entity's unique ID.
	 */
	public long getId()
	{
		return this.id;
	}
}