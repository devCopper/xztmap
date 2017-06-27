package com.ztmap.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;

import com.ztmap.ui.common.MenuFactory;

public class LayerControl extends Tree {

	private static final String NODE_TYPE = "nodeType";
	private static final String NODE_MAP = "map";
	private static final String NODE_LAYER = "layer";
	private static final String NODE_LEGEND = "legend";
	
	private TreeItem root = null;
	private Menu mapMenu = null;
	private Menu layerMenu = null;
	private Menu legendMenu = null;
	private MapContent mapContent = null;

	public LayerControl(Composite parent) {
		super(parent, SWT.BORDER | SWT.CHECK);

		final TreeItem[] lastItem = new TreeItem[1];
		final TreeEditor editor = new TreeEditor(this);
		final Color editBGColor = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);

		initContextMenu();
		initDragDrop();

		this.addListener(SWT.Selection, event -> {
			final TreeItem item = (TreeItem) event.item;
			if (event.detail == SWT.CHECK) {
				boolean checked = item.getChecked();
				checkItems(item, checked);
				checkPath(item.getParentItem(), checked, false);
			}
		});

		this.addListener(SWT.Selection, event -> {
			final TreeItem item = (TreeItem) event.item;
			if (item == null) {
				return;
			}
			
			if (NODE_LAYER.equals(item.getData(NODE_TYPE))) {
				for (Layer layer : mapContent.layers()) {
					if (layer.equals(item.getData())) {
						layer.setSelected(true);
					} else {
						layer.setSelected(false);
					}
				}
			}
			
			if (event.detail != SWT.CHECK && item == lastItem[0]) {
				boolean showBorder = true;
				final Composite composite = new Composite(this, SWT.NONE);
				if (showBorder)
					composite.setBackground(editBGColor);
				final Text text = new Text(composite, SWT.NONE);
				final int inset = showBorder ? 1 : 0;
				composite.addListener(SWT.Resize, e1 -> {
					Rectangle rect1 = composite.getClientArea();
					text.setBounds(rect1.x + inset, rect1.y + inset, rect1.width - inset * 2, rect1.height - inset * 2);
				});
				Listener textListener = getTextListener(editor, item, composite, text, inset);
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				text.addListener(SWT.Verify, textListener);
				editor.setEditor(composite, item);
				text.setText(item.getText());
				text.selectAll();
				text.setFocus();
			}
			lastItem[0] = item;
		});

	}

	public void setMapContent(MapContent mapContent) {
		this.removeAll();

		this.mapContent = mapContent;

		root = new TreeItem(this, SWT.NONE);
		root.setText(mapContent.getTitle());
		root.setData(NODE_TYPE, NODE_MAP);

		for (Layer layer : mapContent.layers()) {
			addLayer(layer);
		}
	}

	public void addLayer(Layer layer) {
		if (root != null && mapContent != null) {
			TreeItem item = new TreeItem(root, SWT.NONE);
			item.setText(layer.getTitle());
			item.setData(NODE_TYPE, NODE_LAYER);
			item.setChecked(layer.isVisible());
			item.setData(layer);
			root.setExpanded(true);
		}
	}

	public void removeLayer(Layer layer) {

	}

	public void layerChanged(Layer layer) {

	}

	public Layer getSelectedLayer() {
		TreeItem[] items = getSelection();
		if (items.length <= 0) {
			return null;
		}
		return (Layer) items[0].getData();
	}

	private Listener getTextListener(final TreeEditor editor, final TreeItem item, final Composite composite,
			final Text text, final int inset) {
		Listener textListener = e2 -> {
			switch (e2.type) {
			case SWT.FocusOut:
				item.setText(text.getText());
				composite.dispose();
				break;
			case SWT.Verify:
				String newText = text.getText();
				String leftText = newText.substring(0, e2.start);
				String rightText = newText.substring(e2.end, newText.length());
				GC gc = new GC(text);
				Point size = gc.textExtent(leftText + e2.text + rightText);
				gc.dispose();
				size = text.computeSize(size.x, SWT.DEFAULT);
				editor.horizontalAlignment = SWT.LEFT;
				Rectangle itemRect = item.getBounds(), rect2 = this.getClientArea();
				editor.minimumWidth = Math.max(size.x, itemRect.width) + inset * 2;
				int left = itemRect.x, right = rect2.x + rect2.width;
				editor.minimumWidth = Math.min(editor.minimumWidth, right - left);
				editor.minimumHeight = size.y + inset * 2;
				editor.layout();
				break;
			case SWT.Traverse:
				switch (e2.detail) {
				case SWT.TRAVERSE_RETURN:
					item.setText(text.getText());
					// FALL THROUGH
				case SWT.TRAVERSE_ESCAPE:
					composite.dispose();
					e2.doit = false;
				}
				break;
			}
		};
		return textListener;
	}

	private void initContextMenu() {
		mapMenu = new Menu(this);
		MenuFactory.createMenuItems(mapMenu, "resources/menu/menu_map.json");
		layerMenu = new Menu(this);
		MenuFactory.createMenuItems(layerMenu, "resources/menu/menu_layer.json");
		legendMenu = new Menu(this);
		MenuFactory.createMenuItems(legendMenu, "resources/menu/menu_legend.json");

		this.addListener(SWT.MouseDown, event -> {
			TreeItem[] items = getSelection();
			if (items.length <= 0) {
				return;
			}
			if (NODE_MAP.equals(items[0].getData(NODE_TYPE))) {
				this.setMenu(mapMenu);
			} else if (NODE_LAYER.equals(items[0].getData(NODE_TYPE))) {
				this.setMenu(layerMenu);
			} else if (NODE_LEGEND.equals(items[0].getData(NODE_TYPE))) {
				this.setMenu(legendMenu);
			}
		});
	}

	private void initDragDrop() {
		Tree that = this;

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(that, operations);
		source.setTransfer(types);
		final TreeItem[] dragSourceItem = new TreeItem[1];
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = that.getSelection();
				if (selection.length > 0 && selection[0].getItemCount() == 0) {
					event.doit = true;
					dragSourceItem[0] = selection[0];
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				event.data = dragSourceItem[0].getText();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE)
					dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
			}
		});

		DropTarget target = new DropTarget(that, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
				if (event.item != null) {
					TreeItem item = (TreeItem) event.item;
					Point pt = Display.getCurrent().map(null, that, event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				String text = (String) event.data;
				if (event.item == null) {
					TreeItem item = new TreeItem(that, SWT.NONE);
					item.setText(text);
				} else {
					TreeItem item = (TreeItem) event.item;
					Point pt = Display.getCurrent().map(null, that, event.x, event.y);
					Rectangle bounds = item.getBounds();
					TreeItem parent = item.getParentItem();
					if (parent != null) {
						TreeItem[] items = parent.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {
							TreeItem newItem = new TreeItem(parent, SWT.NONE, index);
							newItem.setText(text);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(parent, SWT.NONE, index + 1);
							newItem.setText(text);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
						}

					} else {
						TreeItem[] items = that.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {
							TreeItem newItem = new TreeItem(that, SWT.NONE, index);
							newItem.setText(text);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(that, SWT.NONE, index + 1);
							newItem.setText(text);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
						}
					}

				}
			}
		});
	}

	private void checkPath(TreeItem item, boolean checked, boolean grayed) {
		if (item == null)
			return;
		if (grayed) {
			checked = true;
		} else {
			int index = 0;
			TreeItem[] items = item.getItems();
			while (index < items.length) {
				TreeItem child = items[index];
				if (child.getGrayed() || checked != child.getChecked()) {
					checked = grayed = true;
					break;
				}
				index++;
			}
		}
		item.setChecked(checked);
		item.setGrayed(grayed);
		checkPath(item.getParentItem(), checked, grayed);
	}

	private void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			checkItems(items[i], checked);
		}
	}

	@Override
	protected void checkSubclass() {
	}
}
