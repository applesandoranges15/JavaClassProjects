package edu.uwm.cs351.money;

import java.util.NoSuchElementException;

/**
 * An endogenous container of coins.
 */
public interface Container {
	/**
	 * Determine whether this container is empty.
	 * @return whether the container has no coins
	 */
	public boolean isEmpty();
	
	/**
	 * Return the number of coins in the container
	 * @return number of coins in container
	 */
	public int size();
	
	/**
	 * Indicate whether the given coin can be added
	 * @param c coin potentially to add, may be null
	 * @return whether this coin could be added.
	 */
	public boolean canAdd(Coin c);
	
	/**
	 * Add a coin to the container, and we will then own it.
	 * @param c coin to add
	 * @throw IllegalArgumentException if the coin cannot be added.
	 */
	public void add(Coin c);
	
	/**
	 * Remove and return a coin from this container, no longer owning it.
	 * @return coin, will not be null, and not owned by any container
	 * @throws NoSuchElementException if the container is empty
	 */
	public Coin remove() throws NoSuchElementException;
	
}
