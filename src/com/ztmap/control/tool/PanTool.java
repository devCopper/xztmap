package com.ztmap.control.tool;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.geotools.geometry.jts.ReferencedEnvelope;

import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;
import com.ztmap.control.utils.Utils;

public class PanTool extends CursorTool {

	public static final String TOOL_NAME = "Pan";
	public static final String TOOL_TIP = "Pan Map";
	private Cursor cursor;
	private Point startPos;
	private Point endPos;
	private boolean panning;

	public PanTool(int triggerButtonMask) {
		super(triggerButtonMask);
		cursor = CursorManager.getInstance().getPanCursor();
		panning = false;
	}

	public PanTool() {
		this(CursorTool.ANY_BUTTON);
	}

	@Override
	public void onMousePressed(MapMouseEvent ev) {

		if (!isTriggerMouseButton(ev)) {
			return;
		}

		startPos = ev.getPoint();
		panning = true;
	}

	@Override
	public void onMouseDragged(MapMouseEvent ev) {
		if (panning) {
			endPos = ev.getPoint();
			if (!endPos.equals(startPos)) {
				Rectangle clientRect = mapControl.getClientRect();
				if (mapControl != null && !mapControl.isDisposed()) {
					Image swtImage = new Image(mapControl.getDisplay(),
							Utils.awtToSwt(mapControl.getBaseImage(), clientRect.width + 1, clientRect.height + 1));

					GC gc = new GC(mapControl);
					Image tmpImage = new Image(mapControl.getDisplay(), clientRect.width, clientRect.height);
					GC tmpGc = new GC(tmpImage);
					tmpGc.fillRectangle(0, 0, clientRect.width, clientRect.height);
					tmpGc.drawImage(swtImage, endPos.x - startPos.x, endPos.y - startPos.y);
					gc.drawImage(tmpImage, 0, 0);
					tmpImage.dispose();
					swtImage.dispose();
				}
			}
		}
	}

	@Override
	public void onMouseReleased(MapMouseEvent ev) {
		if (panning && endPos != null && !endPos.equals(startPos)) {
			AffineTransform screenToWorldTransform = getMapPane().getScreenToWorldTransform();
			screenToWorldTransform.translate(startPos.x - endPos.x, startPos.y - endPos.y);
			Point2D p0 = new Point2D.Double(0, 0);
			Point2D p1 = new Point2D.Double(mapControl.getClientRect().width, mapControl.getClientRect().height);
			screenToWorldTransform.transform(p0, p0);
			screenToWorldTransform.transform(p1, p1);

			ReferencedEnvelope aoi = new ReferencedEnvelope(Math.min(p0.getX(), p1.getX()),
					Math.max(p0.getX(), p1.getX()), Math.min(p0.getY(), p1.getY()), Math.max(p0.getY(), p1.getY()),
					getCRS());

			mapControl.setDisplayArea(aoi);
			getMapPane().redrawMap(true);
			endPos = null;
		}
		panning = false;
	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}

}