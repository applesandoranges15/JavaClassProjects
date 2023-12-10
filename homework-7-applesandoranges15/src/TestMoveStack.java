import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import edu.uwm.cs351.MoveStack;
import edu.uwm.cs351.money.Coin;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Stack;
import edu.uwm.cs351.money.Type;
import junit.framework.TestCase;

public class TestMoveStack extends TestCase {
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

	private int numCoins, totalCoins, removes;
	
	private class InstrumentedStack extends Stack {
		@Override
		public void add(Coin c) {
			super.add(c);
			if (++numCoins > totalCoins) {
				++totalCoins;
			}
		}

		@Override
		public Coin remove() throws NoSuchElementException {
			Coin result = super.remove();
			++removes;
			if (numCoins-- < totalCoins) {
				throw new AssertionError("removed more than one coin at a time from the stacks");
			}
			return result;
		}		
	}
	
	protected Coin p1, n1, d1, q1, h1, s1, p2, n2, d2, q2, h2, s2;
	private Stack st1, st2, st3;
	
	@Override
	protected void setUp() {
		// we don't care whether assertions are on or off
		
		st1 = new InstrumentedStack();
		st2 = new InstrumentedStack();
		st3 = new InstrumentedStack();
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
		
		numCoins = totalCoins = removes = 0;
	}
	
	@Override
	protected void tearDown() {
		st1 = st2 = st3 = null;
	}
	
	
	public void test0() {
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(0, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(0, numCoins);
		assertEquals(0, totalCoins);
		assertEquals(0, removes);
	}
	
	public void test1() {
		st1.add(n1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(1, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(1, numCoins);
		assertEquals(1, totalCoins);
		assertEquals(1, removes);

		assertSame(n1, st2.remove());
	}
	
	public void test2() {
		st1.add(n1);
		st1.add(p1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(2, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(2, numCoins);
		assertEquals(2, totalCoins);
		assertEquals(3, removes);
		
		assertSame(p1, st2.remove()); --totalCoins;
		assertSame(n1, st2.remove()); --totalCoins;
	}
	
	public void test3() {
		st1.add(h1);
		st1.add(n1);
		st1.add(p1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(3, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(3, numCoins);
		assertEquals(3, totalCoins);
		assertEquals(7, removes);
		
		assertSame(p1, st2.remove()); --totalCoins;
		assertSame(n1, st2.remove()); --totalCoins;
		assertSame(h1, st2.remove()); --totalCoins;
	}
	
	public void test4() {
		st1.add(h1);
		st1.add(q1);
		st1.add(n1);
		st1.add(p1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(4, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(4, numCoins);
		assertEquals(4, totalCoins);
		assertEquals(15, removes);
		
		assertSame(p1, st2.remove()); --totalCoins;
		assertSame(n1, st2.remove()); --totalCoins;
		assertSame(q1, st2.remove()); --totalCoins;
		assertSame(h1, st2.remove()); --totalCoins;		
	}
	
	public void test5() {
		st1.add(h1);
		st1.add(q1);
		st1.add(n1);
		st1.add(p1);
		st1.add(d1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(5, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(5, numCoins);
		assertEquals(5, totalCoins);
		assertEquals(31, removes);
		
		assertSame(d1, st2.remove()); --totalCoins;
		assertSame(p1, st2.remove()); --totalCoins;
		assertSame(n1, st2.remove()); --totalCoins;
		assertSame(q1, st2.remove()); --totalCoins;
		assertSame(h1, st2.remove()); --totalCoins;		
	}
	
	public void test6() {
		st1.add(h1);
		st1.add(s1);
		st1.add(q1);
		st1.add(n1);
		st1.add(p1);
		st1.add(d1);
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(6, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(6, numCoins);
		assertEquals(6, totalCoins);
		assertEquals(63, removes);
		
		assertSame(d1, st2.remove()); --totalCoins;
		assertSame(p1, st2.remove()); --totalCoins;
		assertSame(n1, st2.remove()); --totalCoins;
		assertSame(q1, st2.remove()); --totalCoins;
		assertSame(s1, st2.remove()); --totalCoins;
		assertSame(h1, st2.remove()); --totalCoins;		
	}
	
	public void test7() {
		// This test is a little more complicated
		// because it gives lee-way for MoveStack
		// to optimize the case that coins are the same size.
		// But it does not require this optimization
		// (and the solution does not have it).
		st1.add(h1); st1.add(h2);
		st1.add(s1); st1.add(s2);
		st1.add(q1); st1.add(q2);
		st1.add(n1); st1.add(n2);
		st1.add(p1); st1.add(p2);
		st1.add(d1); st1.add(d2);
		
		MoveStack.doMove(st1, st2, st3);
		assertTrue(st1.isEmpty());
		assertEquals(12, st2.size());
		assertTrue(st3.isEmpty());
		assertEquals(12, numCoins);
		assertEquals(12, totalCoins);
		// assertEquals(4095, removes); // unoptimized
		
		Coin my[] = new Coin[] { d1, d2, p1, p2, n1, n2, q1, q2, s1, s2, h1, h2};
		List<Coin> myCoins = Arrays.asList(my);
		Coin c[] = new Coin[12];
		for (int i=0; i < 12; ++i) {
			c[i] = st2.remove();
			assertTrue(myCoins.contains(c[i]));
			--totalCoins;
		}
		assertEquals(Type.DIME, c[0].getType());
		assertEquals(Type.DIME, c[1].getType());
		assertEquals(Type.PENNY, c[2].getType());
		assertEquals(Type.PENNY, c[3].getType());
		assertEquals(Type.NICKEL, c[4].getType());
		assertEquals(Type.NICKEL, c[5].getType());
		assertEquals(Type.QUARTER, c[6].getType());
		assertEquals(Type.QUARTER, c[7].getType());
		assertEquals(Type.DOLLAR, c[8].getType());
		assertEquals(Type.DOLLAR, c[9].getType());
		assertEquals(Type.HALFDOLLAR, c[10].getType());
		assertEquals(Type.HALFDOLLAR, c[11].getType());
	}
	
	
	/// errors
	
	public void test8() {
		assertException(NullPointerException.class, () -> MoveStack.doMove(null, st2, st3));
		assertException(NullPointerException.class, () -> MoveStack.doMove(st1, null, st3));
		assertException(NullPointerException.class, () -> MoveStack.doMove(st1, st2, null));
		
		assertException(IllegalArgumentException.class, () -> MoveStack.doMove(st1, st2, st2));
	}
	
	public void test9() {
		st1.add(q1);
		st1.add(p2);
		st2.add(d2);
		
		assertException(IllegalArgumentException.class, () -> MoveStack.doMove(st1, st2, st3));

		st2.remove();
		st3.add(d2);
		
		assertException(IllegalArgumentException.class, () -> MoveStack.doMove(st1, st2, st3));
	}
}
