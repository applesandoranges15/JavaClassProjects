package edu.uwm.cs351.money;

public class Bank {

	private int accountBalance = 200;
	
	private Bank() {}
	
	private static final Bank instance = new Bank();
	
	/**
	 * Return the default bank
	 * @return a bank
	 */
	public static Bank getInstance() {
		return instance;
	}
	
	/**
	 * Withdraw a coin from the bank
	 * @param ty type of coin to request, must not be null
	 * @throws IllegalStateException if there are insufficient funds
	 * @return coin of specified type
	 */
	public Coin withdraw(Type ty) throws IllegalStateException {
		if (ty.getValue() > accountBalance) {
			throw new IllegalStateException("Insufficient funds " + accountBalance + " for a withdrawal of " + ty.getValue());
		}
		accountBalance -= ty.getValue();
		return Mint.SanFrancisco.newCoin(ty);
	}
}
