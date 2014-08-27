package de.halbmann.imagesorter.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generic implementation of a comparator. The constructor gets a list of comparators. In the same
 * order, the comparators are set to the constructor, they will be processed. So you can order a
 * list by multiple criteria.
 * 
 * @author fabian
 * 
 */
public class GenericComparator<T> implements Comparator<T> {

	private List<Comparator<T>> comparators = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param comparators
	 *            List of Comparators for the elements
	 */
	public GenericComparator(List<Comparator<T>> comparators) {
		if (comparators != null && !comparators.isEmpty()) {
			this.comparators = comparators;
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param comparators
	 *            List of Comparators for the elements
	 */
	@SafeVarargs
	public GenericComparator(Comparator<T>... comparators) {
		for (Comparator<T> c : comparators) {
			this.comparators.add(c);
		}
	}

	@Override
	public int compare(T o1, T o2) {
		// compare the elements with all comparators, that return 0 as result
		// (in the same ordering, the constructor gets the list of comparators).
		for (Comparator<T> c : comparators) {
			int res = c.compare(o1, o2);
			if (res != 0) {
				return res;
			}
		}
		return 0;
	}
}
