package de.halbmann.imagesorter.files;

import java.util.List;

import de.halbmann.imagesorter.ImageSorterSettings;
import de.halbmann.imagesorter.model.ImageFile;

/**
 * Default filename calculator implementation. This will calculate the filenames based on the
 * prefix, the suffix, and the position of the image in the (sorted) list including the specified
 * offset.
 * <p>
 * The pattern for the new filenames is the following:<br/>
 * prefix + (index + offset) + suffix
 * </p>
 * 
 * @author fabian
 *
 */
public class DefaultFilenameCalculator implements FilenameCalculator {

	/**
	 * the prefix for the new renamed files.
	 */
	private String prefix;

	/**
	 * the suffix for the new renamed files.
	 */
	private String suffix;

	/**
	 * the offset for the number of the renamed files.
	 */
	private int offset;

	/**
	 * This is used for the number-pattern; for the number of leading zeros.
	 */
	private int numberOfDigits = -1;

	/**
	 * This is the minimum number of digits; can be configured from outside. (Later ;)
	 */
	private int minNumberOfDigits = 1;

	public DefaultFilenameCalculator(ImageSorterSettings settings) {
		this.prefix = settings.getPrefix() == null ? "" : settings.getPrefix();
		this.suffix = settings.getSuffix() == null ? "" : settings.getSuffix();
		this.offset = settings.getOffset();
		this.minNumberOfDigits = settings.getNumberOfDigits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.halbmann.imagesorter.files.FilenameCalculator#calculate(java.util.List)
	 */
	@Override
	public void calculate(List<ImageFile> images) {
		numberOfDigits = calculateNumberOfDigits(images);

		for (int i = 0; i < images.size(); i++) {
			ImageFile imageFile = images.get(i);

			if (imageFile != null) {
				imageFile.setCalculatedFilename(calculateFilename(i,
						imageFile.getOriginalFilename()));
			}
		}
	}

	/**
	 * Calculate the number of digits to use for the (new) filename. There can be also set the
	 * minimal number of digits to use. The calculated number of digits depends on the number of
	 * images to process and the offset.
	 * 
	 * @param images
	 *            the list of images to calculate the number of digits for
	 * @return the calculated number of digits to use for naming the files
	 */
	protected int calculateNumberOfDigits(List<ImageFile> images) {
		numberOfDigits = 1;
		int numberOfImages = images.size() + offset;
		int factor = 10;
		while ((numberOfImages / factor) > 1) {
			factor *= 10;
			numberOfDigits++;
		}
		// the minNumberOfDigits can override the calculated nubmer
		return Math.max(numberOfDigits, minNumberOfDigits);
	}

	/**
	 * Calculate the new filename for the file at the given index.
	 * 
	 * @param index
	 *            index of the file in the sorted list
	 * @return the name of the new filename
	 */
	protected String calculateFilename(int index) {
		return calculateFilename(index, null);
	}

	/**
	 * Calculate the new filename for the file at the given index. The new filename is the prefix +
	 * (index + offset) + suffix.
	 * 
	 * @param index
	 *            index of the file in the sorted list
	 * @param originalFilename
	 *            the original filename of the image
	 * @return the name of the new filename
	 */
	protected String calculateFilename(int index, String originalFilename) {
		StringBuilder pattern = new StringBuilder("%s%0");
		pattern.append(numberOfDigits);
		pattern.append("d%s");
		// TODO: add multiple options for the filenames (possibility to include the original
		// filename?, ...)
		return String.format(pattern.toString(), prefix, index + offset, suffix);
	}

	// getters and setters

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	public void setNumberOfDigits(int numberOfDigits) {
		this.numberOfDigits = numberOfDigits;
	}

	public int getMinNumberOfDigits() {
		return minNumberOfDigits;
	}

	public void setMinNumberOfDigits(int minNumberOfDigits) {
		this.minNumberOfDigits = minNumberOfDigits;
	}

}
