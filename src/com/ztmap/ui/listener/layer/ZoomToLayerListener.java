package com.ztmap.ui.listener.layer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.geotools.map.Layer;

import com.ztmap.ui.Application;

public class ZoomToLayerListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (Application.mapContent == null) {
			return;
		}
		
		for (Layer layer : Application.mapContent.layers()) {
			if (layer.isSelected()) {
				Application.mapControl.setDisplayArea(layer.getBounds());
			}
		}
	}

}
