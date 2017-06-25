package com.ztmap.ui.listener.map;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

import com.ztmap.common.MapIOUtils;
import com.ztmap.ui.Application;

public class SaveAsMapListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		FileDialog dialog = new FileDialog(Application.shell, SWT.SAVE);
		String[] filterNames = new String[] { "(*.map) | Map Files" };
		String[] filterExtensions = new String[] { "*.map" };
		String filterPath = "/";
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		if (!StringUtils.isBlank(Application.mapFilePath)) {
			dialog.setFilterPath(filterPath);
		} else {
			dialog.setFilterPath(filterPath);
		}
		dialog.setFileName(Application.mapContent.getTitle());
		String filePath = dialog.open();
		if (StringUtils.isBlank(filePath)) {
			return;
		}
		String map = MapIOUtils.writeMap(Application.mapContent);
		try {
			FileUtils.write(FileUtils.getFile(filePath), map, Charset.forName("utf-8"));
			Application.hasChanged = false;
			Application.mapFilePath = filePath;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
