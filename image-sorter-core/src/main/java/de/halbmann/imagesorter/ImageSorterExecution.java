package de.halbmann.imagesorter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.halbmann.imagesorter.ImageSorterSettings.CameraSettings;
import de.halbmann.imagesorter.files.DefaultFilenameCalculator;
import de.halbmann.imagesorter.files.FilenameCalculator;
import de.halbmann.imagesorter.files.ImageFileWriter;
import de.halbmann.imagesorter.files.ImageMetadataReader;
import de.halbmann.imagesorter.model.ImageFile;
import de.halbmann.imagesorter.modifier.AbsorptionTimeModifier;
import de.halbmann.imagesorter.modifier.ImageModifier;
import de.halbmann.imagesorter.sorting.GenericComparator;
import de.halbmann.imagesorter.sorting.ImageAbsorptionTimeComparator;

/**
 * This is the main entry point for executing the image sorter. You only have to set the base
 * directory and the settings. The run-method will combine the various readers, modifiers, sorters
 * and writers.
 * 
 * @author fabian
 *
 */
public class ImageSorterExecution {

	private static final Logger LOG = Logger.getLogger(ImageSorterExecution.class.getName());

	private String baseDir;
	private ImageSorterSettings settings;

	private List<ImageFile> images;

	public ImageSorterExecution(String baseDir, ImageSorterSettings settings) {
		this.baseDir = baseDir;
		this.settings = settings;
	}

	/**
	 * Run the image sorter.<br/>
	 * This will execute the following steps:
	 * <ul>
	 * <li>read the image files from the base directory</li>
	 * <li>get the camera settings and modify the images</li>
	 * <li>order (sort) the images</li>
	 * <li>calculate the (new) filenames</li>
	 * <li>write the image files</li>
	 * </ul>
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		run(false);
	}

	/**
	 * Run the image sorter.<br/>
	 * This will execute the following steps:
	 * <ul>
	 * <li>read the image files from the base directory</li>
	 * <li>get the camera settings and modify the images</li>
	 * <li>order (sort) the images</li>
	 * <li>calculate the (new) filenames</li>
	 * <li>write the image files</li>
	 * </ul>
	 * 
	 * @param simulate
	 *            only simulate reading, modifying, ordering and calculating image files. This will
	 *            skip the last step: writing the files to the filesystem.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void run(boolean simulate) throws IOException {
		// read the images from the given base directory
		readImageFiles(baseDir, settings.isRecursive());

		// get the camera settings and modify the images
		if (settings.getCameraSettings() != null && !settings.getCameraSettings().isEmpty()) {
			List<ImageModifier> modifiers = new ArrayList<>();
			for (CameraSettings cs : settings.getCameraSettings()) {
				modifiers.add(new AbsorptionTimeModifier(cs));
			}
			modifyImages(modifiers.toArray(new ImageModifier[] {}));
		}

		// order the images
		orderImages(new ImageAbsorptionTimeComparator());

		// calculate the filenames
		calculateFilenames(new DefaultFilenameCalculator(settings));

		// write the images
		if (!simulate) {
			writeImageFiles(baseDir, settings);
		}
	}

	/**
	 * Read the image files including the meta (exif) informations from the given base directory.
	 * 
	 * @param baseDir
	 *            the base directory reading the image files from
	 * @param recursive
	 *            recursive read the directory
	 * @throws IOException
	 */
	protected void readImageFiles(String baseDir, boolean recursive) throws IOException {
		// read the images from the given base directory
		ImageMetadataReader imr = new ImageMetadataReader(recursive);
		images = imr.readImageFiles(baseDir);
	}

	/**
	 * Modify the ImageFiles with the given ImageModifier implementations.
	 * 
	 * @param modifiers
	 *            ImageModifier implementations
	 */
	protected void modifyImages(ImageModifier... modifiers) {
		if (modifiers == null || modifiers.length == 0) {
			return;
		}
		LOG.log(Level.INFO, "modify images");
		LOG.log(Level.FINE, "image modifiers: {0}", modifiers);
		long startTime = System.currentTimeMillis();
		for (ImageModifier m : modifiers) {
			m.modify(images);
		}
		long endTime = System.currentTimeMillis();
		LOG.log(Level.FINE, "modifying took {0} ms", (endTime - startTime));
	}

	/**
	 * Order the ImageFiles with the given comparators.
	 * 
	 * @param comparators
	 *            Comparators for the ImageFiles
	 */
	@SuppressWarnings("unchecked")
	protected void orderImages(Comparator<ImageFile>... comparators) {
		if (comparators == null || comparators.length == 0) {
			return;
		}
		LOG.log(Level.INFO, "order images");
		LOG.log(Level.FINE, "comparators: {0}", comparators);
		long startTime = System.currentTimeMillis();
		GenericComparator<ImageFile> c = new GenericComparator<>(comparators);
		Collections.sort(images, c);
		long endTime = System.currentTimeMillis();
		LOG.log(Level.FINE, "ordering took {0} ms", (endTime - startTime));
	}

	/**
	 * Calculate the filenames for the image files using the given FilenameCalculator.
	 * 
	 * @param calculator
	 *            FilenameCalculator
	 */
	protected void calculateFilenames(FilenameCalculator calculator) {
		if (calculator == null) {
			return;
		}
		LOG.log(Level.INFO, "calculate filenames");
		LOG.log(Level.FINE, "filename calculator: {0}", calculator);
		long startTime = System.currentTimeMillis();
		calculator.calculate(images);
		long endTime = System.currentTimeMillis();
		LOG.log(Level.FINE, "calculation took {0} ms", (endTime - startTime));
	}

	/**
	 * Write the image files to the given output directory
	 * 
	 * @param outputDir
	 *            the directory to write the images to
	 * @param settings
	 *            the ImageSorterSettings
	 * @throws IOException
	 */
	protected void writeImageFiles(String outputDir, ImageSorterSettings settings)
			throws IOException {
		ImageFileWriter ifw = new ImageFileWriter(outputDir, settings);
		ifw.runImageFileOperations(images, settings.getFileWriterSettings());
	}

	// getters and setters

	public String getBaseDir() {
		return baseDir;
	}

	public ImageSorterSettings getSettings() {
		return settings;
	}

	public List<ImageFile> getImages() {
		return images;
	}

}
