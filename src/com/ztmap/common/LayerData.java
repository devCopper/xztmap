package com.ztmap.common;

public class LayerData {
	private String name;
	private Boolean visible;
	private String data;
	private LayerSourceType sourceType;
	private LayerDataType dataType;
	private String style;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public LayerSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(LayerSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public LayerDataType getDataType() {
		return dataType;
	}

	public void setDataType(LayerDataType dataType) {
		this.dataType = dataType;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
