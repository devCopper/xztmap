package com.ztmap.common;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLUtils {

	public static String format(String str) {
		try {
			Document document = null;
			document = DocumentHelper.parseText(str);
			// 格式化输出格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("gb2312");
			StringWriter writer = new StringWriter();
			// 格式化输出流
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			// 将document写入到输出流
			xmlWriter.write(document);
			xmlWriter.close();
			return writer.toString();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
