package com.divergentthoughtsgames.colonies;

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.logic.GameLogicManager;
import com.divergentthoughtsgames.colonies.sound.SoundManager;

import android.util.Log;


/**
 * Used by the primary thread for the Logic and Event systems.
 * @author Christopher D Canfield
 */
public class GameManager implements Serializable, Runnable
{
	private static final long serialVersionUID = 6637913566049267566L;
	
	// The target number of frames (game ticks) per second.
	public static final long FRAMES_PER_SECOND = 30;
	// The number of milliseconds per game tick.
	public static final long MILLIS_PER_GAME_TICK = 1000 / FRAMES_PER_SECOND;
	
	// The single instance of the GameManager.
//	private static final GameManager instance = new GameManager();
	
	// The game subsystems.
	private final EventManager eventManager;
	private final GameLogicManager gameLogicManager;
	private transient SoundManager soundManager;

	// The number of game ticks since the game has started. A game tick
	// is equal to one iteration of the game logic.
	private long gameTicks = 0;
	
	// The amount of time the last game tick took to execute, in milliseconds.
	private long executionTime = 0;
	
	// The time, in milliseconds, that the last frame ended.
	private long lastFrameEndTime = 0;
	
	// The result of the last frames (game ticks) per second calculation.
	private long framesPerSecond = 0;
	
	private final List<String> organismNames;
	
	private transient Calendar calendar;
	
	private boolean isPaused;

	
	public GameManager(List<String> organismNames)
	{	
		// Create instances of the non-transient game subsystems.
		this.eventManager = new EventManager();
		this.organismNames = organismNames;
		this.gameLogicManager = new GameLogicManager(this);
		
		stateRestore();
	}
	
	
	@Override
	public void run()
	{
		try
		{
			if (isPaused())
			{
				return;
			}
			
			long startTime = GameManager.this.calendar.getTimeInMillis();
			GameManager.this.framesPerSecond = calculateFramesPerSecond(startTime, GameManager.this.lastFrameEndTime, 
					GameManager.this.executionTime, GameManager.this.framesPerSecond);
			
			processUpdates();
			
			GameManager.this.executionTime = (GameManager.this.calendar.getTimeInMillis() - startTime);
		}
		catch (Exception e)
		{
			Log.e("GameManager", "Exception: " + e, e);
		}
	}
	
	
	public synchronized boolean isPaused()
	{
		return this.isPaused;
	}
	
	public String getRandomName()
	{
		Random rand = new Random();
		if (this.organismNames.size() > 0)
		{
			int nameIndex = rand.nextInt(this.organismNames.size());
			return this.organismNames.get(nameIndex);
		}
		return Integer.toString(rand.nextInt());
	}
	
	private void stateRestore()
	{
		this.calendar = Calendar.getInstance();
		
		// Create instances of the transient game subsystems.

		this.soundManager = new SoundManager(this);
	}
	
	public EventManager getEventManager()
	{
		return this.eventManager;
	}
	
	public long getGameTicks()
	{
		return this.gameTicks;
	}
	
	/**
	 * Pauses the game.
	 */
	public synchronized void pause()
	{
		this.isPaused = true;
	}
	
	/**
	 * Unpauses the game.
	 */
	public synchronized void unpause()
	{
		this.isPaused = false;
	}

	/**
	 * Calls update() on the EventManager and GameLogicManager, and
	 * increments the game tick counter.
	 */
	private void processUpdates()
	{
		this.eventManager.update(GameManager.this.gameTicks);
		this.gameLogicManager.update(GameManager.this.gameTicks);
		
		++this.gameTicks;
	}
	
	/**
	 * Calculates the frames (game ticks) per second.
	 * @param currentTimeInMillis The current time, in milliseconds.
	 * @return The number of frames per second.
	 */
	private static long calculateFramesPerSecond(long currentTimeInMillis, long lastFrameEndTime, 
			long lastExecutionTime, long framesPerSecond)
	{
		return (currentTimeInMillis - lastFrameEndTime + 
				lastExecutionTime) * framesPerSecond;
	}


	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject();
		stateRestore();
	}
}
