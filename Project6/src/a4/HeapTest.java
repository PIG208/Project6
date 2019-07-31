package a4;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;




class HeapTest {

	@Test
	void test() {
		compare cmp = new compare();
		// The larger the integer is, the larger the priority gets. 
		Heap<String, Integer> heap = new Heap<String, Integer>(cmp);
		
		// Test add()
		heap.add("Tiantian", 10);
		assertEquals("[Tiantian]", heap.toString());
		heap.add("Jasmine", 8);
		assertEquals("[Tiantian, Jasmine]", heap.toString());
		heap.add("Michael", 13);
		assertEquals("[Michael, Jasmine, Tiantian]", heap.toString());
		heap.assertInvariants();
		assertThrows(IllegalArgumentException.class, () -> {heap.add("Tiantian", 10);});
		heap.add("Helen", 12);
		assertEquals("[Michael, Helen, Tiantian, Jasmine]", heap.toString());
		heap.add("Joyce", 20);
		assertEquals("[Joyce, Michael, Tiantian, Jasmine, Helen]", heap.toString());

		
		// Test poll()
		heap.poll();
		assertEquals("[Michael, Helen, Tiantian, Jasmine]", heap.toString());
		heap.assertInvariants();
		
		// Test peek()
		assertEquals("Michael", heap.peek());
		heap.assertInvariants();
		
		// Test changePriority()
		heap.changePriority("Jasmine", 100);
		assertEquals("[Jasmine, Michael, Tiantian, Helen]", heap.toString());
		heap.assertInvariants();
		assertThrows(IllegalArgumentException.class, () -> {heap.changePriority("Alex", 1);});
		
		// Test poll()
		heap.poll();
		heap.poll();
		assertEquals("[Helen, Tiantian]", heap.toString());
		heap.poll();
		assertEquals("[Tiantian]", heap.toString());
		heap.poll();
		assertThrows(NoSuchElementException.class, () -> {heap.poll();});
		assertThrows(NoSuchElementException.class, () -> {heap.peek();});
		
	}

	
	class compare implements Comparator<Integer>{
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	}

}
