package com.divergentthoughtsgames.colonies.logic;


import junit.framework.TestCase;

public class GridPositionTests extends TestCase
{
	private GridPosition gp;
	
	@Override
	public void setUp()
	{
		this.gp = new GridPosition(5, 4);
	}
	
	@Override
	public void tearDown()
	{
		this.gp = null;
	}
	
	public void testGridPosition_invalid()
	{
		try {
			new GridPosition(-5, 4);
			fail("Expected exception, but none encountered.");
		} catch (Exception e) {}
	}

	public void testGetRow()
	{
		assertEquals(this.gp.getRow(), 5);
	}

	public void testGetColumn()
	{
		assertEquals(this.gp.getColumn(), 4);
	}

	public void testEquals()
	{
		GridPosition g1 = new GridPosition(1, 3);
		GridPosition g2 = new GridPosition(1, 3);
		assertEquals(g1, g2);
	}
	
	public void testEquals_false()
	{
		GridPosition g1 = new GridPosition(1, 3);
		GridPosition g2 = new GridPosition(3, 1);
		assertFalse(g1.equals(g2));
	}
	
	public void testHashCode()
	{
		GridPosition g1 = new GridPosition(1, 3);
		GridPosition g2 = new GridPosition(1, 3);
		assertTrue(g1.hashCode() == g2.hashCode());
	}
	
	public void testHashCode_false()
	{
		GridPosition g1 = new GridPosition(1, 3);
		GridPosition g2 = new GridPosition(3, 1);
		assertFalse(g1.hashCode() == g2.hashCode());
	}
}
