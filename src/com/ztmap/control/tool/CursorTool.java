/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

package com.ztmap.control.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.ztmap.control.MapControl;
import com.ztmap.control.event.MapMouseAdapter;
import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;

public abstract class CursorTool extends MapMouseAdapter {

	public static final int ANY_BUTTON = SWT.BUTTON_MASK;

	protected MapControl mapControl;
	private int triggerButtonMask;

	protected Color lineColor;
	protected int lineWidth;
	protected int lineStyle;

	public CursorTool(int triggerButtonMask) {
		this.triggerButtonMask = triggerButtonMask;
		lineWidth = 2;
		lineStyle = SWT.LINE_SOLID;
	}

	public CursorTool() {
		this(ANY_BUTTON);
	}

	public void setMapControl(MapControl mapControl) {
		if (mapControl == null) {
			throw new IllegalArgumentException("parameter is null.");
		}

		lineColor = mapControl.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		this.mapControl = mapControl;
	}

	public MapControl getMapPane() {
		return mapControl;
	}

	public Cursor getCursor() {
		return CursorManager.getInstance().getArrowCursor();
	}

	protected CoordinateReferenceSystem getCRS() {
		return mapControl.getMapContent().getCoordinateReferenceSystem();
	}

	protected boolean isTriggerMouseButton(MapMouseEvent event) {
		return triggerButtonMask == ANY_BUTTON || 0 != (triggerButtonMask & event.getStateMask())
				|| (event.getStateMask() == 0
						&& (((triggerButtonMask & SWT.BUTTON1) != 0 && event.getMouseButton() == 1)
								|| ((triggerButtonMask & SWT.BUTTON2) != 0 && event.getMouseButton() == 2)
								|| ((triggerButtonMask & SWT.BUTTON3) != 0 && event.getMouseButton() == 3)
								|| ((triggerButtonMask & SWT.BUTTON4) != 0 && event.getMouseButton() == 4)
								|| ((triggerButtonMask & SWT.BUTTON5) != 0 && event.getMouseButton() == 5)));
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}

}
