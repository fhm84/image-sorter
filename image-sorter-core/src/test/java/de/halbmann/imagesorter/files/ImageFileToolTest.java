package de.halbmann.imagesorter.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

public class ImageFileToolTest {

	@Test
	public void type() throws Exception {
		assertThat(ImageFileVisitor.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		ImageFileVisitor target = new ImageFileVisitor();
		assertThat(target, notNullValue());
	}

	@Test
	public void walk_A$Path() throws Exception {
		ImageFileVisitor target = new ImageFileVisitor();
		Path pathFile = FileSystems.getDefault().getPath(
				"./src/test/resources/images");
		List<File> actual = target.walk(pathFile);
		assertThat(actual.size(), is(equalTo(1)));
	}

	@Test
	public void walk_A$Path_T$Exception() throws Exception {
		ImageFileVisitor target = new ImageFileVisitor();
		Path pathFile = null;
		try {
			target.walk(pathFile);
			fail("Expected exception was not thrown!");
		} catch (Exception e) {
		}
	}

	@Test
	public void walk_A$Path$boolean() throws Exception {
		ImageFileVisitor target = new ImageFileVisitor();
		Path pathFile = FileSystems.getDefault().getPath(
				"./src/test/resources/images");
		boolean recursive = true;
		List<File> actual = target.walk(pathFile, recursive);
		assertThat(actual.size(), is(equalTo(2)));
	}

}
