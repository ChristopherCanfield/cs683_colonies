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

import com.divergentthoughtsgames.colonies.logic.DynamicAttributes;
import com.divergentthoughtsgames.colonies.logic.GridPosition;
import com.divergentthoughtsgames.colonies.logic.Organism;
import com.divergentthoughtsgames.colonies.logic.StaticAttributes;

/**
 * Information required by the OrganismInfoRequestedResponseEvent.
 * @author Christopher D Canfield
 */
public final class OrganismInfo implements Serializable
{
	private static final long serialVersionUID = 4146863533975075005L;
	
	/** The StaticAttributes of the organism **/
	private final StaticAttributes staticAttributes;
	/** The DynamicAttributes of the organism **/
	private final DynamicAttributes dynamicAttributes;
	/** The current game tick count **/
	private final long gameTick;
	/** The organism's name **/
	private final String name;
	/** The organism't grid position **/
	private final GridPosition position;
	
	/**
	 * @param staticAttributes The StaticAttributes of the organism.
	 * @param dynamicAttributes The DynamicAttributes of the organism.
	 * @param position The location on the game grid.
	 * @param name The Organism's name.
	 * @param gameTick The current game tick count.
	 * @throws IllegalArgumentException if staticAttributes, dynamicAttributes, position or name is null, 
	 * or gameTick is less than zero. 
	 */
	public OrganismInfo(StaticAttributes staticAttributes, DynamicAttributes dynamicAttributes, GridPosition position, 
			String name, long gameTick)
	{
		if (staticAttributes == null)
			throw new IllegalArgumentException("Argument 'staticAttributes' cannot be null.");
		if (dynamicAttributes == null)
			throw new IllegalArgumentException("Argument 'dynamicAttributes' cannot be null.");
		if (position == null)
			throw new IllegalArgumentException("Argument 'position' cannot be null.");
		if (name == null)
			throw new IllegalArgumentException("Argument 'name' cannot be null.");
		if (gameTick < 0)
			throw new IllegalArgumentException("Argument 'gameTick' cannot be less than zero.");
		
		this.staticAttributes = new StaticAttributes(staticAttributes);
		this.dynamicAttributes = new DynamicAttributes(dynamicAttributes);
		this.position = position;
		this.name = name;
		this.gameTick = gameTick;
	}
	
	/**
	 * @param organism The Organism to get data from.
	 * @param gameTick The current game tick count.
	 * @throws IllegalArgumentException if organism is null, or gameTick is less than zero. 
	 */
	public OrganismInfo(Organism organism, long gameTick)
	{
		if (organism == null)
			throw new IllegalArgumentException("Argument 'organism' cannot be null.");
		if (gameTick < 0)
			throw new IllegalArgumentException("Argument 'gameTick' cannot be less than zero.");
		
		this.staticAttributes = new StaticAttributes(organism.getStaticAttributes());
		this.dynamicAttributes = new DynamicAttributes(organism.getDynamicAttributes());
		this.position = organism.getPosition();
		this.name = organism.getName();
		this.gameTick = gameTick;
	}
	
	public StaticAttributes getStaticAttributes()
	{
		return this.staticAttributes;
	}
	
	public DynamicAttributes getDynamicAttributes()
	{
		return this.dynamicAttributes;
	}
	
	public long getGameTick()
	{
		return this.gameTick;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public GridPosition getGridPosition()
	{
		return this.position;
	}
}
