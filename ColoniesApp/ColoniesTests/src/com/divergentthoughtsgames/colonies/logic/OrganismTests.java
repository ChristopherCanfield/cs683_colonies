package com.divergentthoughtsgames.colonies.logic;

import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedData;
import com.divergentthoughtsgames.colonies.event.OrganismDiedEvent;
import com.divergentthoughtsgames.colonies.logic.attributes.Diet;
import com.divergentthoughtsgames.colonies.logic.attributes.FoodType;
import com.divergentthoughtsgames.colonies.logic.attributes.Frequency;
import com.divergentthoughtsgames.colonies.logic.attributes.Preference;

import junit.framework.TestCase;

public class OrganismTests extends TestCase
{
	private StaticAttributes sa;
	private Organism o;
	private EventManager em;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		this.sa = new StaticAttributes(100, 50000L, FoodType.Meat, 
				Diet.Carnivore, Preference.Like, Preference.Dislike, Frequency.Frequent, "Test");
		this.em = new EventManager();
		this.o = new Organism(this.sa, this.em, new WorldGrid(), new GridPosition(1, 2), 10);
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		
		this.o = null;
		this.em = null;
		this.sa = null;
	}

	public void testOrganism_invalidGridPosition()
	{
		try {
			new Organism(this.sa, this.em, new WorldGrid(), null, 10);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testOrganism_invalidStaticAttributes()
	{
		try {
			new Organism(null, this.em, new WorldGrid(), new GridPosition(10, 10), 10);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testOrganism_invalidOrganismManager()
	{
		try {
			new Organism(this.sa, null, new WorldGrid(), new GridPosition(10, 10), 10);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}
	
	public void testOrganism_invalidGameTick()
	{
		try {
			new Organism(this.sa, this.em, new WorldGrid(), new GridPosition(10, 10), -10);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testGetStaticAttributes()
	{
		assertTrue(this.o.getStaticAttributes() != null);
	}

	public void testGetDynamicAttributes()
	{
		assertTrue(this.o.getDynamicAttributes() != null);
	}

	public void testIsReadyToReproduce()
	{
		assertFalse(this.o.isReadyToReproduce(10));
	}
	
	public void testIsReadToReproduce_invalidGameTicks()
	{
		try {
			this.o.isReadyToReproduce(-1);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testUpdate()
	{
		this.o.update(11);
	}
	
	public void testUpdate_invalidGameTicks()
	{
		try {
			this.o.update(-1);
			fail("Exception expected, but none encountered.");
		} catch (Exception e) {}
	}

	public void testNotifyEvent()
	{
		this.o.notify(new OrganismDiedEvent(null));
	}

	public void testGetId()
	{
		assertTrue(this.o.getId() >= Long.MIN_VALUE && this.o.getId() <= Long.MAX_VALUE);
	}
}
