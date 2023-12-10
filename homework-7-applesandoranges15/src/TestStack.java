import java.util.NoSuchElementException;

import edu.uwm.cs351.money.DefaultContainer;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Spy.Statistic;
import edu.uwm.cs351.money.Stack;

public class TestStack extends TestContainer {

    @Override
    protected void initializeContainer() {
        self= Spy.makeStack(null);
    }

    // test() locked tests

    public void test() {
        // "asString" will convert the result into a string
        // If it's not null, then it runs "toString()" on it
        // If it's null, it returns "null"
        // If the code throws an exception, it returns the name of the exception
        // trying to add a QUARTER:
        assertEquals("true", asString(() -> { self.add(q1); return true; }));
        // trying to add a DIME
        assertEquals(Ts(1480376149), asString(() -> { self.add(d1); return true; }));
        // trying to add a PENNY
        assertEquals(Ts(341379553), asString(() -> { self.add(p1); return true; }));

    }
    // testAx: wellFormed tests

    public void testA0() {
        self= Spy.makeStack(null);
        assertWellFormed(true, self);
    }

    public void testA1() {
        self= Spy.makeStack(q1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(true, self);
    }

    public void testA2() {
        Spy.link(0, q1);
        self= Spy.makeStack(q1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(false, self);
    }

    public void testA3() {
        self= Spy.makeStack(n1);
        Spy.link(-1, q1, n1);
        Spy.own(self, q1, n1);
        assertWellFormed(true, self);
    }

    public void testA4() {
        self= Spy.makeStack(q1);
        Spy.link(-1, n1, q1);
        Spy.own(self, q1, n1);
        assertWellFormed(false, self);
    }

    public void testA5() {
        self= Spy.makeStack(n1);
        Spy.link(-1, n2, n1);
        Spy.own(self, n2, n1);
        assertWellFormed(true, self);
    }

    public void testA6() {
        self= Spy.makeStack(p1);
        Spy.link(-1, d1, p1);
        Spy.own(self, d1, p1);
        assertWellFormed(false, self);
    }

    public void testA7() {
        self= Spy.makeStack(p1);
        Spy.link(-1, q1, d1, p1);
        Spy.own(self, q1, d1, p1);
        assertWellFormed(false, self);
    }

    public void testA8() {
        self= Spy.makeStack(p1);
        Spy.link(-1, q1, n1, p1);
        Spy.own(self, q1, n1, p1);
        assertWellFormed(true, self);
    }

    public void testA9() {
        self= Spy.makeStack(d1);
        Spy.link(-1, h1, s2, q1, q2, n1, n2, p1, p2, d2, d1);
        Spy.own(self, p1, p2, n1, n2, d1, d2, q1, q2, h1, h2, s1, s2);
        assertWellFormed(true, self);
        Spy.link(-1, h1, s2, q1, q2, n1, p1, n2, d2, d1);
        assertWellFormed(false, self);
    }

    // testEx: simple tests on empty containers

    public void testE0() {
        assertTrue(self.isEmpty());
    }

    public void testE1() {
        assertEquals(0, self.size());
    }

    public void testE2() {
        assertTrue(self.canAdd(d1));
    }

    public void testE3() {
        assertFalse(self.canAdd(null));
    }

    public void testE4() {
        assertException(NoSuchElementException.class, () -> self.remove());
    }

    public void testE5() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

    // testGx: tests with a small number of elements

    public void testG0() {
        self.add(q1);
        assertEquals(1, self.size());
    }

    public void testG1() {
        self.add(d1);
        assertEquals(false, self.isEmpty());
    }

    public void testG2() {
        self.add(n1);
        assertFalse(self.canAdd(null));
        assertFalse(self.canAdd(q1));
    }

    public void testG3() {
        self.add(h2);
        assertTrue(self.canAdd(h1));
    }

    public void testG4() {
        self.add(p1);
        assertFalse(self.canAdd(p1));
        assertTrue(self.canAdd(d1));
    }

    public void testG5() {
        self.add(p1);
        assertSame(p1, self.remove());
    }

    public void testG6() {
        self.add(s1);
        self.remove();
        assertTrue(self.isEmpty());
    }

    public void testG7() {
        self.add(d1);
        self.remove();
        assertEquals(0, self.size());
    }

    public void testG8() {
        self.add(p1);
        self.remove();
        assertFalse(p1.isOwned());
    }

    public void testG9() {
        self.add(n2);
        self.remove();
        assertTrue(self.canAdd(n2));
    }

    /// testIx: checking that the invariant is being checked

    public void testI0() {
        assertEquals(0, self.size());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI1() {
        assertTrue(self.isEmpty());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI2() {
        assertTrue(self.canAdd(q1));
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI3() {
        self.add(q1);
        assertAtLeast(3, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI4() {
        self.add(d1);
        Spy.clearStats();
        self.remove();
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI5() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        Spy.clearStats();
        self.add(h1);
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI6() {
        assertException(NoSuchElementException.class, () -> self.remove());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        self.add(s2);
        Spy.clearStats();
        self.remove();
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void testI7() {
        self.add(n1);
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

    public void testI8() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

    public void testI9() {
        self.add(d2);
        Spy.clearStats();
        self.remove();
        assertEquals(1, Spy.getStat(Statistic.RELINQUISH));
    }

    /// testMx: bigger containers

    public void testM0() {
        self.add(n1);
        self.add(n2);
        assertFalse(self.isEmpty());
        assertEquals(2, self.size());
    }

    public void testM1() {
        self.add(d1);
        assertException(IllegalArgumentException.class, () -> self.add(q1));
        assertSame(d1, self.remove());
        assertEquals(0, self.size());
    }

    public void testM2() {
        self.add(p1);
        self.add(p2);
        assertFalse(self.canAdd(p1));
        assertFalse(self.canAdd(p2));
        assertFalse(self.canAdd(n1));
        assertTrue(self.canAdd(d1));
    }

    public void testM3() {
        self.add(h1);
        DefaultContainer other= new Stack();
        assertFalse(other.canAdd(h1));
        other.add(q1);
        assertFalse(self.canAdd(q1));
    }

    public void testM4() {
        self.add(h1);
        self.add(s1);
        DefaultContainer other= new Stack();
        other.add(h2);
        other.add(s2);
        assertSame(s1, self.remove());
        assertSame(s2, other.remove());
        self.add(s2);
        other.add(s1);
        assertEquals(2, self.size());
        assertEquals(2, other.size());
    }

    public void testM5() {
        self.add(q1);
        self.add(n2);
        DefaultContainer other= new Stack();
        other.add(h1);
        other.add(d1);
        assertException(IllegalArgumentException.class, () -> self.add(d1));
        assertEquals(2, other.size());
    }

    public void testM6() {
        self.add(q1);
        self.add(n2);
        DefaultContainer other= new Stack();
        other.add(q2);
        other.add(n1);
        assertSame(n1, other.remove());
        self.add(n1);
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

    public void testM7() {
        self.add(n1);
        self.add(p1);
        assertFalse(self.canAdd(n2));
        self.add(d1);
        assertException(IllegalArgumentException.class, () -> self.add(p2));
        DefaultContainer other= new Stack();
        assertSame(d1, self.remove());
        other.add(d1);
        assertSame(p1, self.remove());
        assertException(IllegalArgumentException.class, () -> other.add(p1));
        assertSame(n1, self.remove());
        assertException(IllegalArgumentException.class, () -> other.add(n1));
        assertSame(1, other.size());
        assertTrue(self.isEmpty());
    }

    public void testM8() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

    public void testM9() {
        self.add(h2);
        self.add(s2);
        self.add(q2);
        DefaultContainer other= new Stack();
        other.add(p1);
        assertException(IllegalArgumentException.class, () -> other.add(h2));
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

}
