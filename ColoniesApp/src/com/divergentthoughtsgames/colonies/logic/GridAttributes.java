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

import com.divergentthoughtsgames.colonies.logic.attributes.HeatLevel;


/**
 * The attributes for a grid position in the game world.
 * @author Christopher D Canfield
 */
final class GridAttributes implements Serializable
{
	private static final long serialVersionUID = -5615705178643471291L;
	
	private HeatLevel heatLevel;
	private int neighborsCount;
	
	GridAttributes(HeatLevel heatLevel, int neighborsCount)
	{
		if (neighborsCount < 0) throw new IllegalArgumentException("Invalid neighbors count. Expected >= 0, found " + neighborsCount);
		
		this.heatLevel = heatLevel;
		this.neighborsCount = neighborsCount;
	}

	/**
	 * Gets the heat level.
	 * @return The heat level.
	 */
	public HeatLevel getHeatLevel()
	{
		return this.heatLevel;
	}

	/**
	 * Sets the heat level.
	 * @param heatLevel The heat level.
	 */
	public void setHeatLevel(HeatLevel heatLevel)
	{
		this.heatLevel = heatLevel;
	}

	/**
	 * Gets the number of neighbors adjacent to this grid position.
	 * @return The number of neighbors adjacent to this grid position.
	 */
	public int getNeighborsCount()
	{
		return this.neighborsCount;
	}

	/**
	 * Sets the number of neighbors adjacent to this grid position.
	 * @param neighborsCount The number of neighbors adjacent to this grid position.
	 * @throws IllegalArgumentException if neighborsCount is less than zero.
	 */
	public void setNeighborsCount(int neighborsCount)
	{
		if (neighborsCount < 0) 
			throw new IllegalArgumentException("Invalid neighbors count. Expected >= 0, found " + neighborsCount);
		
		this.neighborsCount = neighborsCount;
	}
}
