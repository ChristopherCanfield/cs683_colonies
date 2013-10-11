package com.divergentthoughtsgames.colonies.logic;

import com.divergentthoughtsgames.colonies.logic.attributes.Action;
import com.divergentthoughtsgames.colonies.logic.attributes.Diet;
import com.divergentthoughtsgames.colonies.logic.attributes.FoodType;
import com.divergentthoughtsgames.colonies.logic.attributes.Frequency;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;

import junit.framework.TestCase;

public class DynamicAttributesTests extends TestCase
{
	private DynamicAttributes da;
	private StaticAttributes sa;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		this.sa = new StaticAttributes(100, 50000L, FoodType.Meat, 
				Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
		this.da = new DynamicAttributes(this.sa, 100);
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		this.sa = null;
		this.da = null;
	}

	public void testDynamicAttributes_invalidGameTicks()
	{
		try {
			new DynamicAttributes(this.sa, -100);
			fail("Exception expected, but none encountered");
		} catch (Exception e) {}
		
	}

	public void testGetHealth()
	{
		assertEquals((int)this.da.getHealth(), this.sa.getMaxHealth());
	}

	public void testSetHealth_valid()
	{
		this.da.setHealth(50);
		assertEquals(this.da.getHealth(), 50.f);
	}
	
	public void testSetHealth_invalid()
	{
		this.da.setHealth(-100);
		assertEquals(this.da.getHealth(), 0.f);
	}

	public void testGetHunger()
	{
		assertEquals(this.da.getHunger(), 0.f);
	}

	public void testSetHunger_valid()
	{
		this.da.setHunger(50);
		assertEquals(this.da.getHunger(), 50.f);
	}
	
	public void testSetHunger_invalid()
	{
		this.da.setHunger(-100);
		assertEquals(this.da.getHunger(), 0.f);
	}

	public void testGetAgeTicks()
	{
		assertEquals(this.da.getAgeTicks(), 0);
	}

	public void testGetLastReproducedTicks()
	{
		assertEquals(this.da.getLastReproducedTicks(), 100);
	}

	public void testSetLastReproducedTicks_valid()
	{
		this.da.setLastReproducedTicks(300);
		assertEquals(this.da.getLastReproducedTicks(), 300);
	}
	
	public void testSetLastReproducedTicks_invalid()
	{
		try {
			this.da.setLastReproducedTicks(-1);
			fail("Expected exception, but none encountered.");
		} catch(Exception e) {}
	}

	public void testGetCurrentAction()
	{
		assertTrue(this.da.getCurrentAction() == Action.Nothing);
	}

	public void testSetCurrentAction_valid()
	{
		this.da.setCurrentAction(Action.Eating, 300);
		assertTrue(this.da.getCurrentAction() == Action.Eating);
	}

	public void testSetCurrentAction_invalid()
	{
		try {
			this.da.setCurrentAction(Action.Eating, -1);
			fail("Expected exception, but none encountered");
		} catch (Exception e) {}
	}
	
	public void testGetLastActionChangedTicks()
	{
		assertEquals(this.da.getLastActionChangedTicks(), 100);
	}

	//TODO (2013-04-09): implement these.
//	public void testGetCrowdHappiness()
//	{
//		)
//	}
//
//	public void testSetCrowdHappiness()
//	{
//		fail("Not yet implemented");
//	}
//
//	public void testGetHeatHappiness()
//	{
//		fail("Not yet implemented");
//	}
//
//	public void testSetHeatHappiness()
//	{
//		fail("Not yet implemented");
//	}
//
//	public void testGetHappiness()
//	{
//		fail("Not yet implemented");
//	}

}
