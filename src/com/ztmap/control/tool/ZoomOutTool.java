package com.ztmap.control.tool;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;

import com.ztmap.control.event.MapMouseEvent;
import com.ztmap.control.utils.CursorManager;

public class ZoomOutTool extends ZoomTool {

	public static final String TOOL_NAME = "Zoom Out";
	public static final String TOOL_TIP = "Zoom Out Map";
	private Cursor cursor;

	public ZoomOutTool(int triggerButtonMask) {
		super(triggerButtonMask);
		cursor = CursorManager.getInstance().getZoomoutCursor();
	}

	public ZoomOutTool() {
		this(CursorTool.ANY_BUTTON);
	}

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
		getMapPane().setDisplayArea(new ReferencedEnvelope(newMapArea, getCRS()));
	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}

}
