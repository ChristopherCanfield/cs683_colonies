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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.divergentthoughtsgames.colonies.event.EventManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


/**
 * @author Christopher D. Canfield
 */
public class App extends Application
{
	private static final String LOG_TAG = "App";
	
	// The file name for the saved game state.
	private static final String GAME_STATE_FILE = "game_state.colonies";
	
	private ScheduledExecutorService executor;
	private GameManager gameManager;
	
	public App()
	{
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * Starts the game simulation.
	 */
	public void startSimulation()
	{
		if (this.gameManager == null)
		{
			this.gameManager = new GameManager(loadOrganismNames());
		}
		
		if (this.gameManager.isPaused())
		{
			this.gameManager.unpause();
			if (this.executor == null || this.executor.isShutdown())
			{
				this.executor = Executors.newSingleThreadScheduledExecutor();
			}
			this.executor.scheduleAtFixedRate(this.gameManager, 0, GameManager.MILLIS_PER_GAME_TICK, TimeUnit.MILLISECONDS);
		}
	}

	
	/**
	 * Stops the game simulation. Can be started again by calling
	 * startSimulation().
	 */
	public void stopSimulation()
	{
		if (this.gameManager != null)
		{
			this.gameManager.pause();
		}
		this.executor.shutdown();
	}

	/**
	 * Resets the game simulation so a new game can be played.
	 */
	public void resetSimulation()
	{
		stopSimulation();
		this.gameManager = new GameManager(loadOrganismNames());
		startSimulation();
	}
	
	public GameManager getGameManager()
	{
		return this.gameManager;
	}
	
	public EventManager getEventManager()
	{
		return this.gameManager.getEventManager();
	}
	
	
	/**
	 * Saves the game state to the file system.
	 */
	public void saveGameState()
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream file = openFileOutput(GAME_STATE_FILE, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(file);
			
			oos.writeObject(this.gameManager);
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG, "saveGameState Error", e);
			e.printStackTrace();
		}
		finally
		{
			if (oos != null)
			{
				try
				{
					oos.close();
				}
				catch (IOException e)
				{
					Log.e(LOG_TAG, "saveGameState Error", e);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Restores the state from the file system.
	 */
	public void loadGameState()
	{
		ObjectInputStream ois = null;
		try
		{
			FileInputStream file = openFileInput(GAME_STATE_FILE);
			ois = new ObjectInputStream(file);
			
			this.gameManager = (GameManager)ois.readObject();
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG, "loadGameState Error", e);
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			Log.e(LOG_TAG, "loadGameState Error", e);
			e.printStackTrace();
		}
		finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				}
				catch (IOException e)
				{
					Log.e(LOG_TAG, "loadGameState Error", e);
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private List<String> loadOrganismNames()
	{	
		List<String> names = new ArrayList<String>(150);
		BufferedReader br = null;
		try {
			Resources res = getResources();
	        InputStream is = res.openRawResource(R.raw.organism_names);
			br = new BufferedReader(new InputStreamReader(is));
			
			String name;
			while ((name = br.readLine()) != null)
			{
				names.add(name);
			}
		}
		catch(IOException e) {
			Log.e(LOG_TAG, "loadOrganismNames Error", e);
			e.printStackTrace();
		}
		finally {
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					Log.e(LOG_TAG, "loadOrganismNames Error", e);
					e.printStackTrace();
				}
			}
		}
		
		return names;
	}
}
