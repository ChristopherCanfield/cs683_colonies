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
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.util.Log;

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.logic.attributes.Diet;
import com.divergentthoughtsgames.colonies.logic.attributes.FoodType;
import com.divergentthoughtsgames.colonies.logic.attributes.Frequency;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;
import com.divergentthoughtsgames.colonies.util.UiUtilities;


/**
 * Unchanging attributes for an Organism. Organisms are
 * born with these attributes.
 * @author Christopher D Canfield
 */
public class StaticAttributes implements Serializable
{
	private static final long serialVersionUID = -3204819176161427854L;
	
	/** The new colony initial organism count key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_COUNT = "newColonyCount";
	/** The new colony crowd preference key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_CROWD_PREF = "newColonyCrowdPref";
	/** The new colony health key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_HEALTH = "newColonyHealth";
	/** The new colony heat preference3 key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_HEAT_PREF = "newColonyHeatPref";
	/** The new colony max age key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_MAX_AGE = "newColonyMaxAge";
	/** The new colony name key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_NAME = "newColonyName";
	/** The new colony reproduction frequency key, for use with a bundle and the fromBundle static method. **/
	public static final String NEW_COLONY_REPRODUCTION_FREQ = "newColonyReproductionFreq";
	
	/** The default diet of an Organism. **/
	public static final Diet DEFAULT_DIET = Diet.Herbivore;
	/** The default body type (food type) of an Organism. **/
	public static final FoodType DEFAULT_BODY_TYPE = FoodType.Meat;
	
	// The organism's maximum health.
	private final int maxHealth;
	// The type of food that this organism is.
	private final FoodType bodyType;
	// The food type eaten by the organism.
	private final Diet diet;
	// The maximum age of the organism, in game ticks.
	private final long maxAgeTicks;
	// The organism's preference for heat.
	private final Preference heatPreference;
	// The organism's preference for crowds.
	private final Preference crowdPreference;
	// The frequency of the organism's reproduction.
	private final Frequency reproductiveFrequency;
	// The colony's name.
	private final String colonyName;
	// The colony's color.
	private final int colonyColor;
	
	/**
	 * Creates a new StaticAttributes object from a bundle. The bundle should have the
	 * following items:
	 * 	- NEW_COLONY_CROWD_PREF (int)
	 *  - NEW_COLONY_HEALTH (int)
	 *  - NEW_COLONY_HEAT_PREF (int)
	 *  - NEW_COLONY_MAX_AGE (int)
	 * 	- NEW_COLONY_NAME (String)
	 * 	- NEW_COLONY_REPRODUCTION_FREQ (int)
	 * @param bundle A bundle matching the requirements listed above.
	 * @return A new StaticAttributes object.
	 */
	public static StaticAttributes fromBundle(Bundle bundle)
	{
		if (bundle == null)
			throw new IllegalArgumentException("Argument 'bundle' cannot be null");
		
		Preference crowdPref = Preference.valueOf(bundle.getInt(NEW_COLONY_CROWD_PREF));
		Preference heatPref = Preference.valueOf(bundle.getInt(NEW_COLONY_HEAT_PREF));
		Frequency reproductiveFreq = Frequency.valueOf(bundle.getInt(NEW_COLONY_REPRODUCTION_FREQ));
		int maxHealth = getMaxHealthFromBundle(bundle);
		long maxAge = getMaxAgeFromBundle(bundle);
		
		return new StaticAttributes(maxHealth, maxAge, DEFAULT_BODY_TYPE, DEFAULT_DIET, 
				heatPref, crowdPref, reproductiveFreq, bundle.getString(NEW_COLONY_NAME));
	}
	
	/**
	 * Returns 50, 75 or 100 based on the provided bundle integer value.
	 * The value should be in a range from 1 through 3, with 1 representing the
	 * least amount of health.
	 * @param bundle A Bundle containing the NEW_COLONY_HEALTH field.
	 * @return The max health value.
	 */
	private static int getMaxHealthFromBundle(Bundle bundle)
	{
		int bundleVal = bundle.getInt(NEW_COLONY_HEALTH);
		return (bundleVal <= 2) ? 50 : (bundleVal == 3) ? 75 : 100;
	}
	
	/**
	 * Returns an age length, in milli based on the provided bundle integer value.
	 * The value should be in a range from 1 through 5, with 1 representing the
	 * least amount of health.
	 * @param bundle A Bundle containing the NEW_COLONY_HEALTH field.
	 * @return The max health value.
	 */
	private static long getMaxAgeFromBundle(Bundle bundle)
	{
		final long SHORTEST_AGE = GameManager.FRAMES_PER_SECOND * (1 * 60);
		final long SHORT_AGE = GameManager.FRAMES_PER_SECOND * (2 * 60);
		final long MIDDLE_AGE = GameManager.FRAMES_PER_SECOND * (3 * 60);
		final long LONG_AGE = GameManager.FRAMES_PER_SECOND * (4 * 60);
		final long LONGEST_AGE = GameManager.FRAMES_PER_SECOND * (5 * 60);
		
		int bundleVal = bundle.getInt(NEW_COLONY_MAX_AGE);
		if (bundleVal <= 1)
		{
			return SHORTEST_AGE;
		}
		else if (bundleVal == 2)
		{
			return SHORT_AGE;
		}
		else if (bundleVal == 3)
		{
			return MIDDLE_AGE;
		}
		else if (bundleVal == 4)
		{
			return LONG_AGE;
		}
		else
		{
			return LONGEST_AGE;
		}
	}
	
	/**
	 * Applies a factor to the max age value in the StaticAttributes parameter, and
	 * then returns a new StateAttributes object. The applied factor is in the range
	 * of -factor...+factor.
	 * @param sa
	 * @param factor A factor range percent amount, from 0 to 100. For example, to get a max age back that is
	 * within the 90% to 110% range of the original max age, pass in 10.
	 * @return
	 */
	static StaticAttributes applyFactorToMaxAge(StaticAttributes sa, int factor)
	{
		Random rand = new Random();
		int plusMinus = (rand.nextInt(2) == 1) ? -1 : 1;
//		final float adjustedFactor = 1 + plusMinus * factor;
		
		final float adjustedFactor = 1 + ((rand.nextInt(factor) * plusMinus) / 100.f);
		Log.d("StaticAttributes", "Factor: " + adjustedFactor);
				
		return new StaticAttributes(sa.maxHealth,
				(long)(sa.maxAgeTicks * adjustedFactor),
				sa.bodyType,
				sa.diet,
				sa.heatPreference,
				sa.crowdPreference,
				sa.reproductiveFrequency,
				sa.colonyName,
				sa.colonyColor);
	}
	
	/**
	 * Creates a new StaticAttributes instance.
	 * @param maxHealth The maximum health for the Organism.
	 * @param maxAgeTicks The maximum age for the Organism, in game ticks.
	 * @param bodyType The type of body that this Organism has.
	 * @param diet The type of diet that this Organism requires.
	 * @param heatPreference The Organism's preference for heat.
	 * @param crowdPreference The Organism's preference for crowds.
	 * @param reproductiveFrequency The Organism's reproductive frequency.
	 * @param colonyName The colony's name.
	 * @throws IllegalArgumentException when maxHealth or maxAgeTicks are less than 
	 * or equal to zero, or colonyName is null or "".
	 */
	StaticAttributes(int maxHealth, long maxAgeTicks, FoodType bodyType, 
			Diet diet, Preference heatPreference, Preference crowdPreference,
			Frequency reproductiveFrequency, String colonyName)
	{
		if (maxHealth <= 0)
			throw new IllegalArgumentException("Argument 'maxHealth' cannot be negative. Found: " + maxHealth);
		if (maxAgeTicks <= 0)
			throw new IllegalArgumentException("Argument 'maxAgeTicks' cannot be negative. Found: " + maxAgeTicks);
		if (colonyName == null)
			throw new IllegalArgumentException("Argument 'colonyName' cannot be null.");
		if (colonyName.equals(""))
			throw new IllegalArgumentException("Argument 'colonyName' cannot be empty.");

		this.maxHealth = maxHealth;
		this.maxAgeTicks = maxAgeTicks;
		this.bodyType = bodyType;
		this.diet = diet;
		this.heatPreference = heatPreference;
		this.crowdPreference = crowdPreference;
		this.reproductiveFrequency = reproductiveFrequency;
		this.colonyName = colonyName;
		this.colonyColor = UiUtilities.getRandomColor();
	}
	
	/**
	 * Creates a new StaticAttributes instance.
	 * @param maxHealth The maximum health for the Organism.
	 * @param maxAgeTicks The maximum age for the Organism, in game ticks.
	 * @param bodyType The type of body that this Organism has.
	 * @param diet The type of diet that this Organism requires.
	 * @param heatPreference The Organism's preference for heat.
	 * @param crowdPreference The Organism's preference for crowds.
	 * @param reproductiveFrequency The Organism's reproductive frequency.
	 * @param colonyName The colony's name.
	 * @param colonyColor The colony's color, as an int.
	 * @throws IllegalArgumentException when maxHealth or maxAgeTicks are less than 
	 * or equal to zero, or colonyName is null or "".
	 */
	StaticAttributes(int maxHealth, long maxAgeTicks, FoodType bodyType, 
			Diet diet, Preference heatPreference, Preference crowdPreference,
			Frequency reproductiveFrequency, String colonyName, int colonyColor)
	{
		if (maxHealth <= 0)
			throw new IllegalArgumentException("Argument 'maxHealth' cannot be negative. Found: " + maxHealth);
		if (maxAgeTicks <= 0)
			throw new IllegalArgumentException("Argument 'maxAgeTicks' cannot be negative. Found: " + maxAgeTicks);
		if (colonyName == null)
			throw new IllegalArgumentException("Argument 'colonyName' cannot be null.");
		if (colonyName.equals(""))
			throw new IllegalArgumentException("Argument 'colonyName' cannot be empty.");

		this.maxHealth = maxHealth;
		this.maxAgeTicks = maxAgeTicks;
		this.bodyType = bodyType;
		this.diet = diet;
		this.heatPreference = heatPreference;
		this.crowdPreference = crowdPreference;
		this.reproductiveFrequency = reproductiveFrequency;
		this.colonyName = colonyName;
		this.colonyColor = colonyColor;
	}
	
	public StaticAttributes(StaticAttributes s)
	{
		this.maxHealth = s.maxHealth;
		this.maxAgeTicks = s.maxAgeTicks;
		this.bodyType = s.bodyType;
		this.diet = s.diet;
		this.heatPreference = s.heatPreference;
		this.crowdPreference = s.crowdPreference;
		this.reproductiveFrequency = s.reproductiveFrequency;
		this.colonyName = s.colonyName;
		this.colonyColor = s.colonyColor;
	}
	

	
	/**
	 * Calculates the averages for the StaticAttributes contained within
	 * each organism in the organisms List. For the enum-type attributes,
	 * the item with the highest count is returned, since an average isn't possible.
	 * @param organisms The Organisms for which averages will be calculated.
	 * @return A StaticAttributes object calculated according to the description above.
	 */
	static StaticAttributes calculateAverage(List<Organism> organisms)
	{
		int totalMaxHealth = 0;
		long totalMaxAge = 0;
		int[] dietPrefCounts = new int[5];
		int[] heatPrefCounts = new int[5];
		int[] crowdPrefCounts = new int[5];
		int[] reproductiveFreqCounts = new int[4];
		
		for (Organism org : organisms)
		{
			totalMaxHealth = org.getStaticAttributes().getMaxHealth();
			totalMaxAge = org.getStaticAttributes().getMaxAgeTicks();
			
			dietPrefCounts[org.getStaticAttributes().getDiet().ordinal()]++;
			heatPrefCounts[org.getStaticAttributes().getHeatPreference().ordinal()]++;
			crowdPrefCounts[org.getStaticAttributes().getCrowdPreference().ordinal()]++;
			reproductiveFreqCounts[org.getStaticAttributes().getReproductiveFrequency().ordinal()]++;
		}
		
		Diet dietPref = Diet.values()[getIndexWithLargestCount(dietPrefCounts)];
		Preference[] prefs = Preference.values();
		Preference heatPref = prefs[getIndexWithLargestCount(heatPrefCounts)];
		Preference crowdPref = prefs[getIndexWithLargestCount(crowdPrefCounts)];
		Frequency reproductiveFreq = Frequency.values()[getIndexWithLargestCount(reproductiveFreqCounts)];
		
		return new StaticAttributes(totalMaxHealth / organisms.size(), totalMaxAge / organisms.size(), 
				organisms.get(0).getStaticAttributes().getBodyType(), dietPref, heatPref, crowdPref, 
				reproductiveFreq, "All");
	}
	
	/**
	 * Returns the index of the element that has the largest count.
	 * @param counts An int array of counts.
	 * @return The index of the element that has the largest count.
	 */
	private static int getIndexWithLargestCount(int[] counts)
	{
		int indexLargest = 0;
		
		for (int i = 0; i < counts.length; ++i)
		{
			if (counts[i] > counts[indexLargest])
			{
				indexLargest = i;
			}
		}
		
		return indexLargest;
	}
	
	/**
	 * Gets the colony's name.
	 * @return The colony's name.
	 */
	public String getColonyName()
	{
		return this.colonyName;
	}
	
	/**
	 * Gets the organism's color.
	 * @return The organism's color.
	 */
	public int getColor()
	{
		return this.colonyColor;
	}
	
	/**
	 * Gets the Organism's maximum health.
	 * @return The Organism's maximum health.
	 */
	public int getMaxHealth()
	{
		return this.maxHealth;
	}
	
	/**
	 * Gets the Organism's maximum age, in ticks.
	 * @return The Organism's maximum age, in ticks.
	 */
	public long getMaxAgeTicks()
	{
		return this.maxAgeTicks;
	}
	
	/**
	 * Gets the Organism's body type.
	 * @return The Organism's body type.
	 */
	public FoodType getBodyType()
	{
		return this.bodyType;
	}
	
	/**
	 * Gets the Organism's diet.
	 * @return The Organism's diet.
	 */
	public Diet getDiet()
	{
		return this.diet;
	}
	
	/**
	 * Gets the Organism's heat preference.
	 * @return The Organism's heat preference.
	 */
	public Preference getHeatPreference()
	{
		return this.heatPreference;
	}
	
	/**
	 * Gets the Organism's crowd preference.
	 * @return The Organism's crowd preference.
	 */
	public Preference getCrowdPreference()
	{
		return this.crowdPreference;
	}
	
	/**
	 * Gets the Organism's reproductive frequency.
	 * @return The Organism's reproductive frequency.
	 */
	public Frequency getReproductiveFrequency()
	{
		return this.reproductiveFrequency;
	}
	
	/**
	 * Gets the reproductive timeout, in game ticks.
	 * @param reproductiveFrequency The frequency of reproduction.
	 * @return The reproductive timeout, in game ticks.
	 */
	public static int getReproductiveTimeout(Frequency reproductiveFrequency)
	{		
		if (reproductiveFrequency == Frequency.VeryFrequent)
		{
			return 300;
		}
		else if (reproductiveFrequency == Frequency.Frequent)
		{
			return 600;
		}
		else if (reproductiveFrequency == Frequency.Infrequent)
		{
			return 900;
		}
		else if (reproductiveFrequency == Frequency.VeryInfrequent)
		{
			return 1200;
		}
		else
		{
			throw new IllegalArgumentException("Invalid reproductiveFrequency provided to StaticAttributes.getReproductiveFrequency");
		}
	}
	
	/**
	 * Gets the preferred number of neighbors for a given Crowd Preference.
	 * @return The preferred number of neighbors for a given Crowd Preference.
	 */
	public static int getCrowdPreferenceNumber(Preference crowdPreference)
	{
		if (crowdPreference == Preference.Love)
		{
			return 5;
		}
		else if (crowdPreference == Preference.Like)
		{
			return 4;
		}
		else if (crowdPreference == Preference.None)
		{
			return 3;
		}
		else if (crowdPreference == Preference.Dislike)
		{
			return 2;
		}
		else if (crowdPreference == Preference.Hate)
		{
			return 1;
		}
		else
		{
			throw new IllegalArgumentException("Invalid crowdPreference provided to StaticAttributes.getCrowdPreferenceNumber");
		}
	}
}
