package com.ztmap.control.tool;

import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;

import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;
import com.ztmap.control.utils.Utils;

public class ZoomInTool extends ZoomTool {

	public static final String TOOL_NAME = "Zoom In";
	public static final String TOOL_TIP = "Zoom In Map";

	private Cursor cursor;

	private Point2D startMapPos;
	private Point startPos;
	private boolean dragged;

	public ZoomInTool(int triggerButtonMask) {
		super(triggerButtonMask);

		cursor = CursorManager.getInstance().getZoominCursor();

		startMapPos = new DirectPosition2D();
		dragged = false;
	}

	public ZoomInTool() {
		this(CursorTool.ANY_BUTTON);
	}

	@Override
	public void onMouseClicked(MapMouseEvent e) {

		if (!isTriggerMouseButton(e)) {
			return;
		}

		startMapPos = new DirectPosition2D();
		startMapPos.setLocation(e.getMapPosition());
		startPos = e.getPoint();
	}

	@Override
	public void onMousePressed(MapMouseEvent ev) {
	}

	@Override
	public void onMouseDragged(MapMouseEvent ev) {

		if (!isTriggerMouseButton(ev)) {
			return;
		}

		if (dragged) {
			Point endPos = ev.getPoint();
			Rectangle clientRect = mapControl.getClientRect();
			if (mapControl != null && !mapControl.isDisposed()) {
				GC gc = new GC(mapControl);
				Image swtImage = new Image(mapControl.getDisplay(),
						Utils.awtToSwt(mapControl.getBaseImage(), clientRect.width + 1, clientRect.height + 1));

				Image tmpImage = new Image(mapControl.getDisplay(), clientRect.width, clientRect.height);
				GC tmpGC = new GC(tmpImage);
				tmpGC.fillRectangle(0, 0, clientRect.width, clientRect.height);
				tmpGC.drawImage(swtImage, 0, 0);
				tmpGC.setLineWidth(lineWidth);
				tmpGC.setLineStyle(lineStyle);
				tmpGC.setForeground(lineColor);
				tmpGC.drawRectangle(Math.min(startPos.x, endPos.x), Math.min(startPos.y, endPos.y),
						Math.abs(startPos.x - endPos.x), Math.abs(startPos.y - endPos.y));
				gc.drawImage(tmpImage, 0, 0);
				tmpImage.dispose();
				swtImage.dispose();
			}
		}

		dragged = true;
	}

	@Override
	public void onMouseReleased(MapMouseEvent ev) {

		if (!isTriggerMouseButton(ev)) {
			return;
		}

		if (dragged) {
			Envelope2D env = new Envelope2D();
			env.setFrameFromDiagonal(startMapPos, ev.getMapPosition());
			dragged = false;
			getMapPane().setDisplayArea(new ReferencedEnvelope(env, getCRS()));
		} else {
			Rectangle paneArea = getMapPane().getClientArea();

			double scale = getMapPane().getWorldToScreenTransform().getScaleX();
			double newScale = scale * zoom;

			DirectPosition2D corner = new DirectPosition2D(startMapPos.getX() - 0.5d * paneArea.width / newScale,
					startMapPos.getY() + 0.5d * paneArea.height / newScale);

			Envelope2D newMapArea = new Envelope2D();
			newMapArea.setFrameFromCenter(startMapPos, corner);
			getMapPane().setDisplayArea(new ReferencedEnvelope(newMapArea, getCRS()));
		}

	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}
}