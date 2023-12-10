import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.money.Coin;
import edu.uwm.cs351.money.Container;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Spy.Statistic;
import edu.uwm.cs351.money.Type;

/**
 * Common setup for testing containers.
 * Do not attempt to run this test.
 */
abstract public class TestContainer extends LockedTestCase {
	protected Coin p1, n1, d1, q1, h1, s1, p2, n2, d2, q2, h2, s2;
	
	protected Container self;
	
	@Override
	public void setUp() {
		p1 = Spy.newCoin(Type.PENNY);
		n1 = Spy.newCoin(Type.NICKEL);
		d1 = Spy.newCoin(Type.DIME);
		q1 = Spy.newCoin(Type.QUARTER);
		h1 = Spy.newCoin(Type.HALFDOLLAR);
		s1 = Spy.newCoin(Type.DOLLAR);
		p2 = Spy.newCoin(Type.PENNY);
		n2 = Spy.newCoin(Type.NICKEL);
		d2 = Spy.newCoin(Type.DIME);
		q2 = Spy.newCoin(Type.QUARTER);
		h2 = Spy.newCoin(Type.HALFDOLLAR);
		s2 = Spy.newCoin(Type.DOLLAR);
		Spy.clearStats();
		try {
			assert 1/self.size() == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (NullPointerException ex) {
			// muffle exception
		}
		initializeContainer();
	}
	
	protected abstract void initializeContainer();
	
	protected void assertException(Class<?> excClass, Runnable producer) {
		try {
			producer.run();
			assertFalse("Should have thrown an exception, not returned.",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Check that the wellFormed for the given container returns what is expected,
	 * and that the number of calls to reports match what the result
	 * (0 if the data structure should be deemed well formed,
	 * 1 if the data structure should be deemed badly formed).
	 * @param expected what result wellFormed should return
	 * @param c container to check
	 * This container should have been created by the {@link Spy} class.
	 */
	protected void assertWellFormed(boolean expected, Container c) {
		Spy.clearStats();
		boolean result = Spy.wellFormed(c);
		int reports = Spy.getStat(Statistic.REPORT);
		if (expected == result) {
			assertEquals("Number of times 'report' was called is wrong",expected?0:1, reports);
		} else if (expected == false) {
			if (reports > 0) {
				assertEquals("wellFormed returned true even though problems were reported",false, result);
			} else {
				assertEquals("wellFormed returned true but should have noticed a problem", false, result);
			}
		} else {
			assertEquals("wellFormed returned false incorectly with report " + Spy.getReport(), true, result);
		}
	}

	/**
	 * Convert the result into a string.
	 * @param supp function to call to get a result which is converted to a string.
	 * if the function throws an exception, then the simple name of the exception is returned
	 * @return
	 */
	protected String asString(Supplier<?> supp) {
		try {
			return ""+supp.get();
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	/**
	 * Check to see if a number is at least as big as expected
	 * @param expected expected number
	 * @param result check that should be at least the expected value
	 */
	protected void assertAtLeast(int expected, int result) {
		if (expected > result) {
			assertEquals(expected, result);
		}
	}
}
