package com.ztmap.ui.listener.map;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.geotools.map.MapContent;

import com.ztmap.common.MapIOUtils;
import com.ztmap.ui.Application;

public class OpenMapListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (Application.hasChanged) {
			MessageBox messageBox = new MessageBox(Application.shell, SWT.YES | SWT.NO);
			messageBox.setMessage("地图已经改变，是否保存？");
			if (messageBox.open() == SWT.YES) {
				new SaveMapListener().widgetSelected(e);
			}
		}

		FileDialog dialog = new FileDialog(Application.shell, SWT.OPEN);
		String[] filterNames = new String[] { "(*.map)|ZTMap Files" };
		String[] filterExtensions = new String[] { "*.map" };
		String filterPath = "/";
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		String filePath = dialog.open();
		if (StringUtils.isBlank(filePath)) {
			return;
		}

		try {
			String mapData = IOUtils.toString(FileUtils.getFile(filePath).toURI(), Charset.forName("utf-8"));
			MapContent mapContent = MapIOUtils.readMap(mapData);
			if (mapContent != null) {
				Application.mapControl.setMapContent(mapContent);
				Application.mapContent = mapContent;
				Application.mapFilePath = filePath;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
