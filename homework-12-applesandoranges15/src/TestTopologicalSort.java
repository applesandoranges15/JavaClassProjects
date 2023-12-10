import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Path;
import edu.uwm.cs351.Schedule;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskFactory;
import edu.uwm.cs351.TopologicalSort;

public class TestTopologicalSort extends LockedTestCase {
    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
    	try {
    		r.run();
    		assertFalse("Exception should have been thrown",true);
        } catch (RuntimeException ex) {
        	if (!c.isInstance(ex)) {
        		ex.printStackTrace();
        	}
        	assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
        }	
    }	

    @SafeVarargs
	protected static <T> void assertOneOf(T test, T... options) {
    	for (T opt : options) {
    		if (opt == null) {
    			if (opt == test) return;
    			else continue;
    		}
    		if (opt.equals(test)) return;
    	}
    	assertFalse("Expected one of " + Arrays.toString(options) + ": not " + test, true);
    }
    
	protected Task[] tasks(String x) {
		Collection<Task> c = new TaskFactory().fromString(x);
		Task[] result = new Task[10];
		for (Task t : c) {
			result[Integer.parseInt(t.getName())] = t;
		}
		return result;
	}

	protected Set<Task> s(Task...tasks) {
		Set<Task> result = new LinkedHashSet<>();
		for (Task t : tasks) {
			result.add(t);
		}
		return result;
	}
	
	private TopologicalSort topo;
    
	
	@Override
	protected void setUp() {
		try {
			assert topo.getCycle() == null;
			throw new IllegalStateException("assertions must be enabled to run this test");
		} catch (NullPointerException ex) {
			// OK!
		}
	}
	
	/// locked tests
	
	public void test() {
		Task[] t = tasks("1<2 1<3"); // define Tasks t[1], t[2], t[3]
		topo = new TopologicalSort(s(t[1], t[2], t[3]));
		List<Task> schedule = topo.getSchedule().asList(); 
		assertOneOf(schedule.toString(), "[1, 2, 3]", Ts(1863908123));
		t = tasks("1<2 2<3 3<2");
		topo = new TopologicalSort(s(t[1], t[2], t[3]));
		Path cycle = topo.getCycle();
		assertOneOf(cycle.toString(), "2<3<2", Ts(237308075));
	}
	

	public void test0() {
		topo = new TopologicalSort(s());
		assertNull(topo.getCycle());
		assertEquals(Arrays.asList(), topo.getSchedule().asList());
	}
	
	public void test1() {
		Task t = new Task("task");
		topo = new TopologicalSort(s(t));
		assertNull(topo.getCycle());
		assertEquals(Arrays.asList(t), topo.getSchedule().asList());
	}
	
	public void test2() {
		Task[] t = tasks("1>2");
		topo = new TopologicalSort(s(t[1], t[2]));
		assertNull(topo.getCycle());
		assertEquals(Arrays.asList(t[2], t[1]), topo.getSchedule().asList());
	}
	
	public void test3() {
		Task[] t = tasks("0<2>1");
		topo = new TopologicalSort(s(t[0], t[1], t[2]));
		assertNull(topo.getCycle());
		List<Task> sch = topo.getSchedule().asList();
		assertOneOf(sch.toString(), "[0, 1, 2]", "[1, 0, 2]");
	}
	
	public void test4() {
		Task[] t = tasks("0<2<3 1<3 1<4<2");
		topo = new TopologicalSort(s(t[0], t[1], t[2], t[3]));
		assertNull(topo.getCycle());
		List<Task> sch = topo.getSchedule().asList();
		assertEquals(4, sch.size()); // don't schedule things we didn't ask for
		assertOneOf(sch.toString(), "[0, 1, 2, 3]", "[1, 0, 2, 3]", "[1, 2, 0, 3]");
	}
	
	public void test5() {
		Task[] t = tasks("0<1 0<2 0<3 1<3 2<4 3<4 2<5 3<5 6<0 6<6 3<6");
		topo = new TopologicalSort(s(t[5], t[4], t[3], t[2], t[1], t[0]));
		assertNull(topo.getCycle());
		List<Task> sch = topo.getSchedule().asList();
		assertOneOf(sch.toString(), 
				"[0, 1, 2, 3, 4, 5]", 
				"[0, 1, 3, 2, 4, 5]",
				"[0, 1, 3, 2, 5, 4]",
				"[0, 2, 1, 3, 4, 5]", 
				"[0, 2, 1, 3, 5, 4]");		
	}
	
	public void test6() {
		Task[] t = tasks("0<5<9 1<6 2<5<8 3<7 4<9 2<4<3<5 0<2 4<5 6<8<9 1<3 7<9");
		topo = new TopologicalSort(
				s(t[9], t[8], t[7], t[6], t[5], 
				  t[4], t[3], t[2], t[1], t[0]));
		assertNull(topo.getCycle());
		Schedule sch = topo.getSchedule();
		for (int i=0; i < 10; ++i) {
			assertTrue(sch.contains(t[i]));
		}
		assertEquals(10, sch.asList().size());
	}
	
	public void test7() {
		Task[] t = tasks("0<1<2<0 0<3<4");
		topo = new TopologicalSort(s(t[0], t[1], t[2], t[3], t[4]));
		Path p = topo.getCycle();
		assertEquals(4, p.size());
		assertOneOf(p.toString(), "0<1<2<0", "1<2<0<1", "2<0<1<2");
	}
	
	public void test8() {
		Task[] t = tasks("0<1<6 2<0 3<4<5<3<2");
		topo = new TopologicalSort(s(t[0], t[1], t[2], t[3], t[4], t[5], t[6]));
		Path p = topo.getCycle();
		assertEquals(4, p.size());
		assertOneOf(p.toString(), "3<4<5<3", "4<5<3<4", "5<3<4<5");
	}
	
	public void test9() {
		Task[] t = tasks("9>8>6 9>7>5 8>7>4 6>4>2 5>5 3>2>1>0");
		topo = new TopologicalSort(
				s(t[9], t[8], t[7], t[6], t[5], 
				  t[4], t[3], t[2], t[1], t[0]));
		Path p = topo.getCycle();
		assertEquals(2, p.size());
		assertEquals("5<5", p.toString());
	}
}
