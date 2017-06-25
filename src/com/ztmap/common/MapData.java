package com.ztmap.common;

import java.util.List;

public class MapData {
	private String name;
	private int sizeX;
	private int sizeY;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private String crs;
	private List<LayerData> layers;

	public int getSizeX() {
		return sizeX;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public List<LayerData> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerData> layers) {
		this.layers = layers;
	}

}
