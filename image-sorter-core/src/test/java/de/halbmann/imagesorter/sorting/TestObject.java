/**
 * 
 */
package de.halbmann.imagesorter.sorting;

/**
 * @author fabian
 * 
 */
public class TestObject {

	String arg0;
	String arg1;
	String arg2;
	String arg3;

	public TestObject(String arg0, String arg1, String arg2, String arg3) {
		this.arg0 = arg0;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
	}

	@Override
	public String toString() {
		return arg0 + " - " + arg1 + " - " + arg2 + " - " + arg3;
	}
}
