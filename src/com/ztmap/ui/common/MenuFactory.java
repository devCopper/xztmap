package com.ztmap.ui.common;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.alibaba.fastjson.JSON;

public class MenuFactory {
	@SuppressWarnings("unchecked")
	public static void createMenuItems(Menu menu, String menuItemsFilePath) {
		String menuItemsData = readFile(menuItemsFilePath);
		if (StringUtils.isBlank(menuItemsData)) {
			return;
		}
		List<MenuItemBean> menuItems = JSON.parseArray(menuItemsData, MenuItemBean.class);

		for (MenuItemBean menuItemBean : menuItems) {
			MenuItem menuItem = new MenuItem(menu, menuItemBean.getStyle());
			menuItem.setText(menuItemBean.getText());
			try {
				Class<?> adpter = Class.forName(menuItemBean.getListener());
				menuItem.addSelectionListener(((Class<SelectionAdapter>) adpter).newInstance());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static String readFile(String fileName) {
		try {
			return IOUtils.toString(FileUtils.getFile(fileName).toURI(), Charset.forName("utf-8"));
		} catch (Exception e) {
			return null;
		}
	}

}
