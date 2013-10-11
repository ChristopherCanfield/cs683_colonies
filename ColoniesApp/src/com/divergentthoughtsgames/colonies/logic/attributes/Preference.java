package com.divergentthoughtsgames.colonies.logic.attributes;

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

/**
 * A preference range.
 * @author Christopher D Canfield
 */
public enum Preference 
{
	Love, Like, None, Dislike, Hate;
	
	
	/**
	 * Converts an int to a Preference. Values should be in the range
	 * of 1 through 5, with 1 (or less) being equivalent to Hate and 5 (or more)
	 * being equivalent to Love.
	 * @param i The value to convert.
	 * @return A Preference. 
	 */
	public static Preference valueOf(int i)
	{
		if (i <= 1)
		{
			return Hate;
		}
		else if (i == 2)
		{
			return Dislike;
		}
		else if (i == 3)
		{
			return None;
		}
		else if (i == 4)
		{
			return Like;
		}
		else
		{
			return Love;
		}
	}
}
