package com.ztmap.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.geotools.map.MapContent;

import com.ztmap.control.LayerControl;
import com.ztmap.control.MapControl;
import com.ztmap.ui.common.MenuFactory;

public class Startup {
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
		MenuFactory.createMenuItems(fileMenu, "resources/menu/menu_main_file.json");

		MenuItem viewItem = new MenuItem(bar, SWT.CASCADE);
		viewItem.setText("View");
		Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
		viewItem.setMenu(viewMenu);
		MenuFactory.createMenuItems(viewMenu, "resources/menu/menu_main_view.json");

		MenuItem layerItem = new MenuItem(bar, SWT.CASCADE);
		layerItem.setText("Layer");
		Menu layerMenu = new Menu(shell, SWT.DROP_DOWN);
		layerItem.setMenu(layerMenu);
		MenuFactory.createMenuItems(layerMenu, "resources/menu/menu_main_layer.json");

		// MenuItem item = new MenuItem(viewMenu, SWT.PUSH);
		// item.addListener(SWT.Selection, e -> System.out.println("Select
		// All"));
		// item.setText("Select &All\tCtrl+A");
		// item.setAccelerator(SWT.MOD1 + 'A');

		SashForm form = new SashForm(shell, SWT.HORIZONTAL);
		form.setLayout(new FillLayout());

		Composite leftPanel = new Composite(form, SWT.BORDER);
		leftPanel.setLayout(new FillLayout());
		LayerControl layerControl = new LayerControl(leftPanel);

		Composite rightPanel = new Composite(form, SWT.BORDER);
		rightPanel.setLayout(new FillLayout());
		MapControl mapControl = new MapControl(rightPanel, SWT.BORDER);

		mapControl.setLayerControl(layerControl);

		Application.shell = shell;
		Application.mapControl = mapControl;
		Application.layerControl = layerControl;
		Application.mapContent = new MapContent();
		Application.mapContent.setTitle("NewMap");
		Application.mapControl.setMapContent(Application.mapContent);

		form.setWeights(new int[] { 30, 70 });
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}
