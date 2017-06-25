package com.ztmap.common;

import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.alibaba.fastjson.JSON;

public class MapIOUtils {
	public static String writeMap(MapContent map) {
		MapData mapData = new MapData();

		mapData.setName(map.getTitle());

		ReferencedEnvelope env = map.getViewport().getBounds();
		mapData.setCrs(env.getCoordinateReferenceSystem().toWKT());
		mapData.setMaxX(env.getMaxX());
		mapData.setMaxY(env.getMaxY());
		mapData.setMinX(env.getMinX());
		mapData.setMinY(env.getMinY());

		Rectangle screenArea = map.getViewport().getScreenArea();
		mapData.setSizeX(new BigDecimal(screenArea.getWidth()).intValue());
		mapData.setSizeY(new BigDecimal(screenArea.getWidth()).intValue());

		mapData.setLayers(new ArrayList<LayerData>());
		for (Layer layer : map.layers()) {
			LayerData layerData = new LayerData();
			layerData.setName(layer.getTitle());
			layerData.setVisible(layer.isVisible());
			if (layer.getUserData().containsKey("data")) {
				layerData.setData(String.valueOf(layer.getUserData().get("data")));
			}
			if (layer.getUserData().containsKey("sourceType")) {
				String fileType = String.valueOf(layer.getUserData().get("sourceType"));
				layerData.setSourceType(LayerSourceType.valueOf(fileType));
			}
			if (layer.getUserData().containsKey("dataType")) {
				String dataType = String.valueOf(layer.getUserData().get("dataType"));
				layerData.setDataType(LayerDataType.valueOf(dataType));
			}
			if (layer.getUserData().containsKey("style")) {
				String style = String.valueOf(layer.getUserData().get("style"));
				layerData.setStyle(style);
			}

			mapData.getLayers().add(layerData);
		}

		return JSON.toJSONString(mapData);
	}

	public static MapContent readMap(String mapDataString) {
		if (StringUtils.isBlank(mapDataString)) {
			return null;
		}
		MapData mapData = JSON.parseObject(mapDataString, MapData.class);
		if (mapData == null) {
			return null;
		}
		MapContent mapContent = new MapContent();
		mapContent.setTitle(mapData.getName());
		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.parseWKT(mapData.getCrs());
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		ReferencedEnvelope env = new ReferencedEnvelope(mapData.getMinX(), mapData.getMaxX(), mapData.getMinY(),
				mapData.getMaxX(), crs);
		mapContent.getViewport().setBounds(env);
		mapContent.getViewport().setScreenArea(new Rectangle(0, 0, mapData.getSizeX(), mapData.getSizeY()));

		for (LayerData layerData : mapData.getLayers()) {
			mapContent.addLayer(LayerFactory.createLayer(layerData));
		}
		return mapContent;
	}
}
