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
			// ��ʽ�������ʽ
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("gb2312");
			StringWriter writer = new StringWriter();
			// ��ʽ�������
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			// ��documentд�뵽�����
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
