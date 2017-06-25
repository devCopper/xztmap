package com.ztmap.ui.listener.map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.geotools.map.Layer;

import com.ztmap.common.LayerData;
import com.ztmap.common.LayerFactory;
import com.ztmap.ui.Application;

public class AddShapefileLayerListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		FileDialog dialog = new FileDialog(Application.shell, SWT.OPEN);
		String[] filterNames = new String[] { "(*.shp)|Shapefiles Files" };
		String[] filterExtensions = new String[] { "*.shp" };
		String filterPath = "/";
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.setFileName(Application.mapContent.getTitle());
		String filePath = dialog.open();
		if (StringUtils.isBlank(filePath)) {
			return;
		}

		LayerData layerData = LayerFactory.createShapefileLayerData(filePath);
		Layer layer = LayerFactory.createLayer(layerData);
		if (layer != null) {
			Application.mapContent.addLayer(layer);
			Application.hasChanged = true;
		}
	}

}
