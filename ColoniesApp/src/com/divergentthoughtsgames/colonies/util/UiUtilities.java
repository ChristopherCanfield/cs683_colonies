package com.divergentthoughtsgames.colonies.util;

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

import android.graphics.Color;

/**
 * A collection of user interface utilities.
 * @author Christopher D. Canfield
 */
public final class UiUtilities
{
	private static final Random RAND = new Random();
	
	private UiUtilities() {}
	
	/**
	 * Returns a random color with a white color mask applied to it. This tends
	 * to result in pastel colors. This is the same as calling getRandomColor(255).
	 * @return A random color with a white color mask applied to it, in the form
	 * of an int.
	 */
	public static int getRandomColor()
	{
		return getRandomColor(255);
	}
	
	/**
	 * Returns a random color with the specified color mask applied to it.
	 * @param mask A color mask to apply to the random color.
	 * @return A random color, in the form of an int.
	 */
	public static int getRandomColor(int mask)
	{		
		final int alpha = 255;
		
		int red = RAND.nextInt(256);
		int green = RAND.nextInt(256);
		int blue = RAND.nextInt(256);
		
		// This section adapted from 
		// http://stackoverflow.com/questions/43044/algorithm-to-randomly-generate-an-aesthetically-pleasing-color-palette
		red = (red + mask) / 2;
		green = (green + mask) / 2;
		blue = (blue + mask) / 2;
		
		return Color.argb(alpha, red, green, blue);
	}
}
