package com.ztmap.ui;

import org.eclipse.swt.widgets.Shell;
import org.geotools.map.MapContent;

import com.ztmap.control.LayerControl;
import com.ztmap.control.MapControl;

public class Application {
	public static boolean hasChanged = false;
	public static MapContent mapContent = new MapContent();
	public static String mapFilePath = null;

	public static MapControl mapControl = null;
	public static LayerControl layerControl = null;
	public static Shell shell = null;

}
