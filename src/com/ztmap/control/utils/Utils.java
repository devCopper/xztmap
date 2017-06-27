package com.ztmap.control.utils;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;

public class Utils {
	private static final PaletteData PALETTE_DATA = new PaletteData(0xFF0000, 0xFF00, 0xFF);

	private static final int TRANSPARENT_COLOR = 0x123456;

	public static Rectangle toSwtRectangle(java.awt.Rectangle rect2d) {
		return new Rectangle((int) Math.round(rect2d.getMinX()), (int) Math.round(rect2d.getMinY()),
				(int) Math.round(rect2d.getWidth()), (int) Math.round(rect2d.getHeight()));
	}

	public static java.awt.Rectangle toAwtRectangle(Rectangle rect) {
		java.awt.Rectangle rect2d = new java.awt.Rectangle();
		rect2d.setRect(rect.x, rect.y, rect.width, rect.height);
		return rect2d;
	}

	public static ImageData awtToSwt(BufferedImage bufferedImage, int width, int height) {
		final int[] awtPixels = new int[width * height];
		ImageData swtImageData = new ImageData(width, height, 24, PALETTE_DATA);
		swtImageData.transparentPixel = TRANSPARENT_COLOR;
		int step = swtImageData.depth / 8;
		final byte[] data = swtImageData.data;
		bufferedImage.getRGB(0, 0, width, height, awtPixels, 0, width);
		for (int i = 0; i < height; i++) {
			int idx = (0 + i) * swtImageData.bytesPerLine + 0 * step;
			for (int j = 0; j < width; j++) {
				int rgb = awtPixels[j + i * width];
				for (int k = swtImageData.depth - 8; k >= 0; k -= 8) {
					data[idx++] = (byte) ((rgb >> k) & 0xFF);
				}
			}
		}

		return swtImageData;
	}

}
