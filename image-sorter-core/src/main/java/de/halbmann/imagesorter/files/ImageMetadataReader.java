package de.halbmann.imagesorter.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import de.halbmann.imagesorter.model.ImageFile;

/**
 * Main class for reading the image files with the metadata.
 * 
 * @author fabian
 * 
 */
public class ImageMetadataReader extends Observable {

	private static final Logger LOG = Logger.getLogger(ImageMetadataReader.class.getName());

	/**
	 * List of images found in the directory.
	 */
	private List<ImageFile> images = new ArrayList<>();

	/**
	 * List of different camera models found.
	 */
	private Set<String> cameraModels = new HashSet<>();

	/**
	 * The ImageFileVisitor. FileVisitor-Implementation to get the list of image files.
	 */
	private ImageFileVisitor fileVisitor;

	/**
	 * Default constructor.
	 */
	public ImageMetadataReader() {
		fileVisitor = new ImageFileVisitor(true);
	}

	/**
	 * Constructor. The recursive flag enables or disables the walk through the subdirectories.
	 * 
	 * @param recursive
	 *            read images from subdirectories
	 */
	public ImageMetadataReader(boolean recursive) {
		fileVisitor = new ImageFileVisitor(recursive);
	}

	/**
	 * Get the list of images.
	 * 
	 * @return the list of images
	 */
	public List<ImageFile> getImages() {
		return images;
	}

	/**
	 * Get the list of different camera models.
	 * 
	 * @return the set of the camera models
	 */
	public Set<String> getCameraModels() {
		return cameraModels;
	}

	/**
	 * Process the given Path. All images in the given path will be read and the information stored
	 * in the list.
	 * 
	 * @param path
	 *            the path to read the images from
	 * @return the list of image files read
	 * @throws IOException
	 */
	public List<ImageFile> readImageFiles(String path) throws IOException {
		Path start = FileSystems.getDefault().getPath(path);
		Files.walkFileTree(start, fileVisitor);

		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService es = Executors.newFixedThreadPool(cores);
		CompletionService<Object> service = new ExecutorCompletionService<Object>(es);

		LOG.log(Level.FINE, "reading image files - path: {0}", path);
		long startTime = System.currentTimeMillis();
		for (final File f : fileVisitor.getFiles()) {
			service.submit(new Callable<Object>() {

				public Object call() throws Exception {
					readMetaData(f);
					return null;
				}
			});
		}

		int numberOfFiles = fileVisitor.getFiles().size();
		for (int i = 0; i < numberOfFiles; i++) {
			try {
				service.take().get();
				notifyObservers((double) i / numberOfFiles);
				setChanged();
			} catch (InterruptedException e) {
				LOG.log(Level.SEVERE, e.getMessage(), e);
			} catch (ExecutionException e) {
				LOG.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		long endTime = System.currentTimeMillis();
		clearChanged();
		LOG.log(Level.INFO, "number of files: {0}", numberOfFiles);
		LOG.log(Level.FINE, "duration: {0} ms", endTime - startTime);
		es.shutdown();

		return images;
	}

	/**
	 * Read the metadata of the file, create a ImageFile-object and add it to the list of images.
	 * 
	 * @param f
	 *            the file to read the metadata from
	 * @throws JpegProcessingException
	 * @throws IOException
	 * @throws MetadataException
	 */
	private void readMetaData(File f) throws JpegProcessingException, IOException,
			MetadataException {
		LOG.log(Level.FINER, "read metadata - file: {0}", f.getName());
		// read the metadata
		Metadata metadata = JpegMetadataReader.readMetadata(f);

		Directory exifDirectory = metadata.getDirectory(ExifIFD0Directory.class);
		int cameraOrientation = exifDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		LOG.log(Level.FINER, "camera orientation: {0}", cameraOrientation);

		String camera = exifDirectory.getString(ExifIFD0Directory.TAG_MODEL);
		LOG.log(Level.FINER, "camera model: {0}", camera);

		Directory exifSubDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);
		Date originalAbsorptionTime = exifSubDirectory
				.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		LOG.log(Level.FINER, "absorption time: {0}", originalAbsorptionTime);

		// create the ImageFile model
		ImageFile image = new ImageFile();
		image.setFile(f);
		String filename = f.getName();
		image.setOriginalFilename(filename);
		image.setFileExtension(filename.substring(filename.lastIndexOf(".") + 1, filename.length()));
		image.setOrientation(cameraOrientation);
		image.setOriginalAbsorptionTime(originalAbsorptionTime);
		image.setCameraModel(camera);

		// add the information to the list(s)
		images.add(image);
		cameraModels.add(camera);
	}

	public void clear() {
		images.clear();
		cameraModels.clear();
		fileVisitor.clear();
	}
}
