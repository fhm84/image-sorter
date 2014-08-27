package de.halbmann.imagesorter.modifier;

import java.util.List;

import de.halbmann.imagesorter.model.ImageFile;

/**
 * Interface for various modifier implementations.
 * 
 * @author fabian
 * 
 */
public interface ImageModifier {

	/**
	 * Modify the {@link ImageFile}s.
	 * 
	 * @param images
	 *            the list of the image-files read
	 * 
	 *            TODO: throws Exception?
	 */
	void modify(List<ImageFile> images);
}
