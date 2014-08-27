package de.halbmann.imagesorter.files;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ImageFileWriterTest {

	@Test
	public void type() throws Exception {
		assertThat(ImageFileWriter.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		ImageFileWriter target = new ImageFileWriter();
		assertThat(target, notNullValue());
	}

}
