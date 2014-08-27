package de.halbmann.imagesorter.files;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

import de.halbmann.imagesorter.model.ImageFile;
import de.halbmann.imagesorter.sorting.GenericComparator;
import de.halbmann.imagesorter.sorting.ImageAbsorptionTimeComparator;

@Ignore
public class ImageMetadataReaderTest {

	@Test
	public void type() throws Exception {
		assertThat(ImageMetadataReader.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		ImageMetadataReader target = new ImageMetadataReader();
		assertThat(target, notNullValue());
	}

	@Test
	public void process_A$String() throws Exception {
		ImageMetadataReader target = new ImageMetadataReader();
		String path = "target/test-classes/images";
		target.readImageFiles(path);

		long startTime = System.currentTimeMillis();
		Collections.sort(target.getImages(), new GenericComparator<>(
				new ImageAbsorptionTimeComparator()));
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("duration for sorting: %d ms", endTime - startTime));

		System.out.println("camera-models: " + target.getCameraModels());
		System.out.println("Images: " + target.getImages().size());
		for (ImageFile i : target.getImages()) {
			System.out.println(i.getFile().getName() + " - " + i.getOrientation() + " - "
					+ i.getOriginalAbsorptionTime() + " - camera: " + i.getCameraModel());
		}
	}

}
