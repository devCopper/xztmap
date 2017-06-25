package com.ztmap.ui.listener.map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.geotools.map.MapContent;

import com.ztmap.ui.Application;

public class NewMapListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (Application.hasChanged) {
			MessageBox messageBox = new MessageBox(Application.shell, SWT.YES | SWT.NO);
			messageBox.setMessage("��ͼ�Ѿ��ı䣬�Ƿ񱣴棿");
			if (messageBox.open() == SWT.YES) {
				new SaveMapListener().widgetSelected(e);
			}

			Application.mapContent.dispose();
			Application.mapContent = new MapContent();
			Application.mapControl.setMapContent(Application.mapContent);
		}
	}

}
