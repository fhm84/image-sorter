package de.halbmann.imagesorter.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.halbmann.imagesorter.ImageSorterSettings;
import de.halbmann.imagesorter.model.ImageFile;

public class DefaultFilenameCalculatorTest {

	@Test
	public void type() throws Exception {
		assertThat(DefaultFilenameCalculator.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		FilenameCalculator target = new DefaultFilenameCalculator(new ImageSorterSettings());
		assertThat(target, notNullValue());
	}

	@Test
	public void calculateFilename() throws Exception {
		ImageSorterSettings settings = new ImageSorterSettings(null, "pre", "suf", 1);
		DefaultFilenameCalculator target = new DefaultFilenameCalculator(settings);
		target.setNumberOfDigits(4);
		String actual = target.calculateFilename(3);
		String expected = "pre0004suf";
		assertThat(actual, is(equalTo(expected)));

		target.setOffset(0);
		target.setSuffix("");
		actual = target.calculateFilename(0);
		expected = "pre0000";
		assertThat(actual, is(equalTo(expected)));

		target.setNumberOfDigits(2);
		target.setOffset(8);
		actual = target.calculateFilename(3);
		expected = "pre11";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void shouldCalculateNumberOfDigits() {
		ImageSorterSettings settings = new ImageSorterSettings(null, "pre", "suf", 1);
		DefaultFilenameCalculator target = new DefaultFilenameCalculator(settings);
		List<ImageFile> images = new ArrayList<>();
		int actual = target.calculateNumberOfDigits(images);
		int expected = 1;

		assertThat(actual, is(expected));

		for (int i = 0; i < 9; i++) {
			images.add(new ImageFile());
		}
		assertThat(images.size(), is(9));
		actual = target.calculateNumberOfDigits(images);
		expected = 1;

		for (int i = 0; i < 80; i++) {
			images.add(new ImageFile());
		}
		assertThat(images.size(), is(89));
		actual = target.calculateNumberOfDigits(images);
		expected = 2;

		for (int i = 0; i < 120; i++) {
			images.add(new ImageFile());
		}
		assertThat(images.size(), is(209));
		actual = target.calculateNumberOfDigits(images);
		expected = 3;

		assertThat(actual, is(expected));

		for (int i = 0; i < 1000; i++) {
			images.add(new ImageFile());
		}
		assertThat(images.size(), is(1209));
		actual = target.calculateNumberOfDigits(images);
		expected = 4;
	}

}
