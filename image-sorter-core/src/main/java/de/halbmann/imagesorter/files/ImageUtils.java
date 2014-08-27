package de.halbmann.imagesorter.files;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Utility-Class for manipulating images (Rotate, Resize, ...).
 * 
 * @author fabian
 * 
 */
public class ImageUtils {

	private static final int NONE = 0;
	private static final int HORIZONTAL = 1;
	private static final int VERTICAL = 2;
	private static final int[][] OPERATIONS = new int[][] { new int[] { 0, NONE }, //
			new int[] { 0, HORIZONTAL }, //
			new int[] { 180, NONE }, //
			new int[] { 0, VERTICAL }, //
			new int[] { 90, HORIZONTAL }, //
			new int[] { 90, NONE }, //
			new int[] { -90, HORIZONTAL }, //
			new int[] { -90, NONE } //
	};

	public static BufferedImage rotateByExif(BufferedImage img, int orientation) {
		// int angle = 0;
		// int w = img.getWidth();
		// int h = img.getHeight();
		// BufferedImage dimg = new BufferedImage(w, h, img.getType());
		// Graphics2D g = dimg.createGraphics();
		// g.rotate(Math.toRadians(angle), w / 2, h / 2);
		// g.drawImage(img, null, 0, 0);
		// return dimg;
		AffineTransform t = getExifTransformation(orientation, img.getWidth(), img.getHeight());
		return transformImage(img, t);
	}

	public static BufferedImage rotate(BufferedImage srcImage, int degrees) {
		BufferedImage rotatedImage = new BufferedImage(srcImage.getHeight(), srcImage.getWidth(),
				srcImage.getType());
		AffineTransform affineTransform = AffineTransform.getRotateInstance(
				Math.toRadians(degrees), rotatedImage.getWidth() / 2d, srcImage.getHeight() / 2d);
		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		g.setTransform(affineTransform);
		g.drawImage(srcImage, 0, 0, null);
		return rotatedImage;
	}

	// public static BufferedImage rotateByExif(BufferedImage img,
	// int exifOrientation) {
	// exifOrientation -= 1;
	// int degrees = OPERATIONS[exifOrientation][0];
	// if (degrees != 0)
	// img = img.rotateImage(degrees);
	//
	// switch (OPERATIONS[exifOrientation][1]) {
	// case HORIZONTAL:
	// image = image.flopImage();
	// break;
	// case VERTICAL:
	// image = image.flipImage();
	// }
	// }

	public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) {
		//
		// double rotationRequired = Math.toRadian(45);
		// double locationX = image.getWidth() / 2;
		// double locationY = image.getHeight() / 2;
		// AffineTransform tx =
		// AffineTransform.getRotateInstance(rotationRequired, locationX,
		// locationY);
		// AffineTransformOp op = new AffineTransformOp(tx,
		// AffineTransformOp.TYPE_BILINEAR);
		//
		// // Drawing the rotated image at the required drawing locations
		// g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY,
		// null);

		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
		//
		// BufferedImage destinationImage = op.createCompatibleDestImage(
		// image,
		// (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image
		// .getColorModel() : null);
		// Graphics2D g = destinationImage.createGraphics();
		// g.setBackground(Color.WHITE);
		// g.clearRect(0, 0, destinationImage.getWidth(),
		// destinationImage.getHeight());
		// destinationImage = op.filter(image, destinationImage);
		// return destinationImage;

		// Graphics2D g2d = (Graphics2D) image.getGraphics();
		return op.filter(image, null);
		// g2d.drawImage(op.filter(image, null), 0, 0, null);
		// g2d.dispose();
		// return image;
	}

	public static AffineTransform getExifTransformation(int exifOrientation, int width, int height) {

		AffineTransform t = new AffineTransform();

		switch (exifOrientation) {
		case 1:
			break;
		case 2: // Flip X
			t.scale(-1.0, 1.0);
			t.translate(-width, 0);
			break;
		case 3: // PI rotation
			t.translate(width, height);
			t.rotate(Math.PI);
			break;
		case 4: // Flip Y
			t.scale(1.0, -1.0);
			t.translate(0, -height);
			break;
		case 5: // - PI/2 and Flip X
			t.rotate(-Math.PI / 2d);
			t.scale(-1.0, 1.0);
			break;
		case 6: // -PI/2 and -width
			t.translate(height, 0);
			t.rotate(Math.PI / 2d);
			break;
		case 7: // PI/2 and Flip
			t.scale(-1.0, 1.0);
			t.translate(-height, 0);
			t.translate(0, width);
			t.rotate(3 * Math.PI / 2d);
			break;
		case 8: // PI / 2
			t.translate(0, width);
			t.rotate(3 * Math.PI / 2d);
			break;
		}

		return t;
	}
}
