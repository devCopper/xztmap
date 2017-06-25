/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package com.ztmap.control.utils;

import org.eclipse.swt.graphics.Rectangle;

/**
 * Utilities class.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class Utils {

	/**
	 * Transform an awt {@link java.awt.Rectangle} instance into a swt one.
	 * <p>
	 * The coordinates are rounded to integer for the swt object.
	 * 
	 * @param rect2d
	 *            The awt rectangle to map.
	 * @return an swt <code>Rectangle</code> object.
	 */
	public static Rectangle toSwtRectangle(java.awt.Rectangle rect2d) {
		return new Rectangle((int) Math.round(rect2d.getMinX()), (int) Math.round(rect2d.getMinY()),
				(int) Math.round(rect2d.getWidth()), (int) Math.round(rect2d.getHeight()));
	}

	/**
	 * Transform a swt Rectangle instance into an awt one.
	 * 
	 * @param rect
	 *            the swt <code>Rectangle</code>
	 * @return a {@link java.awt.Rectangle} instance with the appropriate
	 *         location and size.
	 */
	public static java.awt.Rectangle toAwtRectangle(Rectangle rect) {
		java.awt.Rectangle rect2d = new java.awt.Rectangle();
		rect2d.setRect(rect.x, rect.y, rect.width, rect.height);
		return rect2d;
	}

}
