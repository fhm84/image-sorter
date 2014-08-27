package de.halbmann.imagesorter.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Ignore;
import org.junit.Test;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

/**
 * @author fabian
 * 
 */
@Ignore
public class JPEGMetadataPerformanceTest {

	String path = "target/test-classes/images";

	@Test
	public void test_MultipleThreads() throws IOException {
		ImageFileVisitor ft = new ImageFileVisitor(true);
		Path start = FileSystems.getDefault().getPath(path);
		Files.walkFileTree(start, ft);

		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService es = Executors.newFixedThreadPool(cores);
		CompletionService<Object> service = new ExecutorCompletionService<Object>(es);

		long startTime = System.currentTimeMillis();
		for (final File f : ft.getFiles()) {
			service.submit(new Callable<Object>() {

				public Object call() throws Exception {
					readMetaData(f);
					return null;
				}
			});
		}

		int numberOfFiles = ft.getFiles().size();
		for (int i = 0; i < numberOfFiles; i++) {
			try {
				service.take().get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();
		es.shutdown();

		System.out.println("***");
		System.out.println("Zusammenfassung: ");
		System.out.println("Number of cores: " + cores);
		System.out.println("Anzahl Bilder: " + numberOfFiles);
		System.out.println(String.format("Dauer: %d ms", endTime - startTime));
	}

	@Test
	public void test_SingleThread() throws IOException, JpegProcessingException {
		ImageFileVisitor ft = new ImageFileVisitor(true);
		Path start = FileSystems.getDefault().getPath(path);
		Files.walkFileTree(start, ft);

		long startTime = System.currentTimeMillis();
		int numberOfFiles = ft.getFiles().size();
		for (final File f : ft.getFiles()) {
			readMetaData(f);
		}

		long endTime = System.currentTimeMillis();

		System.out.println("***");
		System.out.println("Zusammenfassung: ");
		System.out.println("Anzahl Bilder: " + numberOfFiles);
		System.out.println(String.format("Dauer: %d ms", endTime - startTime));
	}

	private void readMetaData(File f) throws JpegProcessingException, IOException {
		Metadata metadata = JpegMetadataReader.readMetadata(f);

		Directory exifDirectory = metadata.getDirectory(ExifIFD0Directory.class);
		String cameraOrientation = exifDirectory.getString(ExifIFD0Directory.TAG_ORIENTATION);

		Date aufnahmeZeitpunkt = exifDirectory.getDate(ExifIFD0Directory.TAG_DATETIME);

		System.out.println(f.getName() + ": " + cameraOrientation + " - " + aufnahmeZeitpunkt);
	}
}
