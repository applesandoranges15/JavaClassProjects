import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.uwm.cs351.ConcatPath;
import edu.uwm.cs351.DegeneratePath;
import edu.uwm.cs351.Path;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TopologicalSort;
import junit.framework.TestCase;

public class TestEfficiency extends TestCase {
	private static final int POWER = 20;
	private static final int MAX = (1 << POWER);
	private static final int TESTS = 10000;
	
	private static Task[] t; // reuse for test speed
	private Path path;
	
	@Override
	public void setUp() {
		try {
			assert 1/(t.length - MAX -1) == 42 : "OK";
			assertTrue(true);
		} catch (RuntimeException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		if (t != null) return;
		t = new Task[MAX+1];
		for (int i=0; i <= MAX; ++i) {
			t[i] = new Task(""+i); 
			if (i > 0) t[i].dependsOn(t[i-1]);
			for (int pow = 1; pow < 3; ++pow) {
				int val = 1 << pow;
				if ((i & val) > 0) t[i].dependsOn(t[i&~val]);
			}		
		}
		t[0].dependsOn(t[MAX]);
	}

	public void testA() {
		path = new DegeneratePath(t[0]);
		for (int i=1; i < MAX; ++i) {
			path = path.append(t[i]);
		}
		for (int i=0; i < TESTS; ++i) {
			assertEquals(MAX, path.size());
		}
	}
	
	public void testB() {
		path = new DegeneratePath(t[0]);
		for (int i=1; i < MAX; ++i) {
			path = path.append(t[i]);
		}
		for (int i=0; i < TESTS; ++i) {
			assertSame(t[0], path.getFirst());
		}
	}
	
	public void testC() {
		path = new DegeneratePath(t[MAX-1]);
		for (int i=MAX-2; i >= 0; --i) {
			path = path.prepend(t[i]);
		}
		for (int i=0; i < TESTS; ++i) {
			assertEquals(MAX, path.size());
		}
	}
	
	public void testD() {
		path = new DegeneratePath(t[MAX-1]);
		for (int i=MAX-2; i >= 0; --i) {
			path = path.prepend(t[i]);
		}
		for (int i=0; i < TESTS; ++i) {
			assertSame(t[MAX-1], path.getLast());
		}
	}
	
	public void testE() {
		path = new DegeneratePath(t[0]);
		for (int i=1; i < MAX; ++i) {
			Path path2 = new DegeneratePath(t[i-1]).append(t[i]);
			path = path.concat(path2);
		}
		for (int i=0; i < TESTS; ++i) {
			assertSame(t[0], path.getFirst());
		}
	}

	public void testF() {
		path = new DegeneratePath(t[MAX-1]);
		for (int i=MAX-2; i >= 0; --i) {
			Path path2 = new DegeneratePath(t[i+1]).prepend(t[i]);
			path = path2.concat(path);
		}
		for (int i=0; i < TESTS; ++i) {
			assertSame(t[MAX-1], path.getLast());
		}
	}

	public void testG() {
		int mid = MAX/2;
		path = new DegeneratePath(t[mid-1]).append(t[mid]);
		for (int i=1; i < mid; ++i) {
			Path plo, phi;
			if ((i & 1) == 0) {
				plo = new DegeneratePath(t[mid-1-i]).append(t[mid-i]);
				phi = new DegeneratePath(t[mid-1+i]).append(t[mid+i]);
			} else {
				plo = new DegeneratePath(t[mid-i]).prepend(t[mid-1-i]);
				phi = new DegeneratePath(t[mid+i]).prepend(t[mid-1+i]);
			}
			path = new ConcatPath(plo,path);
			path = new ConcatPath(path,phi);
		}
		for (int i=0; i < TESTS; ++i) {
			assertEquals(MAX, path.size());
		}
	}
	
	public void testH() {
		int mid = MAX/2;
		Path path1 = new DegeneratePath(t[0]);
		for (int i=1; i <= mid; ++i) {
			path1 = path1.append(t[i]);
		}
		Path path2 = new DegeneratePath(t[MAX-1]);
		for (int i=MAX-2; i >= mid; --i) {
			path2 = path2.prepend(t[i]);
		}
		path = new ConcatPath(path1, path2);
		for (int i=0; i < TESTS; ++i) {
			assertEquals(t[0], path.getFirst());
			assertEquals(t[MAX-1], path.getLast());
			assertEquals(MAX, path.size());
		}		
	}
	
	public void testI() {
		path = new DegeneratePath(t[0]);
		for (int i=1; i < MAX; ++i) {
			path = path.append(t[i]);
		}
		Task[] array = path.toArray();
		for (int i=0; i < MAX; ++i) {
			assertEquals("task[" + i + "]", t[i], array[i]);
		}
	}
	
	public void testJ() {
		path = new DegeneratePath(t[MAX-1]);
		for (int i=MAX-2; i >= 0; --i) {
			path = path.prepend(t[i]);
		}
		Task[] array = path.toArray();
		for (int i=0; i < MAX; ++i) {
			assertEquals("task[" + i + "]", t[i], array[i]);
		}
	}
	
	protected void makeComplexPath() {
		int mid = MAX/2;
		path = new DegeneratePath(t[mid-1]).append(t[mid]);
		for (int i=1; i < mid; ++i) {
			switch(i&3) {
			default:
			case 0:
				path = path.append(t[mid+i]);
				path = new DegeneratePath(t[mid-i]).prepend(t[mid-i-1]).concat(path);
				break;
			case 1:
				path = path.concat(new DegeneratePath(t[mid+i]).prepend(t[mid+i-1]));
				path = path.prepend(t[mid-i-1]);
				break;
			case 2:
				path = path.prepend(t[mid-i-1]);
				path = path.concat(new DegeneratePath(t[mid+i-1]).append(t[mid+i]));
				break;
			case 3:
				path = new DegeneratePath(t[mid-i-1]).append(t[mid-i]).concat(path);
				path = path.append(t[mid+i]);
				break;
			}
		}
	}
	
	public void testK() {
		makeComplexPath();
		Task[] array = path.toArray();
		for (int i=0; i < MAX; ++i) {
			assertEquals("task[" + i + "]", t[i], array[i]);
		}
	}
	
	protected Task fake(Task t) {
		Task result = new Task(t.getName());
		for (Task d : t.getDependencies()) {
			result.dependsOn(d);
		}
		return result;
	}
	
	public void testL() {
		makeComplexPath();
		Task target = t[MAX/2];
		assertTrue(path.contains(target));
		assertFalse(path.contains(fake(target)));
		assertTrue(path.contains(t[MAX*3/4]));
		assertFalse(path.contains(fake(t[MAX/4])));
	}
	
	public void testM() {
		makeComplexPath();
		assertSame(t[MAX/2-5], path.get(MAX/2-5));
		assertSame(t[MAX/2-2], path.get(MAX/2-2));
		assertSame(t[MAX/2-1], path.get(MAX/2-1));
		assertSame(t[MAX/2], path.get(MAX/2));
		assertSame(t[MAX/2+1], path.get(MAX/2+1));
		assertSame(t[MAX/2+3], path.get(MAX/2+3));		
	}
	
	
	/// tests of topological sort
	
	
	public void testN() {
		Set<Task> normal = new HashSet<>();
		for (int i=0; i < MAX; ++i) {
			normal.add(t[i]);
		}
		TopologicalSort topo = new TopologicalSort(normal);
		assertNull(topo.getCycle());
		assertEquals(MAX, topo.getSchedule().asList().size());
	}
	
	public void testO() {
		Set<Task> cyclic = new HashSet<>(Arrays.asList(t));
		TopologicalSort topo = new TopologicalSort(cyclic);
		Path cycle = topo.getCycle();
		assertNotNull(cycle);
		assertTrue(cycle.isCycle());
	}
}
