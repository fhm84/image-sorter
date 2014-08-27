package de.halbmann.imagesorter.files;

import java.util.List;

import de.halbmann.imagesorter.model.ImageFile;

/**
 * Interface for various filename calculators. A FilenameCalculator implementation will be used for
 * the new filenames.
 * 
 * @author fabian
 *
 */
public interface FilenameCalculator {

	/**
	 * Calculate the (final) filenames for the image-files to write.
	 * 
	 * @param images
	 *            the list of the image-files to calculate the (new) filenames for
	 */
	void calculate(List<ImageFile> images);
}
