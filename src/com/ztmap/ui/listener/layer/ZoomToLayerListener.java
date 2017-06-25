package com.ztmap.ui.listener.layer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.geotools.map.Layer;

import com.ztmap.ui.Application;

public class ZoomToLayerListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Layer layer = Application.layerControl.getSelectedLayer();
		if (layer == null) {
			return;
		}
		Application.mapControl.setDisplayArea(layer.getBounds());
	}

}
