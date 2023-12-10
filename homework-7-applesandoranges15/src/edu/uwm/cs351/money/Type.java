package edu.uwm.cs351.money;

/**
 * An enumeration of the different kinds of coins we can make.
 * Each type has a value and a size.
 */
public enum Type {
	PENNY(1,2), NICKEL(5,3), DIME(10,1), QUARTER(25,4), HALFDOLLAR(50,6), DOLLAR(100,5);
	private final int value;
	private final int size;
	
	private Type(int v, int s) {
		value = v;
		size = s;
	}
	
	/**
	 * Return the value in cents of the coin type.
	 * @return value in cents
	 */
	public int getValue() { return value; }
	
	/**
	 * Return the relative size of the coin with respect to other coins.
	 * @return indication of relative size (between 1 and 10)
 	 */
	public int getSize() { return size; }
}
