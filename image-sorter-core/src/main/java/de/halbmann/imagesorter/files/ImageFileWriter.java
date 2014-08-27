package de.halbmann.imagesorter.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import de.halbmann.imagesorter.ImageSorterSettings;
import de.halbmann.imagesorter.model.ImageFile;

/**
 * Class for writing the new ordered, renamed, or what else could be done with the images.
 * 
 * @author fabian
 * 
 */
public class ImageFileWriter extends Observable {

	private static final Logger log = Logger.getLogger(ImageFileWriter.class.getName());

	/**
	 * hold the original path of the files.
	 */
	private String originalPath;

	/**
	 * the subpath for saving the modified files. if this is null, the files will be stored in the
	 * "original" image directory.
	 */
	private String subpath;

	/**
	 * Default constructor.
	 */
	public ImageFileWriter() {
	}

	/**
	 * Consructor for setting the subpath, prefix, suffix and the offset.
	 * 
	 * @param originalPath
	 *            the original path of the images
	 * @param subpath
	 *            subpath for the new files
	 * @param prefix
	 *            the prefix that is used for the new filenames
	 * @param suffix
	 *            the suffix that is used for the new filenames
	 * @param offset
	 *            the offset for "indexing" the renamed files
	 */
	public ImageFileWriter(String originalPath, String subpath, String prefix, String suffix,
			int offset) {
		this.originalPath = originalPath;
		this.subpath = subpath;
	}

	/**
	 * Constructor for setting the attributes via the ImageSorterSettings object.
	 * 
	 * @param originalPath
	 *            the original path of the images
	 * @param settings
	 *            the settings for writing the images
	 */
	public ImageFileWriter(String originalPath, ImageSorterSettings settings) {
		this(originalPath, settings.getOutputDir(), settings.getPrefix(), settings.getSuffix(),
				settings.getOffset());
	}

	/**
	 * Write (rename) the image files of the given list in the right order on the filesystem. The
	 * original files will be modified!
	 * 
	 * @param images
	 *            the list of ImageFiles to process
	 * @param rotateImages
	 *            automatically rotate the images by its exif metadata orentation settings
	 * @throws IOException
	 */
	public void renameImageFiles(List<ImageFile> images, boolean rotateImages) throws IOException {
		runImageFileOperations(images, new Settings(rotateImages, false));
	}

	/**
	 * Write the image files of the given list in the right order to the filesystem.
	 * 
	 * @param images
	 *            the list of ImageFiles to process
	 * @param rotateImages
	 *            automatically rotate the images by its exif metadata orentation settings
	 * @throws IOException
	 */
	public void writeImageFiles(List<ImageFile> images, boolean rotateImages) throws IOException {
		runImageFileOperations(images, new Settings(rotateImages, true));
	}

	/**
	 * Write the image files of the given list in the right order to the filesystem. If copy flag is
	 * set, the original image files won't be modified, it will be copied to the subpath. Else the
	 * original files will be renamed!
	 * 
	 * @param images
	 *            the list of ImageFiles to process
	 * @param rotateImages
	 *            automatically rotate the images by its exif metadata orientation settings
	 * @param copy
	 *            flag whether to copy (true) or rename (false) the original files
	 * @throws IOException
	 */
	public void runImageFileOperations(List<ImageFile> images, Settings settings)
			throws IOException {
		if (images == null || images.isEmpty()) {
			return;
		}

		// create subdirectory
		if (settings.isCopy() && subpath != null) {
			Path p = FileSystems.getDefault().getPath(originalPath + subpath);
			if (!Files.exists(p)) {
				Files.createDirectory(p);
			}
		}

		for (int i = 0; i < images.size(); i++) {
			ImageFile imageFile = images.get(i);

			if (imageFile != null) {
				String filename = imageFile.getCalculatedFilename();
				filename = String.format("%s.%s", filename, imageFile.getFileExtension());
				log.log(Level.FINE, "Calculated filename: {0}", filename);

				if (settings.isRotateImages()) {
					BufferedImage orig = null;
					try {
						orig = ImageIO.read(imageFile.getFile());
					} catch (IOException e) {
						log.log(Level.WARNING, "Error while reading image file: {0}", imageFile
								.getFile().getName());
					}
					BufferedImage image = ImageUtils.rotate(orig, imageFile.getOrientation());
					// TODO: get the correct image format here!
					String format = imageFile.getFile().getName().endsWith(".png") ? "png" : "jpg";
					try {
						if (subpath != null) {
							filename = String.format("%s/%s", subpath, filename);
						}
						boolean success = ImageIO.write(image, format, new File(originalPath
								+ filename));
						log.log(Level.INFO, "Could write new image file: {0}", success);
					} catch (IOException e) {
						log.log(Level.WARNING, "Error while writing image file: {0}", filename);
					}
				} else {
					if (settings.isCopy()) {
						if (subpath != null) {
							filename = String.format("%s/%s", subpath, filename);
						}
						copyFile(imageFile.getFile(), new File(originalPath + filename));
					} else {
						renameFile(imageFile.getFile(), filename);
					}
				}
				notifyObservers((double) i / images.size());
				setChanged();
			}
		}
	}

	/**
	 * Fast copy the source file to the destination file.
	 * 
	 * @param sourceFile
	 *            the source file to copy
	 * @param destFile
	 *            the destination file to copy the source file to
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/**
	 * Rename the sourceFile to the given new filename.
	 * 
	 * @param sourceFile
	 *            the source file to rename
	 * @param newFilename
	 *            the new filename
	 * @throws IOException
	 */
	public static void renameFile(File sourceFile, String newFilename) throws IOException {
		Path source = sourceFile.toPath();
		Files.move(source, source.resolveSibling(newFilename));
	}

	public String getSubpath() {
		return subpath;
	}

	public void setSubpath(String subpath) {
		this.subpath = subpath;
	}

	/**
	 * Container object for the settings.
	 * 
	 * @author fabian
	 * 
	 */
	public static class Settings {

		private boolean rotateImages = false;
		private boolean copy = false;

		public Settings() {
		}

		public Settings(boolean copy) {
			this.copy = copy;
		}

		public Settings(boolean rotateImages, boolean copy) {
			this.rotateImages = rotateImages;
			this.copy = copy;
		}

		public boolean isRotateImages() {
			return rotateImages;
		}

		public void setRotateImages(boolean rotateImages) {
			this.rotateImages = rotateImages;
		}

		public boolean isCopy() {
			return copy;
		}

		public void setCopy(boolean copy) {
			this.copy = copy;
		}

	}
}
