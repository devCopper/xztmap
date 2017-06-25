package com.ztmap.ui.listener.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ztmap.control.tool.ZoomInTool;
import com.ztmap.ui.Application;

public class ZoomInListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Application.mapControl.setCursorTool(new ZoomInTool());
	}

}
