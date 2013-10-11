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

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.logic.attributes.Action;
import com.divergentthoughtsgames.colonies.logic.attributes.Happiness;


/**
 * Organism attributes that can change over time. 
 * @author Christopher D Canfield
 */
public class DynamicAttributes implements Serializable
{
	private static final long serialVersionUID = -7808552607511760286L;
	
	// The current health. Float to allow incremental
	// increases/decreases in the health level over time.
	private float health;
	// maxHealth is stored, even though it is a StaticAttribute,
	// because it is needed for the setHealth() method.
	private final int maxHealth;
	// The current level of hunger, from 0 to 100, 
	// with 100 being most hungry. Float to allow
	// incremental increases/decreases in the hunger level.
	private float hunger;
	// The maximum hunger. Hunger ranges from 0% to 100%.
	private static final int MAX_HUNGER = 100;
	// The current age of the Organism, in ticks.
	private long ageTicks;
	// The last time the Organism reproduced, in ticks.
	private long lastReproducedTicks;
	// The crowd happiness level.
	private Happiness crowdHappiness;
	// The heat happiness level.
	private Happiness heatHappiness;
	// The number of children the Organism has had.
	private int childrenCount;
	// The current action being performed by the Organism.
	private Action currentAction;
	// The last time the current action was changed, in ticks.
	private long lastActionChangedTicks;
	// The time to report that the happiness level has changed. Used
	// to prevent a large number of happiness changed events from firing
	// if the organism's happiness is fluctuating.
	private long reportHappinessAt;
	// Whether a happiness changed event is pending.
	private boolean happinessChangePending;
	// The last happiness level.
	private Happiness lastHappinessLevel;
	

	DynamicAttributes(StaticAttributes attributes, long currentGameTicks)
	{
		if (attributes == null)
			throw new IllegalArgumentException("Argument 'attributes' cannot be null.");
		if (currentGameTicks < 0)
			throw new IllegalArgumentException("Argument 'currentGameTicks' cannot be less than zero.");
		
		this.health = attributes.getMaxHealth();
		this.maxHealth = attributes.getMaxHealth();
		this.hunger = 0;
		this.ageTicks = 0;
		this.lastReproducedTicks = currentGameTicks;
		this.crowdHappiness = Happiness.Neutral;
		this.heatHappiness = Happiness.Neutral;
		this.lastHappinessLevel = Happiness.Neutral;
		this.reportHappinessAt = 0;
		this.happinessChangePending = true;
		this.childrenCount = 0;
		this.currentAction = Action.Nothing;
		this.lastActionChangedTicks = currentGameTicks;
	}
	
	public DynamicAttributes(DynamicAttributes d)
	{
		this.maxHealth = d.maxHealth;
		this.health = d.health;
		this.hunger = d.hunger;
		this.ageTicks = d.ageTicks;
		this.lastReproducedTicks = d.lastReproducedTicks;
		this.crowdHappiness = d.crowdHappiness;
		this.heatHappiness = d.heatHappiness;
		this.childrenCount = d.childrenCount;
		this.currentAction = d.currentAction;
		this.lastActionChangedTicks = d.lastActionChangedTicks;
		this.reportHappinessAt = d.reportHappinessAt;
		this.happinessChangePending = d.happinessChangePending;
		this.lastHappinessLevel = d.lastHappinessLevel;
	}
	
	/**
	 * Gets the organism's current health.
	 * @return The organism's current health.
	 */
	public float getHealth()
	{
		return this.health;
	}
	
	/**
	 * Sets the organism's current health.
	 * @param health The organism's current health.
	 */
	public void setHealth(float health)
	{
		// Ensure that the health is no less than 0, and not greater than maxHealth.
		this.health = (health < 0) ? 0 : (health > this.maxHealth) ? this.maxHealth : health;
	}

	/**
	 * Gets the organism's hunger.
	 * @return The organism's hunger.
	 */
	public float getHunger()
	{
		return this.hunger;
	}
	
	/**
	 * Sets the organism's hunger.
	 * @param hunger The organism's hunger.
	 */
	public void setHunger(float hunger)
	{
		this.hunger = (hunger < 0) ? 0 : (hunger > MAX_HUNGER) ? MAX_HUNGER : hunger;
	}
	
	/**
	 * Gets the age of the organism, in game ticks.
	 * @return The age of the organism, in game ticks.
	 */
	public long getAgeTicks()
	{
		return this.ageTicks;
	}
	
	/**
	 * Increments the age of the organism by one game ticks.
	 */
	public void incrementAgeTicks()
	{
		++this.ageTicks;
	}
	
	/**
	 * Gets the last reproduction time, in game ticks.
	 * @return The last reproduction time, in game ticks.
	 */
	public long getLastReproducedTicks()
	{
		return this.lastReproducedTicks;
	}
	
	/**
	 * Sets the last reproduction time, in game ticks.
	 * @param lastReproducedTicks The last reproduced time, in game ticks.
	 */
	public void setLastReproducedTicks(long lastReproducedTicks)
	{
		if (lastReproducedTicks < 0) 
			throw new IllegalArgumentException("Argument 'lastReproducedTicks' cannot be less than zero.");
		
		this.lastReproducedTicks = lastReproducedTicks;
	}
	
	
	/**
	 * Gets the count of the number of children
	 * this Organism has had.
	 * @return The number of children this Organism has had.
	 */
	public int getChildCount()
	{
		return this.childrenCount;
	}
	
	/**
	 * Adds 1 to the Organism's child count.
	 */
	public void addChild()
	{
		++this.childrenCount;
	}
	
	/**
	 * Gets the current action being performed by the organism.
	 * @return The current action being performed by the organism.
	 */
	public Action getCurrentAction()
	{
		return this.currentAction;
	}
	
	/**
	 * Sets the current action being performed by the organism.
	 * @param action The current action being performed by the organism.
	 * @param currentGameTicks The number of ticks since the game started.
	 */
	public void setCurrentAction(Action action, long currentGameTicks)
	{
		if (currentGameTicks < 0)
			throw new IllegalArgumentException("Argument 'currentGameTicks' cannot be less than zero.");
		
		this.currentAction = action;
		this.lastActionChangedTicks = currentGameTicks;
	}
	
	/**
	 * Gets the last time the Action was changed, in game ticks.
	 * @return The last time the Action was changed, in game ticks.
	 */
	public long getLastActionChangedTicks()
	{
		return this.lastActionChangedTicks;
	}
	
	/**
	 * Gets the crowd happiness of the Organism.
	 * @return The crowd happiness of the Organism.
	 */
	public Happiness getCrowdHappiness()
	{
		return this.crowdHappiness;
	}
	
	/**
	 * Sets the crowd happiness of the Organism.
	 * @param crowdHappiness The crowd happiness of the Organism.
	 */
	public void setCrowdHappiness(Happiness crowdHappiness)
	{
		this.crowdHappiness = crowdHappiness;
	}
	
	/**
	 * Gets the heat happiness of the Organism.
	 * @return The heat happiness of the Organism.
	 */
	public Happiness getHeatHappiness()
	{
		return this.heatHappiness;
	}

	/**
	 * Sets the heat happiness of the Organism.
	 * @param heatHappiness The heat happiness of the Organism.
	 */
	public void setHeatHappiness(Happiness heatHappiness)
	{
		this.heatHappiness = heatHappiness;
	}
	
	public void updateHappiness(long gameTicks)
	{
		if (this.ageTicks < (10 * GameManager.FRAMES_PER_SECOND))
		{
			return;
		}
		
		final Happiness newHappiness = getHappiness();
		
		if (this.lastHappinessLevel != newHappiness)
		{
			this.reportHappinessAt = gameTicks;
			this.lastHappinessLevel = newHappiness;
			this.happinessChangePending = true;
		}
	}
	
	/**
	 * Calculates the overall happiness of the Organism.
	 * @return The overall happiness of the Organism.
	 */
	public Happiness getHappiness()
	{
		// If hunger is greater than 75, the Organism will be unhappy.
		// Otherwise, it depends on the heatHappiness and crowdHappiness levels.
		if (this.hunger >= 75)
		{
			return Happiness.Unhappy;
		}
		
		return this.crowdHappiness;
		
		// TODO (2013-05-10): Heat happiness is currently disabled. Uncomment this
		// once heat happiness has been implemented.
//		else if (this.crowdHappiness == Happiness.Happy)
//		{
//			if (this.heatHappiness == Happiness.Happy)
//			{
//				return Happiness.Happy;
//			}
//			else
//			{
//				return Happiness.Neutral;
//			}
//		}
//		else if (this.crowdHappiness == Happiness.Neutral)
//		{
//			if (this.heatHappiness == Happiness.Unhappy)
//			{
//				return Happiness.Unhappy;
//			}
//			else
//			{
//				return Happiness.Neutral;
//			}
//		}
//		else
//		{
//			if (this.heatHappiness == Happiness.Unhappy)
//			{
//				return Happiness.Unhappy;
//			}
//			else 
//			{
//				return Happiness.Neutral;
//			}
//		}
	}
	
	
	/**
	 * Specifies whether the organism is ready to report that its happiness
	 * has changed.
	 * @param gameTicks
	 * @return Whether the organism is ready to report that its happiness
	 * has changed.
	 */
	public boolean isReadyToReportHappiness(long gameTicks)
	{
		if (this.ageTicks < (10 * GameManager.FRAMES_PER_SECOND))
		{
			return false;
		}
		
		if (this.reportHappinessAt < gameTicks && this.happinessChangePending)
		{
			this.happinessChangePending = false;
			return true;
		}
		else if ((this.reportHappinessAt + 10 * GameManager.FRAMES_PER_SECOND) < gameTicks)
		{
			this.reportHappinessAt = gameTicks + GameManager.FRAMES_PER_SECOND * 120;
			return true;
		}
		else
		{
			return false;
		}
	}
}
