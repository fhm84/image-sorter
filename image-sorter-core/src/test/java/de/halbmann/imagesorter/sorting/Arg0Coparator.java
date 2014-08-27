/**
 * 
 */
package de.halbmann.imagesorter.sorting;

import java.util.Comparator;

/**
 * @author fabian
 * 
 */
public class Arg0Coparator implements Comparator<TestObject> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(TestObject o1, TestObject o2) {
		return o1.arg0.compareTo(o2.arg0);
	}

}
