package com.ztmap.control;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.ztmap.control.event.MapMouseListener;
import com.ztmap.control.tool.CursorTool;
import com.ztmap.control.tool.ToolManager;
import com.ztmap.control.utils.CursorManager;
import com.ztmap.control.utils.Utils;

public class MapControl extends Canvas implements MapLayerListListener {

	public static final int DEFAULT_RESIZING_PAINT_DELAY = 500;

	private MapContent mapContent;
	private LayerControl layerControl;
	private GTRenderer renderer;
	private ToolManager toolManager;
	private AffineTransform worldToScreen;
	private AffineTransform screenToWorld;
	private Rectangle curPaintArea;
	private BufferedImage baseImage;

	public MapControl(Composite parent, int style) {
		this(parent, style, null);
		renderer = new StreamingRenderer();
	}

	public MapControl(Composite parent, int style, MapContent content) {
		super(parent, style);

		setMapContent(content);

		toolManager = new ToolManager(this);

		this.addMouseListener(toolManager);
		this.addMouseMoveListener(toolManager);
		this.addMouseWheelListener(toolManager);

		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				curPaintArea = getClientArea();
				setTransforms(mapContent.getViewport().getBounds(), curPaintArea);
				setDisplayArea(mapContent.getViewport().getBounds());
			}
		});
	}

	public void setCursorTool(CursorTool tool) {
		if (tool == null) {
			toolManager.setNoCursorTool();
			this.setCursor(CursorManager.getInstance().getArrowCursor());
		} else {
			this.setCursor(tool.getCursor());
			toolManager.setCursorTool(tool);
		}
	}

	public void addMouseListener(MapMouseListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("parameter is null.");
		}

		toolManager.addMouseListener(listener);
	}

	public void removeMouseListener(MapMouseListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("parameter is null.");
		}

		toolManager.removeMouseListener(listener);
	}

	public void setLayerControl(LayerControl layerControl) {
		if (layerControl == null) {
			throw new IllegalArgumentException("parameter is null"); //$NON-NLS-1$
		}

		this.layerControl = layerControl;
	}

	public MapContent getMapContent() {
		return mapContent;
	}

	public void setMapContent(MapContent content) {
		if (mapContent == content || content == null) {
			return;
		}

		if (mapContent != null) {
			mapContent.removeMapLayerListListener(this);
		}

		mapContent = content;
		mapContent.addMapLayerListListener(this);
		if (layerControl != null) {
			layerControl.setMapContent(content);
		}

		renderer.setMapContent(this.mapContent);
		ReferencedEnvelope bound = mapContent.getViewport().getBounds();
		if (mapContent.getViewport().getBounds() != null && curPaintArea != null) {
			setTransforms(bound, curPaintArea);
			setDisplayArea(bound);
			return;
		}

		if (mapContent.layers().size() <= 0) {
			return;
		}

		try {
			ReferencedEnvelope env = mapContent.layers().get(0).getBounds();
			CoordinateReferenceSystem targetCRS = env.getCoordinateReferenceSystem();
			for (int i = 1; i < mapContent.layers().size(); i++) {
				ReferencedEnvelope bounds = mapContent.layers().get(i).getBounds();
				CoordinateReferenceSystem sourceCRS = bounds.getCoordinateReferenceSystem();
				if (!sourceCRS.equals(targetCRS)) {
					MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
					com.vividsolutions.jts.geom.Envelope newJtsEnv = JTS.transform(bounds, transform);
					env.expandToInclude(newJtsEnv);
				}
			}
			mapContent.getViewport().setBounds(env);
			setTransforms(env, curPaintArea);
			setDisplayArea(env);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setCrs(CoordinateReferenceSystem crs) {
		try {
			ReferencedEnvelope rEnv = mapContent.getViewport().getBounds();
			if (rEnv == null) {
				return;
			}

			CoordinateReferenceSystem sourceCRS = rEnv.getCoordinateReferenceSystem();
			CoordinateReferenceSystem targetCRS = crs;

			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			com.vividsolutions.jts.geom.Envelope newJtsEnv = JTS.transform(rEnv, transform);

			ReferencedEnvelope newEnvelope = new ReferencedEnvelope(newJtsEnv, targetCRS);
			mapContent.getViewport().setBounds(newEnvelope);
			setDisplayArea(newEnvelope);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDisplayArea(ReferencedEnvelope envelope) {
		if (mapContent == null && curPaintArea == null && curPaintArea.isEmpty()) {
			return;
		}

		setTransforms(envelope, curPaintArea);
		ReferencedEnvelope adjustedEnvelope = getDisplayArea();
		mapContent.getViewport().setBounds(adjustedEnvelope);

		if (!isDisposed()) {
			redrawMap(true);
		}
	}

	public BufferedImage getBaseImage() {
		return this.baseImage;
	}

	public AffineTransform getScreenToWorldTransform() {
		if (screenToWorld != null) {
			return new AffineTransform(screenToWorld);
		} else {
			return null;
		}
	}

	public AffineTransform getWorldToScreenTransform() {
		if (worldToScreen != null) {
			return new AffineTransform(worldToScreen);
		} else {
			return null;
		}
	}

	public void layerAdded(MapLayerListEvent event) {
		Layer layer = event.getElement();
		if (layerControl != null) {
			layerControl.addLayer(layer);
		}

		ReferencedEnvelope env = mapContent.layers().get(0).getBounds();
		mapContent.getViewport().setBounds(env);

		if (!isDisposed()) {
			redrawMap(true);
		}
	}

	public void layerRemoved(MapLayerListEvent event) {
		Layer layer = event.getElement();
		if (layerControl != null) {
			layerControl.removeLayer(layer);
		}

		if (!isDisposed()) {
			redrawMap(true);
		}
	}

	public void layerChanged(MapLayerListEvent event) {
		if (layerControl != null) {
			layerControl.layerChanged(event.getElement());
		}

		int reason = event.getMapLayerEvent().getReason();
		if (reason != MapLayerEvent.SELECTION_CHANGED) {
			if (!isDisposed()) {
				redrawMap(true);
			}
		}
	}

	public void layerMoved(MapLayerListEvent event) {
		if (!isDisposed()) {
			redrawMap(true);
		}
	}

	public void layerPreDispose(MapLayerListEvent event) {

	}

	public void mapBoundsChanged(MapBoundsEvent event) {
	}

	private void setTransforms(final Envelope envelope, final Rectangle paintArea) {
		ReferencedEnvelope refEnv = null;
		if (envelope != null) {
			refEnv = new ReferencedEnvelope(envelope);
		} else {
			refEnv = worldEnvelope();
		}

		java.awt.Rectangle awtPaintArea = Utils.toAwtRectangle(paintArea);
		double xscale = awtPaintArea.getWidth() / refEnv.getWidth();
		double yscale = awtPaintArea.getHeight() / refEnv.getHeight();

		double scale = Math.min(xscale, yscale);

		double xoff = refEnv.getMedian(0) * scale - awtPaintArea.getCenterX();
		double yoff = refEnv.getMedian(1) * scale + awtPaintArea.getCenterY();

		worldToScreen = new AffineTransform(scale, 0, 0, -scale, -xoff, yoff);
		try {
			screenToWorld = worldToScreen.createInverse();

		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
		}
		mapContent.getViewport().setBounds(getDisplayArea());
	}

	public ReferencedEnvelope getDisplayArea() {
		ReferencedEnvelope env = null;

		if (curPaintArea != null && screenToWorld != null) {
			Rectangle2D awtRectangle = Utils.toAwtRectangle(curPaintArea);
			Point2D p0 = new Point2D.Double(awtRectangle.getMinX(), awtRectangle.getMinY());
			Point2D p1 = new Point2D.Double(awtRectangle.getMaxX(), awtRectangle.getMaxY());
			screenToWorld.transform(p0, p0);
			screenToWorld.transform(p1, p1);

			env = new ReferencedEnvelope(Math.min(p0.getX(), p1.getX()), Math.max(p0.getX(), p1.getX()),
					Math.min(p0.getY(), p1.getY()), Math.max(p0.getY(), p1.getY()),
					mapContent.getCoordinateReferenceSystem());
		}

		return env;
	}

	private ReferencedEnvelope worldEnvelope() {
		return new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
	}

	public void redrawMap(boolean redrawBaseImage) {
		GC gc = new GC(this);

		final ReferencedEnvelope mapAOI = mapContent.getViewport().getBounds();
		if (mapAOI == null) {
			return;
		}

		if (redrawBaseImage) {
			baseImage = new BufferedImage(curPaintArea.width + 1, curPaintArea.height + 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = baseImage.createGraphics();
			g2d.fillRect(0, 0, curPaintArea.width + 1, curPaintArea.height + 1);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			java.awt.Rectangle awtRectangle = Utils.toAwtRectangle(curPaintArea);
			renderer.paint(g2d, awtRectangle, mapAOI, getWorldToScreenTransform());

			Image swtImage = new Image(getDisplay(),
					Utils.awtToSwt(baseImage, curPaintArea.width + 1, curPaintArea.height + 1));

			if (gc != null && !gc.isDisposed()) {
				Image tmpImage = new Image(getDisplay(), curPaintArea.width + 1, curPaintArea.height + 1);
				GC tmpGc = new GC(tmpImage);
				tmpGc.fillRectangle(0, 0, curPaintArea.width, curPaintArea.height);
				tmpGc.drawImage(swtImage, 0, 0);
				gc.drawImage(tmpImage, 0, 0);
				tmpImage.dispose();
			}
		}
	}

	public Rectangle getClientRect() {
		return curPaintArea;
	}

}
