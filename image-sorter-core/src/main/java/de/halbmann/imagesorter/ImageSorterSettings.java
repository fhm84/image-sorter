package de.halbmann.imagesorter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.halbmann.imagesorter.files.ImageFileWriter.Settings;

/**
 * Container object for the settings.
 * 
 * @author fabian
 *
 */
public class ImageSorterSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	private String outputDir;
	private String prefix;
	private String suffix;
	private int offset;
	/**
	 * This is used for the number-pattern; for the number of leading zeros.
	 */
	private int numberOfDigits = -1;

	private boolean recursive = true;

	private Settings fileWriterSettings = new Settings();

	private List<CameraSettings> cameraSettings = new ArrayList<>();

	public ImageSorterSettings() {
	}

	public ImageSorterSettings(String outputDir, String prefix, String suffix, int offset) {
		this.outputDir = outputDir;
		this.prefix = prefix;
		this.suffix = suffix;
		this.offset = offset;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	public void setNumberOfDigits(int numberOfDigits) {
		this.numberOfDigits = numberOfDigits;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public Settings getFileWriterSettings() {
		return fileWriterSettings;
	}

	public void setFileWriterSettings(Settings fileWriterSettings) {
		this.fileWriterSettings = fileWriterSettings;
	}

	public List<CameraSettings> getCameraSettings() {
		return cameraSettings;
	}

	public void setCameraSettings(List<CameraSettings> cameraSettings) {
		this.cameraSettings = cameraSettings;
	}

	public static class CameraSettings implements Serializable {

		private static final long serialVersionUID = 1L;

		private String cameraModel;
		private int years = 0;
		private int months = 0;
		private int days = 0;
		private int hours = 0;
		private int minutes = 0;
		private int seconds = 0;

		public String getCameraModel() {
			return cameraModel;
		}

		public void setCameraModel(String cameraModel) {
			this.cameraModel = cameraModel;
		}

		public int getYears() {
			return years;
		}

		public void setYears(int years) {
			this.years = years;
		}

		public int getMonths() {
			return months;
		}

		public void setMonths(int months) {
			this.months = months;
		}

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public int getHours() {
			return hours;
		}

		public void setHours(int hours) {
			this.hours = hours;
		}

		public int getMinutes() {
			return minutes;
		}

		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}

		public int getSeconds() {
			return seconds;
		}

		public void setSeconds(int seconds) {
			this.seconds = seconds;
		}

	}
}
