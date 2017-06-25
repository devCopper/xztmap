package com.ztmap.ui.listener.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ztmap.control.tool.ZoomOutTool;
import com.ztmap.ui.Application;

public class ZoomOutListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Application.mapControl.setCursorTool(new ZoomOutTool());
	}
}
