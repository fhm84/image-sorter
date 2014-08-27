package de.halbmann.imagesorter.sorting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author fabian
 * 
 */
public class SortingTest {

	@Test
	public void type() throws Exception {
		assertThat(GenericComparator.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		List<Comparator<TestObject>> comparators = null;
		GenericComparator<TestObject> target = new GenericComparator<>(
				comparators);
		assertThat(target, notNullValue());
	}

	@Test
	public void testStandardSorting() {
		List<String> l;
		String[] s = { "C", "d", "c", "b", "A" };
		l = Arrays.asList(s);

		Collections.sort(l);
		String[] se = { "A", "C", "b", "c", "d" };
		List<String> expected = Arrays.asList(se);

		assertThat(l, is(equalTo(expected)));

		Collections.sort(l, String.CASE_INSENSITIVE_ORDER);
		String[] se2 = { "A", "b", "C", "c", "d" };
		expected = Arrays.asList(se2);
		assertThat(l, is(equalTo(expected)));

		Collections.sort(l, Collections.reverseOrder());
		String[] se3 = { "d", "c", "b", "C", "A" };
		expected = Arrays.asList(se3);
		assertThat(l, is(equalTo(expected)));
	}

	@Test
	public void compare_TestObjects() throws Exception {
		TestObject t1, t2, t3, t4, t5, t6, t7;
		t1 = new TestObject("aaa", "ccc", "fff", "bbb");
		t2 = new TestObject("aaa", "bbb", "lll", "bbb");
		t3 = new TestObject("aaa", "bbb", "aaa", "ggg");
		t4 = new TestObject("ccc", "ccc", "fff", "bbb");
		t5 = new TestObject("ccc", "fff", "aaa", "fff");
		t6 = new TestObject("ccc", "bbb", "aaa", "ccc");
		t7 = new TestObject("aaa", "ccc", "eee", "bbb");

		TestObject[] objects = { t1, t2, t3, t4, t5, t6, t7 };
		List<TestObject> list = Arrays.asList(objects);

		GenericComparator<TestObject> c = new GenericComparator<>(
				new Arg0Coparator(), new Arg1Coparator(), new Arg2Coparator(),
				new Arg3Coparator());
		Collections.sort(list, c);

		TestObject[] objects_e = { t3, t2, t7, t1, t6, t4, t5 };
		List<TestObject> expected = Arrays.asList(objects_e);

		assertThat(list, is(equalTo(expected)));

		// second Test
		list = Arrays.asList(objects);
		c = new GenericComparator<>(new Arg2Coparator(), new Arg1Coparator(),
				new Arg0Coparator(), new Arg3Coparator());
		Collections.sort(list, c);

		TestObject[] objects_e2 = { t3, t6, t5, t7, t1, t4, t2 };
		expected = Arrays.asList(objects_e2);

		assertThat(list, is(equalTo(expected)));
	}

}
