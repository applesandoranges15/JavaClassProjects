
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.ConcatPath;
import edu.uwm.cs351.DegeneratePath;
import edu.uwm.cs351.Path;
import edu.uwm.cs351.PostpendPath;
import edu.uwm.cs351.PrependPath;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskFactory;

public class TestPath extends LockedTestCase {

    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
        try {
            r.run();
            assertFalse("Exception should have been thrown", true);
        } catch (RuntimeException ex) {
            if (!c.isInstance(ex)) {
                ex.printStackTrace();
            }
            assertTrue("should throw exception of " + c + ", not of " + ex.getClass(),
                c.isInstance(ex));
        }
    }

    protected Path path;
    protected Task t1, t2, t3, t4, t5;

    @Override
    protected void setUp() {
        try {
            assert path.contains(null);
            throw new IllegalStateException("assertions must be enabled to run this test");
        } catch (NullPointerException ex) {
            // OK!
        }
        t1= new Task("1");
        t2= new Task("2");
        t3= new Task("3");
        t4= new Task("4");
        t5= new Task("5");
    }

    protected String asString(Supplier<?> supp) {
        try {
            return "" + supp.get();
        } catch (RuntimeException ex) {
            return ex.getClass().getSimpleName();
        }
    }

    /// Locked tests

    public void test() {
        // asString returns a string for the expression, "null" (if null) or the name of the
        // exception thrown
        // tn prints as n.
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        t5.dependsOn(t3);
        path= new DegeneratePath(t1);
        assertEquals(Ts(1765875794), asString(() -> path));
        assertEquals(Ts(1052239261), asString(() -> path.prepend(t2)));
        assertEquals(Ts(1252049385), asString(() -> path.append(t2)));
        assertEquals(Ts(1678737599), asString(() -> path.append(t3)));
        path= new PostpendPath(path, t3);
        Path path2= new DegeneratePath(t3);
        assertEquals(Ts(511838520), asString(() -> new ConcatPath(path, path2)));
        assertEquals(Ts(282574108), asString(() -> path.concat(path2)));
        Path path3= path2.append(t5);
        assertEquals(Ts(307692636), asString(() -> new ConcatPath(path, path3)));
    }

    /// test0X: degenerate paths
    // methods that don't use the worklist

    public void test00() {
        path= new DegeneratePath(t1);
        assertTrue(true);
    }

    public void test01() {
        path= new DegeneratePath(t2);
        assertEquals(1, path.size());
    }

    public void test02() {
        path= new DegeneratePath(t3);
        assertSame(t3, path.getFirst());
    }

    public void test03() {
        path= new DegeneratePath(t4);
        assertSame(t4, path.getLast());
    }

    public void test04() {
        path= new DegeneratePath(t4);
        t4.dependsOn(t3);
        assertSame(t4, path.getFirst());
    }

    public void test05() {
        assertFalse(t3.getDependencies().contains(t3)); // should succeed
        path= new DegeneratePath(t3);
        assertSame(t3, path.getLast());
    }

    public void test06() {
        path= new DegeneratePath(t4);
        assertSame(t4, path.getFirst());
        t4.dependsOn(t5);
    }

    public void test07() {
        path= new DegeneratePath(t5);
        assertEquals(1, path.size());
        t5.dependsOn(t4);
    }

    public void test08() {
        path= new DegeneratePath(t1);
        assertFalse(path.isCycle());
    }

    /// test1x: prepend paths

    public void test10() {
        t1.dependsOn(t2);
        path= new DegeneratePath(t1);
        path= new PrependPath(t2, path);
        assertTrue(true);
    }

    public void test11() {
        t2.dependsOn(t1);
        path= new DegeneratePath(t2);
        path= new PrependPath(t1, path);
        assertEquals(2, path.size());
    }

    public void test12() {
        t3.dependsOn(t4);
        path= new DegeneratePath(t3);
        path= new PrependPath(t4, path);
        assertSame(t4, path.getFirst());
    }

    public void test13() {
        t4.dependsOn(t2);
        t2.dependsOn(t3);
        t5.dependsOn(t4);
        path= new DegeneratePath(t4);
        path= new PrependPath(t2, path);
        assertSame(t4, path.getLast());
    }

    public void test14() {
        t1.dependsOn(t3);
        t3.dependsOn(t5);
        t5.dependsOn(t2);
        t3.dependsOn(t2);
        path= new DegeneratePath(t1);
        path= new PrependPath(t3, path);
        path= new PrependPath(t2, path);
        assertEquals(3, path.size());
    }

    public void test15() {
        t1.dependsOn(t3);
        t3.dependsOn(t5);
        t5.dependsOn(t2);
        t3.dependsOn(t2);
        path= new DegeneratePath(t1);
        path= new PrependPath(t3, path);
        path= new PrependPath(t5, path);
        assertSame(t5, path.getFirst());
        assertSame(t1, path.getLast());
    }

    public void test16() {
        path= new DegeneratePath(t1);
        assertException(NullPointerException.class, () -> new PrependPath(null, path));
    }

    public void test17() {
        assertException(NullPointerException.class, () -> new PrependPath(t1, null));
    }

    public void test18() {
        t1.dependsOn(t2);
        path= new DegeneratePath(t2);
        assertException(IllegalArgumentException.class, () -> new PrependPath(t1, path));
    }

    public void test19() {
        t1.dependsOn(t1);
        t2.dependsOn(t2);
        path= new DegeneratePath(t2);
        assertException(IllegalArgumentException.class, () -> new PrependPath(t1, path));
    }

    /// test2x: postpend paths

    public void test20() {
        t1.dependsOn(t2);
        path= new DegeneratePath(t2);
        path= new PostpendPath(path, t1);
        assertTrue(true);
    }

    public void test21() {
        t2.dependsOn(t1);
        path= new DegeneratePath(t1);
        path= new PostpendPath(path, t2);
        assertEquals(2, path.size());
    }

    public void test22() {
        t3.dependsOn(t4);
        path= new DegeneratePath(t4);
        path= new PostpendPath(path, t3);
        assertSame(t4, path.getFirst());
    }

    public void test23() {
        t4.dependsOn(t2);
        t2.dependsOn(t3);
        t5.dependsOn(t4);
        path= new DegeneratePath(t2);
        path= new PostpendPath(path, t4);
        assertSame(t4, path.getLast());
    }

    public void test24() {
        t1.dependsOn(t3);
        t3.dependsOn(t5);
        t5.dependsOn(t2);
        t3.dependsOn(t2);
        path= new DegeneratePath(t2);
        path= new PostpendPath(path, t3);
        path= new PostpendPath(path, t1);
        assertEquals(3, path.size());
    }

    public void test25() {
        t1.dependsOn(t3);
        t3.dependsOn(t5);
        t5.dependsOn(t2);
        t3.dependsOn(t2);
        path= new DegeneratePath(t5);
        path= new PostpendPath(path, t3);
        path= new PostpendPath(path, t1);
        assertSame(t5, path.getFirst());
        assertSame(t1, path.getLast());
    }

    public void test26() {
        path= new DegeneratePath(t1);
        assertException(NullPointerException.class, () -> new PostpendPath(path, null));
    }

    public void test27() {
        assertException(NullPointerException.class, () -> new PostpendPath(null, t1));
    }

    public void test28() {
        t1.dependsOn(t2);
        path= new DegeneratePath(t2);
        assertException(IllegalArgumentException.class, () -> new PostpendPath(path, t2));
    }

    public void test29() {
        t1.dependsOn(t1);
        t2.dependsOn(t2);
        path= new DegeneratePath(t2);
        assertException(IllegalArgumentException.class, () -> new PostpendPath(path, t1));
    }

    /// test3X: tests of concat path

    public void test30() {
        t1.dependsOn(t2);
        t2.dependsOn(t3);
        path= new DegeneratePath(t3);
        path= path.append(t2);
        Path path2= new DegeneratePath(t1);
        path2= path2.prepend(t2);
        path= new ConcatPath(path, path2);
        assertEquals(3, path.size());
    }

    public void test31() {
        t2.dependsOn(t1);
        t3.dependsOn(t2);
        path= new DegeneratePath(t2);
        path= path.append(t3);
        Path path2= new DegeneratePath(t2);
        path2= path2.prepend(t1);
        path= new ConcatPath(path2, path);
        assertEquals(3, path.size());
    }

    public void test32() {
        t1.dependsOn(t2);
        t2.dependsOn(t3);
        path= new DegeneratePath(t3);
        path= path.append(t2);
        Path path2= new DegeneratePath(t1);
        path2= path2.prepend(t2);
        path= new ConcatPath(path, path2);
        assertSame(t1, path.getLast());
        assertSame(t3, path.getFirst());
    }

    public void test33() {
        t2.dependsOn(t1);
        t3.dependsOn(t2);
        path= new DegeneratePath(t2);
        path= path.append(t3);
        Path path2= new DegeneratePath(t2);
        path2= path2.prepend(t1);
        path= new ConcatPath(path2, path);
        assertSame(t1, path.getFirst());
        assertSame(t3, path.getLast());
    }

    public void test34() {
        t5.dependsOn(t4);
        t4.dependsOn(t3);
        t3.dependsOn(t2);
        path= new DegeneratePath(t4);
        path= path.append(t5);
        Path path2= new DegeneratePath(t4);
        path2= path2.prepend(t3);
        path= new ConcatPath(wrap(path2), wrap(path));
        path2= new DegeneratePath(t2);
        path2= path2.append(t3);
        path= new ConcatPath(wrap(path2), wrap(path));
        assertEquals(4, path.size());
        assertSame(t2, path.getFirst());
        assertSame(t5, path.getLast());
    }

    public void test35() {
        t1.dependsOn(t4);
        path= new DegeneratePath(t4);
        path= path.append(t1);
        Path path2= new DegeneratePath(t1);
        assertException(IllegalArgumentException.class, () -> new ConcatPath(path, wrap(path2)));
    }

    public void test36() {
        t3.dependsOn(t2);
        t2.dependsOn(t1);
        path= new DegeneratePath(t1);
        path= path.append(t2).append(t3);
        Path path2= new DegeneratePath(t1);
        assertException(IllegalArgumentException.class, () -> new ConcatPath(wrap(path2), path));
    }

    public void test37() {
        t1.dependsOn(t2);
        t2.dependsOn(t3);
        t3.dependsOn(t4);
        path= new DegeneratePath(t1);
        path= path.prepend(t2);
        Path path2= new DegeneratePath(t4).append(t3);
        assertException(IllegalArgumentException.class, () -> new ConcatPath(path2, path));
    }

    public void test38() {
        t4.dependsOn(t5);
        path= new DegeneratePath(t5);
        path= path.append(t4);
        assertException(NullPointerException.class, () -> new ConcatPath(path, null));
    }

    public void test39() {
        t4.dependsOn(t5);
        path= new DegeneratePath(t5);
        path= path.append(t4);
        assertException(NullPointerException.class, () -> new ConcatPath(null, path));
    }

    /// test4X: mixes of prepend, postpend and concat

    protected Task[] tasks(String x) {
        Collection<Task> c= new TaskFactory().fromString(x);
        Task[] result= new Task[10];
        for (Task t : c) {
            result[Integer.parseInt(t.getName())]= t;
        }
        return result;
    }

    public void test40() {
        Task[] t= tasks("1<2<3>5 2<4<5");
        path= new DegeneratePath(t[2]);
        path= new PrependPath(t[1], path);
        path= new PostpendPath(path, t[3]);

        assertEquals(3, path.size());
        assertEquals(t[1], path.getFirst());
        assertEquals(t[3], path.getLast());
    }

    public void test41() {
        Task[] t= tasks("1<2<3>5 2<4<5");
        path= new DegeneratePath(t[2]);
        path= new PostpendPath(path, t[3]);
        path= new PrependPath(t[1], path);

        assertEquals(3, path.size());
        assertEquals(t[1], path.getFirst());
        assertEquals(t[3], path.getLast());
    }

    public void test42() {
        Task[] t= tasks("1<2<3>5 2<4<5");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(path, t[5]);
        path= new PrependPath(t[2], path);

        Path path2= new DegeneratePath(t[3]).prepend(t[5]);
        path= new ConcatPath(path, path2);
        path= new PrependPath(t[1], path);

        assertEquals(5, path.size());
        assertEquals(t[1], path.getFirst());
        assertEquals(t[3], path.getLast());
    }

    public void test43() {
        Task[] t= tasks("1<2<3>5 2<4<5");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(path, t[5]);
        path= new PostpendPath(path, t[3]);
        path= new PrependPath(t[2], path);
        path= new PrependPath(t[1], path);

        assertEquals(5, path.size());
        assertEquals(t[1], path.getFirst());
        assertEquals(t[3], path.getLast());
    }

    public void test44() {
        Task[] t= tasks("1<2<3>5 2<4<5 3<6<9");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(path, t[5]);
        path= new PostpendPath(path, t[3]);
        path= new PrependPath(t[2], path);
        path= new PrependPath(t[1], path);
        path= new PostpendPath(path, t[6]);
        path= new PostpendPath(path, t[9]);
        assertSame(t[1], path.getFirst());
        assertSame(t[9], path.getLast());
        assertFalse(path.isCycle());
    }

    public void test45() {
        Task[] t= tasks("1<2<3>5 2<4<5 3<6<9");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(path, t[5]);
        path= new PostpendPath(path, t[3]);
        path= new PrependPath(t[2], path);
        path= new PrependPath(t[1], path);
        assertException(IllegalArgumentException.class, () -> new PostpendPath(path, t[9]));
    }

    public void test46() {
        Task[] t= tasks("1<2<3>5 2<4<5 3<6<9 6>1<3 1<5");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(path, t[5]);
        path= new PostpendPath(path, t[3]);
        path= new PostpendPath(path, t[6]);
        assertException(IllegalArgumentException.class, () -> new PrependPath(t[1], path));
    }

    public void test47() {
        Task[] t= tasks("1<2<3>5 2<4<5<1");
        path= new DegeneratePath(t[4]);
        path= new PrependPath(t[2], path);
        path= new PrependPath(t[1], path);
        path= new PostpendPath(path, t[5]);
        assertFalse(path.isCycle());
        path= new PostpendPath(path, t[1]);
        assertTrue(path.isCycle());
    }

    public void test48() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[4]);
        path= new PrependPath(t[2], path);
        path= new PrependPath(t[1], path);
        path= new PostpendPath(path, t[5]);
        path= new PostpendPath(path, t[1]);
        path= new PrependPath(t[3], path);
        path= new PrependPath(t[2], path);
        Path p= path.prepend(t[1]);
        assertFalse(path.isCycle());
        assertEquals(7, path.size());
        assertTrue(p.isCycle());
        assertEquals(8, p.size());
    }

    public void test49() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        System.out.println(t[1] + "one");
        System.out.println(t[2] + "two");
        System.out.println(t[4] + "four");
        System.out.println(t[5] + "five");
        Path path1= new DegeneratePath(t[1]).append(t[2]).append(t[4]);
        Path path2= new DegeneratePath(t[1]).prepend(t[5]).prepend(t[4]);
        Path path3= new ConcatPath(path1, path2);
        Path path4= new DegeneratePath(t[2]).prepend(t[1]);
        Path path5= new DegeneratePath(t[2]).append(t[3]);
        Path path6= new ConcatPath(path4, path5);
        path= new ConcatPath(path3, path6);
        assertEquals(7, path.size());
    }

    /// test5X: testing toArray

    public void test50() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[5]);
        assertEquals(Arrays.asList(t[5]), Arrays.asList(path.toArray()));
    }

    public void test51() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[4]);
        path= path.append(t[5]);
        assertEquals(Arrays.asList(t[4], t[5]), Arrays.asList(path.toArray()));
    }

    public void test52() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[4]);
        path= path.append(t[5]).prepend(t[2]);
        assertEquals(Arrays.asList(t[2], t[4], t[5]), Arrays.asList(path.toArray()));
    }

    public void test53() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[4]);
        path= path.append(t[5]).prepend(t[2]).append(t[1]);
        assertEquals(Arrays.asList(t[2], t[4], t[5], t[1]), Arrays.asList(path.toArray()));
    }

    public void test54() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3");
        path= new DegeneratePath(t[2]).append(t[3]).append(t[1]);
        path= path.concat(new DegeneratePath(t[4]).prepend(t[2]).prepend(t[1]));
        path= path.append(t[5]);
        assertEquals(Arrays.asList(t[2], t[3], t[1], t[2], t[4], t[5]),
            Arrays.asList(path.toArray()));
    }

    /// test6x: tests of contains

    public void test60() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        assertTrue(path.contains(t1));
        assertFalse(path.contains(t2));
    }

    public void test61() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t2);
        path= path.append(t1).append(t3);
        assertTrue(path.contains(t1));
        assertTrue(path.contains(t2));
        assertTrue(path.contains(t3));
        assertFalse(path.contains(t4));
        assertFalse(path.contains(new Task("1")));
    }

    public void test62() {
        t1.dependsOn(t2);
        t2.dependsOn(t3);
        path= new DegeneratePath(t1);
        path= path.prepend(t2).prepend(t3);
        assertTrue(path.contains(t1));
        assertTrue(path.contains(t2));
        assertTrue(path.contains(t3));
        assertFalse(path.contains(t4));
        assertFalse(path.contains(new Task("2")));
    }

    public void test63() {
        t2.dependsOn(t1);
        t1.dependsOn(t3);
        path= new DegeneratePath(t1).append(t2);
        path= new DegeneratePath(t1).prepend(t3).concat(path);
        assertTrue(path.contains(t1));
        assertTrue(path.contains(t2));
        assertTrue(path.contains(t3));
        assertFalse(path.contains(t4));
        assertFalse(path.contains(new Task("3")));
    }

    public void test64() {
        Task[] t= tasks("1<2<3>5 2<4<5 3<6<9");
        path= new DegeneratePath(t[4]);
        path= new PostpendPath(wrap(path), t[5]);
        path= new PostpendPath(wrap(path), t[3]);
        path= new PrependPath(t[2], wrap(path));
        path= new PrependPath(t[1], wrap(path));
        path= new PostpendPath(wrap(path), t[6]);
        path= new PostpendPath(wrap(path), t[9]);
        path= wrap(path);
        assertTrue(path.contains(t[1]));
        assertTrue(path.contains(t[2]));
        assertTrue(path.contains(t[3]));
        assertTrue(path.contains(t[4]));
        assertTrue(path.contains(t[5]));
        assertTrue(path.contains(t[6]));
        assertTrue(path.contains(t[9]));
        assertFalse(path.contains(t1));
        assertFalse(path.contains(t2));
    }

    public void test65() {
        Task[] t= tasks("1<2<3>5 2<4<5 3<6<9");
        path= wrap(new DegeneratePath(t[5]));
        assertFalse(path.contains(null));
        assertTrue(path.contains(t[5]));
    }

    public void test69() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3 6 0");
        Path path1= new DegeneratePath(t[1]).append(t[2]).append(t[4]);
        Path path2= new DegeneratePath(t[1]).prepend(t[5]).prepend(t[4]);
        Path path3= new ConcatPath(path1, path2);
        Path path4= new DegeneratePath(t[2]).prepend(t[1]);
        Path path5= new DegeneratePath(t[2]).append(t[3]);
        Path path6= new ConcatPath(path4, path5);
        path= new ConcatPath(path3, path6);
        assertTrue(path.contains(t[1]));
        assertTrue(path.contains(t[2]));
        assertTrue(path.contains(t[3]));
        assertTrue(path.contains(t[4]));
        assertTrue(path.contains(t[5]));
        assertFalse(path.contains(t[6]));
        assertFalse(path.contains(t[7]));
        assertFalse(path.contains(t[0]));
    }

    /// test7X: tests of get

    public void test70() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        assertSame(t1, path.get(0));
    }

    public void test71() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        assertException(IndexOutOfBoundsException.class, () -> path.get(1));
    }

    public void test72() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        path= wrap(wrap(path).prepend(t2));
        assertSame(t2, path.get(0));
        assertSame(t1, path.get(1));
    }

    public void test73() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        path= wrap(wrap(path).prepend(t2));
        assertException(IndexOutOfBoundsException.class, () -> path.get(2));
        assertException(IndexOutOfBoundsException.class, () -> path.get(-1));
    }

    public void test74() {
        t1.dependsOn(t2);
        t3.dependsOn(t1);
        path= new DegeneratePath(t1);
        path= path.append(t3).prepend(t2);
        assertSame(t2, path.get(0));
        assertSame(t1, path.get(1));
        assertSame(t3, path.get(2));
    }

    public void test75() {
        Task[] t= tasks("1<2<3>5 2<4<5<1>3 6 0");
        Path path1= new DegeneratePath(t[1]).append(t[2]).append(t[4]);
        Path path2= new DegeneratePath(t[1]).prepend(t[5]).prepend(t[4]);
        Path path3= new ConcatPath(path1, path2);
        Path path4= new DegeneratePath(t[2]).prepend(t[1]);
        Path path5= new DegeneratePath(t[2]).append(t[3]);
        Path path6= new ConcatPath(path4, path5);
        path= new ConcatPath(path3, path6);
        assertSame(t[1], path.get(0));
        assertSame(t[2], path.get(1));
        assertSame(t[4], path.get(2));
        assertSame(t[5], path.get(3));
        assertSame(t[1], path.get(4));
        assertSame(t[2], path.get(5));
        assertSame(t[3], path.get(6));
        assertException(IndexOutOfBoundsException.class, () -> path.get(7));
    }

    protected Path wrap(Path p) {
        return new Path() {

            @Override
            public int size() {
                return p.size();
            }

            @Override
            public Task getFirst() {
                return p.getFirst();
            }

            @Override
            public Task getLast() {
                return p.getLast();
            }

            @Override
            protected void toArrayHelper(Stack<Work> worklist, Task[] array, int index) {
                worklist.push(new Work(p, index));
            }

            // @Override
            @Override
            protected boolean containsHelper(Stack<Work> worklist, Task query) {
                worklist.push(new Work(p, 0));
                return false;
            }

            // @Override
            @Override
            protected Task getHelper(Stack<Work> worklist, int index) {
                worklist.push(new Work(p, index));
                return null;
            }

        };
    }
}
