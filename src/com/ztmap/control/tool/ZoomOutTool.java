package com.ztmap.control.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;

import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;

public class ZoomOutTool extends ZoomTool {

	/** Tool name */
	public static final String TOOL_NAME = "Zoom Out";
	/** Tool tip text */
	public static final String TOOL_TIP = "Zoom Out Map";

	private Cursor cursor;

	/**
	 * Constructs a new zoom out tool. To activate the tool only on certain
	 * mouse events provide a single mask, e.g. {@link SWT#BUTTON1}, or a
	 * combination of multiple SWT-masks.
	 *
	 * @param triggerButtonMask
	 *            Mouse button which triggers the tool's activation or
	 *            {@value #ANY_BUTTON} if the tool is to be triggered by any
	 *            button
	 */
	public ZoomOutTool(int triggerButtonMask) {
		super(triggerButtonMask);
		cursor = CursorManager.getInstance().getZoomoutCursor();
	}

	/**
	 * Constructs a new zoom out tool which is triggered by any mouse button.
	 */
	public ZoomOutTool() {
		this(CursorTool.ANY_BUTTON);
	}

	/**
	 * Zoom out by the currently set increment, with the map centred at the
	 * location (in world coords) of the mouse click
	 *
	 * @param ev
	 *            the mouse event
	 */
	@Override
	public void onMouseClicked(MapMouseEvent ev) {

		if (!isTriggerMouseButton(ev)) {
			return;
		}

		Rectangle paneArea = getMapPane().getBounds();
		DirectPosition2D mapPos = ev.getMapPosition();

		double scale = getMapPane().getWorldToScreenTransform().getScaleX();
		double newScale = scale / zoom;

		DirectPosition2D corner = new DirectPosition2D(mapPos.getX() - 0.5d * paneArea.width / newScale,
				mapPos.getY() + 0.5d * paneArea.height / newScale);

		Envelope2D newMapArea = new Envelope2D();
		newMapArea.setFrameFromCenter(mapPos, corner);
		getMapPane().setDisplayArea(newMapArea);
	}

	/**
	 * Get the mouse cursor for this tool
	 */
	@Override
	public Cursor getCursor() {
		return cursor;
	}

	public boolean canDraw() {
		return false;
	}

	public boolean canMove() {
		return false;
	}
}
