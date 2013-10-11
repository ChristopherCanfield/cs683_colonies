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
 * The row & column position within the World Grid.
 * @author Christopher D Canfield
 */
public final class GridPosition implements Serializable
{	
	private static final long serialVersionUID = 8730175940559249179L;
	
	private final int row;
	private final int column;
	
	public GridPosition(int row, int column)
	{
		if (row < 0) throw new IllegalArgumentException("row parameter is less than zero.");
		if (column < 0) throw new IllegalArgumentException("column parameter is less than zero.");
		if (row >= WorldGrid.ROWS) 
			throw new IllegalArgumentException("row parameter is larger than WorldGrid.ROWS - 1. Value: " + row);
		if (column >= WorldGrid.COLUMNS)
			throw new IllegalArgumentException("column parameter is larger than WorldGrid.COLUMNS - 1. Value: " + column);

		this.row = row;
		this.column = column;
	}
	
	public GridPosition(GridPosition gp)
	{
		if (gp == null)
			throw new IllegalArgumentException("Argument 'gp' cannot be null.");
		
		this.row = gp.row;
		this.column = gp.column;
	}
	
	/**
	 * Gets the row number within the World Grid.
	 * @return The row number within the World Grid.
	 */
	public int getRow()
	{
		return this.row;
	}
	
	/**
	 * Gets the column number within the World Grid.
	 * @return The column number within the World Grid.
	 */
	public int getColumn()
	{
		return this.column;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (!(o instanceof GridPosition))
		{
			return false;
		}
		
		GridPosition gp = (GridPosition)o;
		return (gp.column == this.column && gp.row == this.row);
	}
	
	@Override
	public int hashCode()
	{
		// From Bloch, Joshua; Effective Java, 2nd Edition.
		int result = 97; 
		result = (31 * result) + this.column;
		result = (31 * result) + this.row;
		return result;
	}
}
