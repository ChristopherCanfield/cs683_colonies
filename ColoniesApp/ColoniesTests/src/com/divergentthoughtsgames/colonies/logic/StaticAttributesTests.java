package com.divergentthoughtsgames.colonies.logic;

import com.divergentthoughtsgames.colonies.logic.attributes.Diet;
import com.divergentthoughtsgames.colonies.logic.attributes.FoodType;
import com.divergentthoughtsgames.colonies.logic.attributes.Frequency;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;

import junit.framework.TestCase;

public class StaticAttributesTests extends TestCase
{
	private StaticAttributes sa;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.sa = new StaticAttributes(100, 50000L, FoodType.Meat, 
				Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		this.sa = null;
	}

	public void testStaticAttributes_invalidMaxHealth()
	{
		try {
			new StaticAttributes(0, 50000L, FoodType.Meat, 
					Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
			fail("Expected exception, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testStaticAttributes_invalidMaxAgeTicks()
	{
		try {
			new StaticAttributes(100, 0, FoodType.Meat, 
					Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
			fail("Expected exception, but none encountered.");
		} catch (Exception e) {}
	}

	//TODO (2013-04-09): Implement this.
//	public void testCalculateAverage()
//	{
//		fail("Not yet implemented");
//	}

	public void testGetMaxHealth()
	{
		assertEquals(this.sa.getMaxHealth(), 100);
	}

	public void testGetMaxAgeTicks()
	{
		assertEquals(this.sa.getMaxAgeTicks(), 50000L);
	}

	public void testGetBodyType()
	{
		assertTrue(this.sa.getBodyType() == FoodType.Meat);
	}

	public void testGetDiet()
	{
		assertTrue(this.sa.getDiet() == Diet.Carnivore);
	}

	public void testGetHeatPreference()
	{
		assertTrue(this.sa.getHeatPreference() == Preference.Like);
	}

	public void testGetCrowdPreference()
	{
		assertTrue(this.sa.getCrowdPreference() == Preference.Dislike);
	}

	public void testGetReproductiveFrequency()
	{
		assertTrue(this.sa.getReproductiveFrequency() == Frequency.Frequent);
	}

	public void testGetReproductiveTimeout_VeryInfrequent()
	{
		assertEquals(StaticAttributes.getReproductiveTimeout(Frequency.VeryInfrequent), 1200);
	}

	public void testGetReproductiveTimeout_Infrequent()
	{
		assertEquals(StaticAttributes.getReproductiveTimeout(Frequency.Infrequent), 900);
	}
	
	public void testGetReproductiveTimeout_Frequent()
	{
		assertEquals(StaticAttributes.getReproductiveTimeout(Frequency.Frequent), 600);
	}
	
	public void testGetReproductiveTimeout_VeryFrequent()
	{
		assertEquals(StaticAttributes.getReproductiveTimeout(Frequency.VeryFrequent), 300);
	}
	
	public void testGetCrowdPreferenceNumber_Hate()
	{
		assertEquals(StaticAttributes.getCrowdPreferenceNumber(Preference.Hate), 1);
	}
	
	public void testGetCrowdPreferenceNumber_Dislike()
	{
		assertEquals(StaticAttributes.getCrowdPreferenceNumber(Preference.Dislike), 2);
	}
	
	public void testGetCrowdPreferenceNumber_None()
	{
		assertEquals(StaticAttributes.getCrowdPreferenceNumber(Preference.None), 3);
	}
	
	public void testGetCrowdPreferenceNumber_Like()
	{
		assertEquals(StaticAttributes.getCrowdPreferenceNumber(Preference.Like), 4);
	}
	
	public void testGetCrowdPreferenceNumber_Love()
	{
		assertEquals(StaticAttributes.getCrowdPreferenceNumber(Preference.Love), 5);
	}
}
