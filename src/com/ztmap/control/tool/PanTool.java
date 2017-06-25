package com.ztmap.control.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;

import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;

public class PanTool extends CursorTool {

	/** Tool name */
	public static final String TOOL_NAME = "Pan";
	/** Tool tip text */
	public static final String TOOL_TIP = "Pan Map";

	private Cursor cursor;

	private Point panePos;
	boolean panning;

	/**
	 * Constructs a new pan tool. To activate the tool only on certain mouse
	 * events provide a single mask, e.g. {@link SWT#BUTTON1}, or a combination
	 * of multiple SWT-masks.
	 *
	 * @param triggerButtonMask
	 *            Mouse button which triggers the tool's activation or
	 *            {@value #ANY_BUTTON} if the tool is to be triggered by any
	 *            button
	 */
	public PanTool(int triggerButtonMask) {
		super(triggerButtonMask);

		cursor = CursorManager.getInstance().getPanCursor();

		panning = false;
	}

	/**
	 * Constructs a new pan tool which is triggered by any mouse button.
	 */
	public PanTool() {
		this(CursorTool.ANY_BUTTON);
	}

	/**
	 * Respond to a mouse button press event from the map mapPane. This may
	 * signal the start of a mouse drag. Records the event's window position.
	 * 
	 * @param ev
	 *            the mouse event
	 */
	@Override
	public void onMousePressed(MapMouseEvent ev) {

		if (!isTriggerMouseButton(ev)) {
			return;
		}

		panePos = ev.getPoint();
		panning = true;
	}

	/**
	 * Respond to a mouse dragged event. Calls
	 * {@link org.geotools.swing.JMapPane#moveImage()}
	 * 
	 * @param ev
	 *            the mouse event
	 */
	@Override
	public void onMouseDragged(MapMouseEvent ev) {
		if (panning) {
			Point pos = ev.getPoint();
			if (!pos.equals(panePos)) {
				getMapPane().moveImage(pos.x - panePos.x, pos.y - panePos.y);
				panePos = pos;
			}
		}
	}

	/**
	 * If this button release is the end of a mouse dragged event, requests the
	 * map mapPane to repaint the display
	 * 
	 * @param ev
	 *            the mouse event
	 */
	@Override
	public void onMouseReleased(MapMouseEvent ev) {
		if (panning) {
			panning = false;
			getMapPane().redraw();
		}
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
		return true;
	}
}