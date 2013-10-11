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
import java.util.HashMap;
import java.util.Map;

import com.divergentthoughtsgames.colonies.GameActivity;


/**
 * @author Christopher D. Canfield
 */
public class WorldGrid implements Serializable
{
	private static final long serialVersionUID = -3590384828640978569L;

	/** The number of rows in the world grid **/
	public static int ROWS = GameActivity.GRID_ROWS;
	
	/** The number of columns in the world grid **/
	public static int COLUMNS = GameActivity.GRID_COLUMNS;
	
	// References to entities within the game world, including Organisms and
	// environmental effects.
	private final Map<GridPosition, WorldGridEntity> entities = new HashMap<GridPosition, WorldGridEntity>();
	
	// The attributes for each grid position.
	private final GridAttributes[][] gridAttributes = new GridAttributes[ROWS][COLUMNS];
	
	
	
	public WorldGrid()
	{
	}
	
	public GridAttributes[][] getGridAttributes()
	{
		return this.gridAttributes;
	}
	
	public WorldGridEntity getGridEntity(GridPosition gp)
	{
		return this.entities.get(gp);
	}
	
	public WorldGridEntity getGridEntity(int row, int column)
	{
		return this.entities.get(new GridPosition(row, column));
	}
	
	public void setGridEntity(GridPosition gp, WorldGridEntity ent)
	{
		this.entities.put(gp, ent);
	}
}
