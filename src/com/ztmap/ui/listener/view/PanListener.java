package com.ztmap.ui.listener.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ztmap.control.tool.PanTool;
import com.ztmap.ui.Application;

public class PanListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Application.mapControl.setCursorTool(new PanTool());
	}
}
