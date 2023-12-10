package edu.uwm.cs351.money;

public class Coin {

	// default access because accessible only to classes in the "money" subpackage

	final Type type;
	final Mint mint;
	final int serial;
	
	Coin next;
	Container owner;
	
	/** Create a "free" coin (not owned by any container)
	 * @param t given type
	 * @param m creating mint
	 * @param num serial number, should be unique
	 */
	Coin(Type t, Mint m, int num) {
		type = t;
		mint = m;
		serial = num;
		next = null;
		owner = null;
	}
	
	@Override // implementation
	public String toString() {
		return type.toString();
	}
	
	/**
	 * Return a string of the form
	 * <type>-<mint>-<serial#>
	 * @return longer descriptive string
	 */
	public String getLongDecription() {
		return type + "-" + (mint == null ? "X" : mint.toString()) + "-" + serial;
	}
	
	/**
	 * Return the type of this coin.
	 * @return type of this coin
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Return true if this coin is owned by a container
	 * @return whether this coin is already in a container
	 */
	public boolean isOwned() {
		return owner != null;
	}
}
