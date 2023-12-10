package edu.uwm.cs351.money;

/**
 * Something that can create legal coins.
 * This class is private so that clients cannot mint their own coins.
 */
class Mint {
	static final int PRODUCTION = 10;
	
	private String location;
	private Pipeline[] pipelines;
	private int serial = 0;
	
	private Mint(String loc) {
		this.location = loc;
		pipelines = new Pipeline[Type.values().length];
		for (Type t : Type.values()) {
			pipelines[t.ordinal()] = new Pipeline();
		}
	}
	
	/**
	 * Get a fresh new coin of the given type from the mint.
	 * @param t type to get a coin for, must not be null
	 * @return new coin of the given type
	 */
	Coin newCoin(Type t) {
		Pipeline pl = pipelines[t.ordinal()];
		if (pl.isEmpty()) {
			produce(pl,t);
		}
		return pl.remove();
	}
	
	private void produce(Pipeline p, Type ty) {
		for (int i=0; i < PRODUCTION; ++i) {
			p.add(new Coin(ty, this, ++serial));
		}
	}
	
	@Override //implementation
	public String toString() {
		return String.valueOf(location.charAt(0));
	}
	
	static Mint Denver = new Mint("Denver");
	static Mint Philadelphia = new Mint("Philadelphia");
	static Mint SanFrancisco = new Mint("San Francisco");
}
