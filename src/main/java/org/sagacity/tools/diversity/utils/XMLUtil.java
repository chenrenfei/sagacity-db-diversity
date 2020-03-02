/**
 * 
 */
package org.sagacity.tools.diversity.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sagacity.tools.diversity.utils.callback.XMLCallbackHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @project sagacity-core
 * @description xml处理的工具类,提供xml对应schema validator等功能
 * @author chenrenfei <a href="mailto:zhongxuchen@hotmail.com">联系作者</a>
 * @version id:XMLUtil.java,Revision:v1.0,Date:2009-4-27 上午11:57:58
 */
public class XMLUtil {
	/**
	 * 定义日志
	 */
	private final static Logger logger = LogManager.getLogger(XMLUtil.class);

	// xml 忽视验证的特性
	private final static String NO_VALIDATOR_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

	private final static String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	/**
	 * @todo xml文件合法性验证
	 * @param xsdStream
	 * @param xmlStream
	 * @return
	 */
	public static boolean validate(InputStream xsdStream, InputStream xmlStream) {
		SchemaFactory factory = SchemaFactory.newInstance(XML_SCHEMA);
		try {
			Source xsdSource = new StreamSource(xsdStream);
			Schema schema = factory.newSchema(xsdSource);
			// Get a validator from the schema.
			Validator validator = schema.newValidator();

			// Parse the document you want to check.
			Source xmlSource = new StreamSource(xmlStream);
			// Check the document
			validator.validate(xmlSource);
			return true;
		} catch (IOException ioe) {
			logger.error("文件IO读取失败!" + ioe.getMessage());
			ioe.printStackTrace();
		} catch (SAXException ex) {
			logger.error("xml验证不合法:" + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * @todo xml文件合法性验证
	 * @param xsdUrl
	 * @param xmlUrl
	 * @return
	 */
	public static boolean validate(URL xsdUrl, URL xmlUrl) {
		SchemaFactory factory = SchemaFactory.newInstance(XML_SCHEMA);
		try {
			Schema schema = factory.newSchema(xsdUrl);
			// Get a validator from the schema.
			Validator validator = schema.newValidator();

			// Parse the document you want to check.
			Source xmlSource = new StreamSource(new FileInputStream(xmlUrl.getFile()));
			// Check the document
			validator.validate(xmlSource);
			return true;
		} catch (IOException ioe) {
			logger.error("文件IO读取失败!" + ioe.getMessage());
			ioe.printStackTrace();
		} catch (SAXException ex) {
			logger.error("xml验证不合法:" + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @todo 验证xml文件对应的schema文件是否匹配
	 * @param xsdFile
	 * @param xmlFile
	 * @return
	 */
	public static boolean validate(String xsdFile, String xmlFile) {
		SchemaFactory factory = SchemaFactory.newInstance(XML_SCHEMA);
		File schemaLocation = new File(xsdFile);
		try {
			Schema schema = factory.newSchema(schemaLocation);
			// Get a validator from the schema.
			Validator validator = schema.newValidator();

			// Parse the document you want to check.
			Source source = new StreamSource(xmlFile);
			// Check the document
			validator.validate(source);
			return true;
		} catch (IOException ioe) {
			logger.error("文件IO读取失败!" + ioe.getMessage());
			ioe.printStackTrace();
		} catch (SAXException ex) {
			logger.error(xmlFile + " is not valid " + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * @todo 获取qName对应的内容
	 * @param xmlFile
	 * @param xmlQuery
	 * @param qName
	 * @return
	 * @throws Exception
	 */
	public static Object getXPathContent(File xmlFile, String xmlQuery, QName qName) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		XPathFactory pathFactory = XPathFactory.newInstance();
		XPath xpath = pathFactory.newXPath();
		XPathExpression pathExpression = xpath.compile(xmlQuery);
		return pathExpression.evaluate(doc, qName);
	}

	/**
	 * 读取xml文件
	 * 
	 * @param xmlFile
	 * @param charset
	 * @param isValidator
	 * @param handler
	 * @throws Exception
	 */
	public static Object readXML(Object xmlFile, String charset, boolean isValidator, XMLCallbackHandler handler)
			throws Exception {
		if (StringUtil.isBlank(xmlFile))
			return null;
		InputStream fileIS = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if (!isValidator) {
				factory.setFeature(NO_VALIDATOR_FEATURE, false);
			}
			DocumentBuilder builder = factory.newDocumentBuilder();
			fileIS = FileUtil.getFileInputStream(xmlFile);
			if (fileIS != null) {
				Document doc = builder.parse(fileIS);
				if (null != doc) {
					return handler.process(doc, doc.getDocumentElement());
				}
			}
		} catch (Exception e) {
			logger.error("解析文件:{}错误:{}!", xmlFile, e.getMessage());
			throw e;
		} finally {
			if (fileIS != null)
				fileIS.close();
		}
		return null;
	}
}
