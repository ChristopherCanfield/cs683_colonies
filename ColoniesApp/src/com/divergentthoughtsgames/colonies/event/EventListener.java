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

/**
 * Listener for Event notifications. An implementer should also
 * override the equals() method.
 * @author Christopher D Canfield
 */
public interface EventListener
{
	/**
	 * Called by the EventManager when a subscribed Event has occurred.
	 * @param event The Event that has occurred.
	 */
	public void notify(GameEvent<?> event);
}
