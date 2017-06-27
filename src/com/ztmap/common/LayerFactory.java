package com.ztmap.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.Style;

public class LayerFactory {
	public static Layer createLayer(LayerData layerData) {
		switch (layerData.getSourceType()) {
		case shapefile:
			return createShapefileLayer(layerData);
		default:
			return null;
		}
	}

	public static LayerData createShapefileLayerData(String data) {
		if (StringUtils.isBlank(data)) {
			return null;
		}
		if (!data.endsWith(LayerFileSuffix.shp.name())) {
			return null;
		}
		if (!FileUtils.getFile(data).exists()) {
			return null;
		}
		LayerData layerData = new LayerData();
		layerData.setData(data);
		layerData.setDataType(LayerDataType.Vector);
		layerData.setName(FilenameUtils.getBaseName(data));
		layerData.setSourceType(LayerSourceType.shapefile);
		layerData.setStyle(data.substring(0, data.lastIndexOf(".")) + ".sld");
		layerData.setVisible(true);
		return layerData;
	}

	public static Layer createShapefileLayer(LayerData layerData) {
		if (layerData == null) {
			return null;
		}
		try {
			File dataFile = FileUtils.getFile(layerData.getData());
			if (!dataFile.exists()) {
				return null;
			}

			FileDataStore myData = FileDataStoreFinder.getDataStore(dataFile);
			SimpleFeatureSource source = myData.getFeatureSource();

			Style style = null;
			File styleFile = FileUtils.getFile(layerData.getStyle());
			if (!styleFile.exists()) {
				style = StyleFactory.createDefaultStyle(source.getSchema());
			} else {
				style = StyleFactory.read(layerData.getStyle());
			}

			FeatureLayer layer = new FeatureLayer(source, style);
			layer.setTitle(layerData.getName());
			setLayerUserData(layer, layerData);

			return layer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static LayerData createTiffLayerData(String data) {
		LayerData layerData = new LayerData();
		layerData.setData(data);
		layerData.setDataType(LayerDataType.Raster);
		layerData.setName(FilenameUtils.getBaseName(data));
		layerData.setSourceType(LayerSourceType.tiff);
		layerData.setStyle(data.substring(0, data.lastIndexOf(".")) + ".sld");
		layerData.setVisible(true);
		return layerData;
	}

	public static Layer createTiffLayer(LayerData layerData) {

		return null;
	}

	public static LayerData createGoogleTileLayerData(String data) {
		LayerData layerData = new LayerData();
		layerData.setData(data);
		layerData.setDataType(LayerDataType.Direct);
		layerData.setName(FilenameUtils.getBaseName(data));
		layerData.setSourceType(LayerSourceType.googletile);
		layerData.setVisible(true);
		return layerData;
	}

	public static Layer createGoogleTileLayer(LayerData layerData) {

		return null;
	}

	private static void setLayerUserData(Layer layer, LayerData layerData) {
		layer.getUserData().put("data", layerData.getData());
		layer.getUserData().put("sourceType", layerData.getSourceType());
		layer.getUserData().put("dataType", layerData.getDataType());
		layer.getUserData().put("style", layerData.getStyle());
	}
}
