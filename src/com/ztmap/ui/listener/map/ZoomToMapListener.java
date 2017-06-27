package com.ztmap.ui.listener.map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ztmap.ui.Application;

public class ZoomToMapListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (Application.mapContent != null) {
			Application.mapControl.setDisplayArea(Application.mapContent.getMaxBounds());
		}
	}

}
