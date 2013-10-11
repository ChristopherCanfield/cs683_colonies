package com.divergentthoughtsgames.colonies.logic;

import java.util.ArrayList;

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedData;
import com.divergentthoughtsgames.colonies.logic.attributes.Diet;
import com.divergentthoughtsgames.colonies.logic.attributes.FoodType;
import com.divergentthoughtsgames.colonies.logic.attributes.Frequency;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;

import junit.framework.TestCase;

public class OrganismManagerTests extends TestCase
{
	private OrganismManager om;
	private NewColonyPlacedData ncpd;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		StaticAttributes sa = new StaticAttributes(100, 50000L, FoodType.Meat, 
				Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
		this.ncpd = new NewColonyPlacedData(sa, new GridPosition(2, 2), 2, 5);
		this.om = new OrganismManager(this.ncpd, new WorldGrid(), new GameManager(new ArrayList<String>()));
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		this.om = null;
		this.ncpd = null;
	}

	public void testOrganismManager_invalidStaticAttributes()
	{
		try {
			new OrganismManager(null, new WorldGrid(), new GameManager(new ArrayList<String>()));
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testOrganismManager_invalidWorldGrid()
	{
		try {
			new OrganismManager(this.ncpd, null, new GameManager(new ArrayList<String>()));
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testOrganismManager_invalidGameManager()
	{
		try {
			new OrganismManager(this.ncpd, new WorldGrid(), null);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testUpdate()
	{
		this.om.update(10);
	}
	
	public void testUpdate_invalidGameTick()
	{
		try {
			this.om.update(-1);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testGetId()
	{
		assertTrue(this.om.getId() >= Long.MIN_VALUE && this.om.getId() <= Long.MAX_VALUE);
	}
}
