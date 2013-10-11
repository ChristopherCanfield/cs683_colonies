package com.divergentthoughtsgames.colonies.logic;

import com.divergentthoughtsgames.colonies.logic.attributes.HeatLevel;

import junit.framework.TestCase;

public class GridAttributesTests extends TestCase
{
	private GridAttributes ga;
	
	@Override
	public void setUp()
	{
		this.ga = new GridAttributes(HeatLevel.Cold, 2);
	}
	
	@Override
	public void tearDown()
	{
		this.ga = null;
	}

	public void testGridAttributes_valid()
	{
		assertNotNull(new GridAttributes(HeatLevel.Cold, 2));
	}
	
	public void testGridAttributes_valid2()
	{
		assertNotNull(new GridAttributes(HeatLevel.VeryHot, 0));
	}

	public void testGetHeatLevel()
	{
		GridAttributes ga = new GridAttributes(HeatLevel.Cold, 2);
		assertTrue(ga.getHeatLevel() == HeatLevel.Cold);
	}

	public void testSetHeatLevel()
	{
		this.ga.setHeatLevel(HeatLevel.VeryCold);
		assertTrue(this.ga.getHeatLevel() == HeatLevel.VeryCold);
	}

	public void testGetNeighborsCount()
	{
		assertTrue(this.ga.getNeighborsCount() == 2);
	}

	public void testSetNeighborsCount_valid()
	{
		this.ga.setNeighborsCount(0);
	}
	
	public void testSetNeighborsCount_invalid()
	{
		try 
		{
			this.ga.setNeighborsCount(-1);
			fail("Expected exception, but none encountered.");
		}
		catch (Exception e) {}
	}

}
