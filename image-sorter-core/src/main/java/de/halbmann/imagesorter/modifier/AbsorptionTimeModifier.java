package de.halbmann.imagesorter.modifier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import de.halbmann.imagesorter.ImageSorterSettings.CameraSettings;
import de.halbmann.imagesorter.model.ImageFile;

/**
 * ImageModifier implementation for modifying the absorption time of the ImageFiles by camera model.
 * In the settings the modifications can be set by camera model.
 * 
 * @author fabian
 * 
 */
public class AbsorptionTimeModifier implements ImageModifier {

	String cameraModel;
	int years = 0;
	int months = 0;
	int days = 0;
	int hours = 0;
	int minutes = 0;
	int seconds = 0;

	/**
	 * Constructor for setting the camera model and the time to modify (as Properties).
	 * 
	 * @param cameraModel
	 *            camera model, all images taken with this camera model will be modified
	 * @param properties
	 *            the properties containing the time settings
	 */
	public AbsorptionTimeModifier(String cameraModel, Properties properties) {
		this.cameraModel = cameraModel;
		if (properties != null) {
			this.years = Integer.valueOf(properties.getProperty(cameraModel + ".years"));
			this.months = Integer.valueOf(properties.getProperty(cameraModel + ".months"));
			this.days = Integer.valueOf(properties.getProperty(cameraModel + ".days"));
			this.hours = Integer.valueOf(properties.getProperty(cameraModel + ".hours"));
			this.minutes = Integer.valueOf(properties.getProperty(cameraModel + ".minutes"));
			this.seconds = Integer.valueOf(properties.getProperty(cameraModel + ".seconds"));
		}
	}

	/**
	 * Constructor for setting the camera model and the time to modify.
	 * 
	 * @param cameraModel
	 *            camera model, all images taken with this camera model will be modified
	 * @param years
	 *            years to add to the absorption time (also negative values are allowed)
	 * @param months
	 *            months to add to the absorption time (also negative values are allowed)
	 * @param days
	 *            days to add to the absorption time (also negative values are allowed)
	 * @param hours
	 *            hours to add to the absorption time (also negative values are allowed)
	 * @param minutes
	 *            minutes to add to the absorption time (also negative values are allowed)
	 * @param seconds
	 *            seconds to add to the absorption time (also negative values are allowed)
	 */
	public AbsorptionTimeModifier(String cameraModel, int years, int months, int days, int hours,
			int minutes, int seconds) {
		this.cameraModel = cameraModel;
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	/**
	 * Constructor for setting the camera model and the time to modify via the CameraSettings.
	 * 
	 * @param settings
	 *            the camera settings including camera model, years, months, days, hours, minutes
	 *            and seconds
	 */
	public AbsorptionTimeModifier(CameraSettings settings) {
		this.cameraModel = settings.getCameraModel();
		this.years = settings.getYears();
		this.months = settings.getMonths();
		this.days = settings.getDays();
		this.hours = settings.getHours();
		this.minutes = settings.getMinutes();
		this.seconds = settings.getSeconds();
	}

	/**
	 * Modify the ImageFile-Attributes. Add the given values to the absorptionTime for the given
	 * cameraModel.
	 * 
	 * @param images
	 *            list of the ImageFiles to modify
	 */
	@Override
	public void modify(List<ImageFile> images) {
		if (cameraModel == null) {
			throw new IllegalArgumentException("CameraModel cannot be null!");
		}
		for (ImageFile image : images) {
			if (cameraModel.equals(image.getCameraModel())) {
				Date absorptionTime = image.getOriginalAbsorptionTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(absorptionTime);
				if (years != 0) {
					cal.add(Calendar.YEAR, years);
				}
				if (months != 0) {
					cal.add(Calendar.MONTH, months);
				}
				if (days != 0) {
					cal.add(Calendar.DAY_OF_MONTH, days);
				}
				if (hours != 0) {
					cal.add(Calendar.HOUR_OF_DAY, hours);
				}
				if (minutes != 0) {
					cal.add(Calendar.MINUTE, minutes);
				}
				if (seconds != 0) {
					cal.add(Calendar.SECOND, seconds);
				}
				image.setModifiedAbsorptionTime(cal.getTime());
			}
		}
	}

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
