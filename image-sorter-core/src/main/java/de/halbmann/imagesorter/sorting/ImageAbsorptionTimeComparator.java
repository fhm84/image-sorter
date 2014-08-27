package de.halbmann.imagesorter.sorting;

import java.util.Comparator;
import java.util.Date;

import de.halbmann.imagesorter.model.ImageFile;

/**
 * Comparator implementation for comparing the absorption time of the images. This will order the
 * {@link ImageFile}-objects by the absorption time. If this time was modified by the user, this
 * comparator will use the modified absorption time, otherwise the original absorption time will be
 * used.
 * 
 * @author fabian
 * 
 */
public class ImageAbsorptionTimeComparator implements Comparator<ImageFile> {

	@Override
	public int compare(ImageFile image0, ImageFile image1) {
		if (image0 == null && image1 == null) {
			return 0;
		}
		if (image0 == null) {
			return 1;
		}
		if (image1 == null) {
			return -1;
		}

		Date d0 = null;
		if (image0.getModifiedAbsorptionTime() != null) {
			d0 = image0.getModifiedAbsorptionTime();
		} else {
			d0 = image0.getOriginalAbsorptionTime();
		}
		Date d1 = null;
		if (image1.getModifiedAbsorptionTime() != null) {
			d1 = image1.getModifiedAbsorptionTime();
		} else {
			d1 = image1.getOriginalAbsorptionTime();
		}

		return d0.compareTo(d1);
	}

}
