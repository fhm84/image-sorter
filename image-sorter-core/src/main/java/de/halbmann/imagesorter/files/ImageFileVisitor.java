package de.halbmann.imagesorter.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * ImageFileVisitor. Implementation of a FileVisitor to get the list of images to order (or rename,
 * or what else).
 * 
 * @author fabian
 * 
 */
public class ImageFileVisitor extends SimpleFileVisitor<Path> {

	/**
	 * list of files that were found
	 */
	private final List<File> fileList = new ArrayList<>();

	/**
	 * list of all file-extensions, that files will be listed
	 */
	private List<String> extensions = new ArrayList<>();

	/**
	 * search only the directory or additionally sub-directories
	 */
	private boolean recursive = false;

	/**
	 * Default constructor. Allows only JPEGs in the current directory (no sub-directories).
	 */
	public ImageFileVisitor() {
		extensions.add(".jpg");
		extensions.add(".jpeg");
	}

	/**
	 * Constructor for setting the recursive parameter. The file-extensions are the defaults.
	 * 
	 * @param recursive
	 *            search only the directory or additionally the sub-directories
	 */
	public ImageFileVisitor(boolean recursive) {
		this();
		this.recursive = recursive;
	}

	/**
	 * Constructor for setting the file-extensions allowed.
	 * 
	 * @param extensions
	 *            file-extensions
	 */
	public ImageFileVisitor(String... extensions) {
		this.extensions = Arrays.asList(extensions);
	}

	/**
	 * Constructor for setting the recursive parameter and the file-extensions.
	 * 
	 * @param recursive
	 *            search only the directory or additionally the sub-directories
	 * @param extensions
	 *            file-extensions
	 */
	public ImageFileVisitor(boolean recursive, String... extensions) {
		this.recursive = recursive;
		this.extensions = Arrays.asList(extensions);
	}

	/**
	 * Walk through the given path. If the recursive flag is set to the ImageFileVisitor, also the
	 * sub-directories will be searched for image files.
	 * 
	 * @param pathFile
	 *            the path to walk through
	 * @return the list of image files found
	 * @throws IOException
	 */
	public List<File> walk(Path pathFile) throws IOException {
		if (recursive) {
			Files.walkFileTree(pathFile, this);
		} else {
			Files.walkFileTree(pathFile, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, this);
		}
		return fileList;
	}

	/**
	 * Walk through the given path and search for all image files.
	 * 
	 * @param pathFile
	 *            the path to walk through
	 * @param recursive
	 *            search only the directory or additionally the sub-directories
	 * @return the list of image files found
	 * @throws IOException
	 */
	public List<File> walk(Path pathFile, boolean recursive) throws IOException {
		this.recursive = recursive;
		return walk(pathFile);
	}

	@Override
	public FileVisitResult visitFile(Path pathFile, BasicFileAttributes attrs) throws IOException {
		if (fileAccessed(pathFile.toString())) {
			fileList.add(pathFile.toFile());
			return FileVisitResult.CONTINUE;
		} else {
			return recursive ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.TERMINATE;
		}
	}

	/**
	 * Checks if the file-extension fits with one of the extensions allowed.
	 * 
	 * @param filename
	 *            complete filename (incl. path)
	 * @return true, if the file-extension fits with one of the extensions in the list
	 */
	private boolean fileAccessed(String filename) {
		for (String ext : extensions) {
			if (filename.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	public List<File> getFiles() {
		return fileList;
	}

	public void clear() {
		fileList.clear();
	}
}
