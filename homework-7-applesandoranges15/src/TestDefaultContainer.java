import java.util.NoSuchElementException;

import edu.uwm.cs351.money.Container;
import edu.uwm.cs351.money.DefaultContainer;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Spy.Statistic;

public class TestDefaultContainer extends TestContainer {

    @Override
    protected void initializeContainer() {
        self= Spy.makeContainer(null);
    }

    // locked tests:
    public void test() {
        // "asString" will convert the result into a string
        // If it's not null, then it runs "toString()" on it
        // If it's null, it returns "null"
        // If the code throws an exception, it returns the name of the exception
        assertEquals(Ts(362782147), asString(() -> self.remove()));
        // trying to add a QUARTER
        assertEquals(Ts(694337376), asString(() -> { self.add(q1); return true; }));
        assertEquals(Ts(1332444588), asString(() -> self.remove()));
        self.add(q1);
        assertEquals(Ts(1969024103), asString(() -> { self.add(q1); return true; }));
        self.add(n1); // a NICKEL
        assertEquals(Ts(59538507), asString(() -> self.remove()));
    }

    // test0x: wellFormed tests
//
    public void test00() {
        self= Spy.makeContainer(null);
        assertWellFormed(true, self);
    }

    public void test01() {
        self= Spy.makeContainer(q1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(true, self);
    }

    public void test02() {
        Spy.link(0, q1);
        self= Spy.makeContainer(q1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(false, self);
    }

    public void test03() {
        self= Spy.makeContainer(n1);
        Spy.link(-1, q1, n1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(false, self);
        Spy.own(self, n1);
        assertWellFormed(true, self);
        Spy.own(null, q1);
        assertWellFormed(false, self);
    }

    public void test04() {
        self= Spy.makeContainer(n1);
        Spy.link(0, q1, n1);
        assertWellFormed(false, self);
        Spy.own(self, q1, n1);
        assertWellFormed(false, self);
        Spy.link(1, q1, n1);
        assertWellFormed(false, self);
    }

    public void test05() {
        self= Spy.makeContainer(p1);
        Spy.link(-1, h1, d1, p1);
        assertWellFormed(false, self);
        Spy.own(self, p1, h1);
        assertWellFormed(false, self);
        Spy.own(null, h1, d1, p1);
        Spy.own(self, p1, d1);
        assertWellFormed(false, self);
        Spy.own(self, h1, d1, p1);
        assertWellFormed(true, self);
    }

    public void test06() {
        self= Spy.makeContainer(p1);
        Spy.own(self, h1, d1, p1);
        Spy.link(0, h1, d1, p1);
        assertWellFormed(false, self);
        Spy.link(1, h1, d1, p1);
        assertWellFormed(false, self);
        Spy.link(2, h1, d1, p1);
        assertWellFormed(false, self);
    }

    public void test07() {
        self= Spy.makeContainer(q1);
        Spy.link(-1, s1, h1, q2, d2, q1);
        assertWellFormed(false, self);
        Container other= new DefaultContainer();
        Spy.own(other, s1, h1, q2, s2, q1);
        assertWellFormed(false, self);
        Spy.own(self, q1);
        assertWellFormed(false, self);
        Spy.own(self, d2);
        assertWellFormed(false, self);
        Spy.own(self, q2);
        assertWellFormed(false, self);
        Spy.own(self, h1);
        assertWellFormed(false, self);
        Spy.own(self, s1);
        assertWellFormed(true, self);
    }

    // test1x: simple tests on empty containers

    public void test10() {
        assertTrue(self.isEmpty());
    }

    public void test11() {
        assertEquals(0, self.size());
    }

    public void test12() {
        assertTrue(self.canAdd(d1));
    }

    public void test13() {
        assertFalse(self.canAdd(null));
    }

    public void test14() {
        assertException(NoSuchElementException.class, () -> self.remove());
    }

    public void test15() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

    // test2x: tests with a small number of elements

    public void test20() {
        self.add(q1);
        assertEquals(1, self.size());
    }

    public void test21() {
        self.add(d1);
        assertEquals(false, self.isEmpty());
    }

    public void test22() {
        self.add(n1);
        assertFalse(self.canAdd(null));
    }

    public void test23() {
        self.add(h2);
        assertTrue(self.canAdd(h1));
    }

    public void test24() {
        self.add(h1);
        assertFalse(self.canAdd(h1));
    }

    public void test25() {
        self.add(p1);
        assertSame(p1, self.remove());
    }

    public void test26() {
        self.add(s1);
        self.remove();
        assertTrue(self.isEmpty());
    }

    public void test27() {
        self.add(d1);
        self.remove();
        assertEquals(0, self.size());
    }

    public void test28() {
        self.add(p1);
        self.remove();
        assertFalse(p1.isOwned());
    }

    public void test29() {
        self.add(n2);
        self.remove();
        assertTrue(self.canAdd(n2));
    }

    /// test3x: checking that the invariant is being checked

    public void test30() {
        assertEquals(0, self.size());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test31() {
        assertTrue(self.isEmpty());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test32() {
        assertTrue(self.canAdd(q1));
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test33() {
        self.add(q1);
        assertAtLeast(3, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test34() {
        self.add(d1);
        Spy.clearStats();
        self.remove();
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test35() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        Spy.clearStats();
        self.add(h1);
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test36() {
        assertException(NoSuchElementException.class, () -> self.remove());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        self.add(s2);
        Spy.clearStats();
        self.remove();
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

    public void test37() {
        self.add(n1);
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

    public void test38() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

    public void test39() {
        self.add(d2);
        Spy.clearStats();
        self.remove();
        assertEquals(1, Spy.getStat(Statistic.RELINQUISH));
    }

    /// test4x: bigger containers

    public void test40() {
        self.add(n1);
        self.add(n2);
        assertFalse(self.isEmpty());
        assertEquals(2, self.size());
    }

    public void test41() {
        self.add(d1);
        self.add(q1);
        assertSame(q1, self.remove());
        assertEquals(1, self.size());
    }

    public void test42() {
        self.add(p1);
        self.add(p2);
        assertFalse(self.canAdd(p1));
        assertFalse(self.canAdd(p2));
        assertTrue(self.canAdd(n1));
    }

    public void test43() {
        self.add(d1);
        DefaultContainer other= new DefaultContainer();
        assertFalse(other.canAdd(d1));
        other.add(q1);
        assertFalse(self.canAdd(q1));
    }

    public void test44() {
        self.add(s1);
        self.add(h1);
        DefaultContainer other= new DefaultContainer();
        other.add(s2);
        other.add(h2);
        assertSame(h1, self.remove());
        assertSame(h2, other.remove());
        self.add(h2);
        other.add(h1);
        assertEquals(2, self.size());
        assertEquals(2, other.size());
    }

    public void test45() {
        self.add(q1);
        self.add(n2);
        DefaultContainer other= new DefaultContainer();
        other.add(s1);
        other.add(h1);
        assertException(IllegalArgumentException.class, () -> self.add(s1));
        assertEquals(2, other.size());
    }

    public void test46() {
        self.add(p1);
        self.add(n2);
        DefaultContainer other= new DefaultContainer();
        other.add(p2);
        other.add(n1);
        assertSame(n1, other.remove());
        self.add(n1);
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

    public void test47() {
        self.add(p1);
        self.add(n1);
        self.add(d1);
        DefaultContainer other= new DefaultContainer();
        assertSame(d1, self.remove());
        other.add(d1);
        assertSame(n1, self.remove());
        other.add(n1);
        assertSame(p1, self.remove());
        other.add(p1);
        assertSame(3, other.size());
        assertTrue(self.isEmpty());
    }

    public void test48() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

    public void test49() {
        self.add(s2);
        self.add(h2);
        self.add(q2);
        DefaultContainer other= new DefaultContainer();
        other.add(p1);
        assertException(IllegalArgumentException.class, () -> other.add(h2));
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

    /// test5x: larger tests

    public void test50() {
        self.add(p1);
        self.add(s1);
        self.add(n1);
        self.add(h1);
        self.add(q1);
        assertSame(q1, self.remove());
        assertSame(h1, self.remove());
        assertSame(n1, self.remove());
        assertSame(s1, self.remove());
        assertSame(p1, self.remove());
    }

    public void test51() {
        DefaultContainer other= new DefaultContainer();
        self.add(p2);
        self.add(n2);
        self.add(d2);
        self.add(q2);
        self.add(h2);
        self.add(s2);
        other.add(self.remove());
        other.add(self.remove());
        other.add(self.remove());
        other.add(d1);
        other.add(n1);
        other.add(p1);
        assertEquals(6, other.size());
        assertEquals(3, self.size());
    }

    public void test52() {
        self.add(p1);
        self.add(n1);
        self.add(d1);
        self.add(q1);
        self.add(h1);
        self.add(s1);
        assertException(IllegalArgumentException.class, () -> self.add(p1));
        assertException(IllegalArgumentException.class, () -> self.add(n1));
        assertException(IllegalArgumentException.class, () -> self.add(d1));
        assertException(IllegalArgumentException.class, () -> self.add(q1));
        assertException(IllegalArgumentException.class, () -> self.add(h1));
        assertException(IllegalArgumentException.class, () -> self.add(s1));
        self.add(p2);
        assertEquals(7, self.size());
    }
}
