package com.ztmap.common;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class StyleFactory {
	public static final String SLD_SUFFIX = "sld";
	public static org.geotools.styling.StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
	public static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
	public static StyleBuilder sb = new StyleBuilder(styleFactory, filterFactory);

	public static Style createDefaultStyle(SimpleFeatureType type) {
		Class<?> geomType = type.getBinding();

		if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
			return createPolygonStyle();

		} else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
			return createLineStyle();

		} else {
			return createPointStyle();
		}
	}

	public static Style createPolygonStyle() {

		// create a partially opaque outline stroke
		Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLUE), filterFactory.literal(1),
				filterFactory.literal(0.5));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(Color.CYAN), filterFactory.literal(0.5));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * Create a default line style.
	 * 
	 * @return the created style.
	 */
	public static Style createLineStyle() {
		Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLUE), filterFactory.literal(1));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	/**
	 * Create a default point style.
	 * 
	 * @return the created style.
	 */
	public static Style createPointStyle() {
		Graphic gr = styleFactory.createDefaultGraphic();

		Mark mark = styleFactory.getCircleMark();

		mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

		mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

		gr.graphicalSymbols().clear();
		gr.graphicalSymbols().add(mark);
		gr.setSize(filterFactory.literal(5));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	public static Style read(String sldFilePath) {
		org.geotools.styling.StyleFactory factory = CommonFactoryFinder.getStyleFactory();
		try {
			SLDParser sldParser = new SLDParser(factory, FileUtils.getFile(sldFilePath));
			Style[] styles = sldParser.readXML();
			if (styles != null && styles.length > 0) {
				return styles[0];
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void write(String sldFilePath, Style style) {
		SLDTransformer styleTransform = new SLDTransformer();
		try {
			String sld = styleTransform.transform(style);
			FileUtils.write(FileUtils.getFile(sldFilePath), XMLUtils.format(sld), Charset.forName("utf-8"));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
