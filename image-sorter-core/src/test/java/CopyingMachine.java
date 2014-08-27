import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

public class CopyingMachine {

	public static void copyDirectory(String sourceDirectoryPath, String targetDirectoryPath) {
		copyDirectory(new File(sourceDirectoryPath), new File(targetDirectoryPath));
	}

	public static void copyDirectory(File sourceDir, File targetDir) {
		for (File sourceFile : sourceDir.listFiles()) {
			File targetFile = new File(targetDir.getAbsolutePath() + File.separator
					+ sourceFile.getName());

			if (sourceFile.isDirectory()) {
				if (!targetFile.exists()) {
					targetFile.mkdir();
				}
				copyDirectory(sourceFile, targetFile);
			} else {
				copyFile(sourceFile, targetFile);
			}
		}
	}

	public static void copyFile(File inputFile, File outputFile) {
		System.out.println("copy " + inputFile.getName() + " to " + outputFile.getAbsolutePath());

		try (FileInputStream fis = new FileInputStream(inputFile);
				FileOutputStream fos = new FileOutputStream(outputFile)) {
			byte[] b = new byte[1024];
			int noOfBytes;
			while ((noOfBytes = fis.read(b)) != -1) {
				fos.write(b, 0, noOfBytes);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void copyDirectory_newFileCopy(String sourceDirectoryPath,
			String targetDirectoryPath) {
		try {
			copyDirectory_newFileCopy(new File(sourceDirectoryPath), new File(targetDirectoryPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyDirectory_newFileCopy(File sourceDir, File targetDir) throws IOException {
		for (File sourceFile : sourceDir.listFiles()) {
			File targetFile = new File(targetDir.getAbsolutePath() + File.separator
					+ sourceFile.getName());

			if (sourceFile.isDirectory()) {
				if (!targetFile.exists()) {
					targetFile.mkdir();
				}
				copyDirectory_newFileCopy(sourceFile, targetFile);
			} else {
				copyFile_new(sourceFile, targetFile);
			}
		}
	}

	/**
	 * Fast copy the source file to the destination file.
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile_new(File sourceFile, File destFile) throws IOException {
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

	public static void copyDirectory_new(String sourceDirectoryPath, String targetDirectoryPath) {
		try {
			copyDirectory_new(new File(sourceDirectoryPath), new File(targetDirectoryPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyDirectory_new(File sourceDir, File targetDir) throws IOException {
		for (File sourceFile : sourceDir.listFiles()) {
			File targetFile = new File(targetDir.getAbsolutePath() + File.separator
					+ sourceFile.getName());

			if (sourceFile.isDirectory()) {
				if (!targetFile.exists()) {
					targetFile.mkdir();
				}
				copyDirectory_newFileCopy(sourceFile, targetFile);
			} else {
				Path sourcePath = FileSystems.getDefault().getPath(sourceFile.getAbsolutePath());
				Path destPath = FileSystems.getDefault().getPath(targetFile.getAbsolutePath());
				Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES);
			}
		}
	}

	@Test
	public void test() {
		String source = "src/test/resources";
		String dest1 = "test1";
		String dest2 = "test2";
		String dest3 = "test3";

		long start = System.currentTimeMillis();
		copyDirectory(source, dest1);
		long end = System.currentTimeMillis();

		System.out.println(String.format("Test1 took %d ms", end - start));

		start = System.currentTimeMillis();
		copyDirectory_newFileCopy(source, dest2);
		end = System.currentTimeMillis();

		System.out.println(String.format("Test2 took %d ms", end - start));

		start = System.currentTimeMillis();
		copyDirectory_new(source, dest3);
		end = System.currentTimeMillis();

		System.out.println(String.format("Test3 took %d ms", end - start));
	}
}