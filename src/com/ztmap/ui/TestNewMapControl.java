package com.ztmap.ui;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.geotools.map.MapContent;

import com.ztmap.common.MapIOUtils;
import com.ztmap.control.MapControl;

public class TestNewMapControl {
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileMenu);

		MenuItem menuItem = new MenuItem(fileMenu, SWT.PUSH);
		menuItem.setText("Zoom All");

		MapControl mapControl = new MapControl(shell, SWT.BORDER);

		String filePath = "D:/NewMap2.map";
		String mapData = null;
		try {
			mapData = IOUtils.toString(FileUtils.getFile(filePath).toURI(), Charset.forName("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MapContent mapContent = MapIOUtils.readMap(mapData);
		mapControl.setMapContent(mapContent);

		menuItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				System.out.println("Zoom All click.");
				mapControl.setDisplayArea(mapContent.getMaxBounds());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
				// TODO Auto-generated method stub

			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}
