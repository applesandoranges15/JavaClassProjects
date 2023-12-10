import java.util.NoSuchElementException;

import edu.uwm.cs351.money.DefaultContainer;
import edu.uwm.cs351.money.Pipeline;
import edu.uwm.cs351.money.Spy;
import edu.uwm.cs351.money.Spy.Statistic;

public class TestPipeline extends TestContainer {

    @Override
    protected void initializeContainer() {
        self= Spy.makePipeline(null, null);
    }

    /// locked tests

    public void test() {
        // added a PENNY
        self.add(p1);
        // added a DOLLAR coin
        self.add(s1);
        // add a NICKEL
        self.add(n1);
        // "asString" will convert the result into a string
        // If it's not null, then it runs "toString()" on it
        // If it's null, it returns "null"
        // If the code throws an exception, it returns the name of the exception
        assertEquals(Ts(1475330063), asString(() -> self.remove()));
        assertEquals(Ts(506998948), asString(() -> self.remove()));
        assertEquals(Ts(336966447), asString(() -> self.remove()));
        assertEquals(Ts(223309470), asString(() -> self.remove()));
    }

//
////
////    // testax: wellFormed tests
////
    public void testa0() {
        self= Spy.makePipeline(null, null);
        assertWellFormed(true, self);
    }

////
////////
    public void testa1() {
        self= Spy.makePipeline(q1, null);
        Spy.own(self, q1);
        assertWellFormed(false, self);
        self= Spy.makePipeline(null, q1);
        Spy.own(self, q1);
        assertWellFormed(false, self);
    }

////
////////
    public void testa2() {
        Spy.link(0, q1);
        self= Spy.makePipeline(q1, q1);
        Spy.own(self, q1);
        assertWellFormed(false, self);
        Spy.link(-1, q1);
        assertWellFormed(true, self);
        Spy.own(Spy.makePipeline(q1, q1), q1);
        assertWellFormed(false, self);
    }

////////
////
////    // if self is wellFormed, then self would have all its
////    // endogenous elements as owner
    public void testa3() {
        Spy.link(-1, q1);
        Spy.link(-1, q2);
        Spy.link(0, q1);
        self= Spy.makePipeline(q1, q2);
        Spy.own(self, q1, q2);
        assertWellFormed(false, self);
        Spy.link(-1, q2, q1);
        assertWellFormed(true, self);
        Spy.link(0, q2, q1);
        assertWellFormed(false, self);
    }

////
////////
    public void testa4() {
        self= Spy.makePipeline(n1, q1);
        Spy.link(-1, q1, n1);
        Spy.own(self, n1, q1);
        assertWellFormed(true, self);
        Spy.own(null, q1);
        assertWellFormed(false, self);
    }

////
////////
    public void testa5() {
        self= Spy.makePipeline(p1, q2);
        Spy.link(-1, q1, d1, n1, p1);
        Spy.own(self, p1, n1, d1, q1, q2);
        assertWellFormed(false, self);
        Spy.link(-1, q2, d1, n1, p1);
        assertWellFormed(true, self);
        Spy.link(2, q1, d1, n1, p1);
        assertWellFormed(false, self);
    }

////
////////
    public void testa6() {
        self= Spy.makePipeline(p1, q1);
        Spy.link(-1, q2, q1, d1, n1, p1);
        Spy.own(self, p1, n1, d1, q1, q2);
        assertWellFormed(false, self);
    }

////
////////
    public void testa7() {
        self= Spy.makePipeline(p1, s1);
        Spy.link(-1, s2, s1, h2, h1, q2, q1, d2, d1, n2, n1, p2, p1);
        Spy.own(self, p1, p2, n1, n2, d1, d2, q1, q2, h1, h2, s1, s2);
        assertWellFormed(false, self);
        Spy.link(-1, s2, h2);
        assertWellFormed(false, self);
        Spy.link(-1, s1, s2, h2);
        assertWellFormed(true, self);
    }

////
//////
//////    // testex: simple tests on empty containers
//////
    public void teste0() {
        assertTrue(self.isEmpty());
    }

////
//////
    public void teste1() {
        assertEquals(0, self.size());
    }

////
//////
    public void teste2() {
        assertTrue(self.canAdd(d1));
    }

////
//////
    public void teste3() {
        assertFalse(self.canAdd(null));
    }

////
//////
    public void teste4() {
        assertException(NoSuchElementException.class, () -> self.remove());
    }

////
//////
    public void teste5() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

//
//    // testgx: tests with a small number of elements
////
    public void testg0() {
        self.add(q1);
        assertEquals(1, self.size());
    }

////
    public void testg1() {
        self.add(d1);
        assertEquals(false, self.isEmpty());
    }

////
    public void testg2() {
        self.add(n1);
        assertFalse(self.canAdd(null));
    }

//
////
    public void testg3() {
        self.add(h2);
        assertTrue(self.canAdd(h1));
    }

//
////
    public void testg4() {
        self.add(h1);
        assertFalse(self.canAdd(h1));
    }

//
////
    public void testg5() {
        self.add(p1);
        assertSame(p1, self.remove());
    }

//
////
    public void testg6() {
        self.add(s1);
        self.remove();
        assertTrue(self.isEmpty());
    }

//
////
    public void testg7() {
        self.add(d1);
        self.remove();
        assertEquals(0, self.size());
    }

//
////
    public void testg8() {
        self.add(p1);
        self.remove();
        assertFalse(p1.isOwned());
    }

//
////
    public void testg9() {
        self.add(n2);
        self.remove();
        assertTrue(self.canAdd(n2));
    }

//
////
////    /// testix: checking that the invariant is being checked
////
    public void testi0() {
        assertEquals(0, self.size());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

//
////
    public void testi1() {
        assertTrue(self.isEmpty());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

//
////
    public void testi2() {
        assertTrue(self.canAdd(q1));
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
    }

////
//////
    public void testi3() {
        self.add(q1);
        assertAtLeast(3, Spy.getStat(Statistic.WELLFORMED));
    }

////
//////
    public void testi4() {
        self.add(d1);
        Spy.clearStats();
        self.remove();
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
    }

////
//////
    public void testi5() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertAtLeast(2, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        Spy.clearStats();
        self.add(h1);
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

//////
    public void testi6() {
        assertException(NoSuchElementException.class, () -> self.remove());
        assertAtLeast(1, Spy.getStat(Statistic.WELLFORMED));
        int ifError= Spy.getStat(Statistic.WELLFORMED);
        self.add(s2);
        Spy.clearStats();
        self.remove();
        assertAtLeast(ifError + 1, Spy.getStat(Statistic.WELLFORMED));
    }

//////
    public void testi7() {
        self.add(n1);
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

////
//////
    public void testi8() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
        assertEquals(1, Spy.getStat(Statistic.TAKEOWNERSHIP));
    }

////
//////
    public void testi9() {
        self.add(d2);
        Spy.clearStats();
        self.remove();
        assertEquals(1, Spy.getStat(Statistic.RELINQUISH));
    }

////
//////
//////    /// testlx: bigger containers
//////
    public void testl0() {
        self.add(n1);
        self.add(n2);
        assertFalse(self.isEmpty());
        assertEquals(2, self.size());
    }

//////
    public void testl1() {
        self.add(d1);
        self.add(q1);
        assertSame(d1, self.remove());
        assertEquals(1, self.size());
    }

////
//////
    public void testl2() {
        self.add(p1);
        self.add(p2);
        assertFalse(self.canAdd(p1));
        assertFalse(self.canAdd(p2));
        assertTrue(self.canAdd(n1));
    }

////
//////
    public void testl3() {
        self.add(d1);
        DefaultContainer other= new Pipeline();
        assertFalse(other.canAdd(d1));
        other.add(q1);
        assertFalse(self.canAdd(q1));
    }

//
    public void testl4() {
        self.add(s1);
        self.add(h1);
        DefaultContainer other= new Pipeline();
        other.add(s2);
        other.add(h2);
        assertSame(s1, self.remove());
        assertSame(s2, other.remove());
        self.add(s2);
        other.add(s1);
        assertEquals(2, self.size());
        assertEquals(2, other.size());
    }

//
    public void testl5() {
        self.add(q1);
        self.add(n2);
        DefaultContainer other= new Pipeline();
        other.add(s1);
        other.add(h1);
        assertException(IllegalArgumentException.class, () -> self.add(s1));
        assertEquals(2, other.size());
    }

    public void testl6() {
        self.add(p1);
        self.add(n2);
        DefaultContainer other= new Pipeline();
        other.add(p2);
        other.add(n1);
        assertSame(p2, other.remove());
        self.add(p2);
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

    public void testl7() {
        self.add(p1);
        self.add(n1);
        self.add(d1);
        DefaultContainer other= new Pipeline();
        assertSame(p1, self.remove());
        other.add(p1);
        assertSame(n1, self.remove());
        other.add(n1);
        assertSame(d1, self.remove());
        other.add(d1);
        assertSame(3, other.size());
        assertTrue(self.isEmpty());
    }

    public void testl8() {
        assertException(IllegalArgumentException.class, () -> self.add(null));
    }

    public void testl9() {
        self.add(s2);
        self.add(h2);
        self.add(q2);
        DefaultContainer other= new Pipeline();
        other.add(p1);
        assertException(IllegalArgumentException.class, () -> other.add(h2));
        assertEquals(3, self.size());
        assertEquals(1, other.size());
    }

    /// testmx: larger tests

    public void testm0() {
        self.add(p1);
        self.add(s1);
        self.add(n1);
        self.add(h1);
        self.add(q1);
        assertSame(p1, self.remove());
        assertSame(s1, self.remove());
        assertSame(n1, self.remove());
        assertSame(h1, self.remove());
        assertSame(q1, self.remove());
    }

    public void testm1() {
        DefaultContainer other= new Pipeline();
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

    public void testm2() {
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

    public void testm3() {
        self.add(p1);
        self.add(n1);
        self.add(d1);
        self.add(q1);
        self.add(h1);
        self.add(s1);
        self.remove();
        self.remove();
        self.remove();
        self.add(p2);
        self.add(n2);
        self.add(d2);
        assertEquals(6, self.size());
        assertSame(q1, self.remove());
        assertSame(h1, self.remove());
        assertSame(s1, self.remove());
        assertSame(p2, self.remove());
    }
}
