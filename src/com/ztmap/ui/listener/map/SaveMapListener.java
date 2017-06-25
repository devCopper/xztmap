package com.ztmap.ui.listener.map;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.ztmap.common.MapIOUtils;
import com.ztmap.ui.Application;

public class SaveMapListener extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (StringUtils.isBlank(Application.mapFilePath)) {
			new SaveAsMapListener().widgetSelected(e);
		} else {
			String map = MapIOUtils.writeMap(Application.mapContent);
			try {
				FileUtils.write(FileUtils.getFile(Application.mapFilePath), map, Charset.forName("utf-8"));
				Application.hasChanged = false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
