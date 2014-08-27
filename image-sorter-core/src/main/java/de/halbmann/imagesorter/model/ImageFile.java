package de.halbmann.imagesorter.model;

import java.io.File;
import java.util.Date;

/**
 * The model class for ordering the images.
 * 
 * @author fabian
 * 
 */
public class ImageFile {

	/**
	 * link to the file
	 */
	private File file;

	/**
	 * the original filename
	 */
	private String originalFilename;

	/**
	 * the fileExtension
	 */
	private String fileExtension;

	/**
	 * tha calculated filename
	 */
	private String calculatedFilename;

	/**
	 * orientation of the image (to automatically rotate)
	 */
	private int orientation;

	/**
	 * absorption time like its set in the metadata
	 */
	private Date originalAbsorptionTime;

	/**
	 * modified absorption time (for manually corrected camera-time-settings)
	 */
	private Date modifiedAbsorptionTime;

	/**
	 * the camera-model (for manually modify the absorption time by camera model)
	 */
	private String cameraModel;

	// getter and setter

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getCalculatedFilename() {
		if (calculatedFilename != null && !calculatedFilename.isEmpty()) {
			return calculatedFilename;
		} else {
			return originalFilename;
		}
	}

	public void setCalculatedFilename(String calculatedFilename) {
		this.calculatedFilename = calculatedFilename;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public Date getOriginalAbsorptionTime() {
		return originalAbsorptionTime;
	}

	public void setOriginalAbsorptionTime(Date originalAbsorptionTime) {
		this.originalAbsorptionTime = originalAbsorptionTime;
	}

	public Date getModifiedAbsorptionTime() {
		return modifiedAbsorptionTime;
	}

	public void setModifiedAbsorptionTime(Date modifiedAbsorptionTime) {
		this.modifiedAbsorptionTime = modifiedAbsorptionTime;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	public void setCameraModel(String cameraModel) {
		this.cameraModel = cameraModel;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.originalFilename);
		sb.append(" - orig: ");
		sb.append(this.originalAbsorptionTime);
		sb.append(" - orientation: ");
		sb.append(this.orientation);
		return sb.toString();
	}
}
