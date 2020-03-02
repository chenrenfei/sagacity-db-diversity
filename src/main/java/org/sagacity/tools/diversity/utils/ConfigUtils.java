/**
 * 
 */
package org.sagacity.tools.diversity.utils;

import org.sagacity.tools.diversity.DiversityConstants;
import org.sagacity.tools.diversity.model.DataSourceModel;
import org.sagacity.tools.diversity.model.DiversityModel;
import org.sagacity.tools.diversity.utils.callback.XMLCallbackHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author zhongxuchen
 *         <db-diversity only-differ="true" report-template="" report-file="">
 *         <reference-db url="" driver="" username="" password="" />
 *         <target-db url="" driver="" username="" password="" />
 *         </db-diversity>
 */
public class ConfigUtils {
	public static DiversityModel parse() throws Exception {
		String configXML = DiversityConstants.CONFIG_FILE;
		DiversityModel diversityModel = null;
		if (StringUtil.isBlank(configXML)) {
			return diversityModel;
		}
		String realConfig = configXML;
		if (!FileUtil.isRootPath(realConfig))
			realConfig = FileUtil.linkPath(DiversityConstants.BASE_DIR, configXML);
		diversityModel = (DiversityModel) XMLUtil.readXML(realConfig, DiversityConstants.ENCODING, false,
				new XMLCallbackHandler() {
					@Override
					public Object process(Document doc, Element root) throws Exception {
						DiversityModel diversityModel = new DiversityModel();
						String var = root.getAttribute("only-differ");
						if (StringUtil.isNotBlank(var))
							diversityModel.setOnlyDiffer(Boolean.parseBoolean(var));
						var = root.getAttribute("report-template");
						if (StringUtil.isNotBlank(var))
							diversityModel.setReportTemplate(var);
						var = root.getAttribute("report-file");
						if (StringUtil.isNotBlank(var)) {
							diversityModel.setReportFile(var);
						}
						// 忽视注释
						var = root.getAttribute("ignore-comment");
						if (StringUtil.isNotBlank(var))
							diversityModel.setIgnorComment(Boolean.parseBoolean(var));
						// 忽视主键名称
						var = root.getAttribute("ignore-pk-name");
						if (StringUtil.isNotBlank(var))
							diversityModel.setIgnorePkName(Boolean.parseBoolean(var));
						// 忽视索引名称
						var = root.getAttribute("ignore-index-name");
						if (StringUtil.isNotBlank(var))
							diversityModel.setIgnoreIndexName(Boolean.parseBoolean(var));
						// 忽视外键名称
						var = root.getAttribute("ignore-foreign-name");
						if (StringUtil.isNotBlank(var))
							diversityModel.setIgnoreForeignKeyName(Boolean.parseBoolean(var));
						// 忽视被其他表关联的名称
						var = root.getAttribute("ignore-export-name");
						if (StringUtil.isNotBlank(var))
							diversityModel.setIgnoreExportKeyName(Boolean.parseBoolean(var));
						var = root.getAttribute("include-tables");
						if (StringUtil.isNotBlank(var))
							diversityModel.setInclude(new String[] { var });
						var = root.getAttribute("exclude-tables");
						if (StringUtil.isNotBlank(var))
							diversityModel.setExclude(new String[] { var });
						String[] dbs = { "reference-db", "target-db" };
						Element elt;
						// String value;
						Element child;
						NodeList nodeList;
						for (String name : dbs) {
							elt = (Element) root.getElementsByTagName(name).item(0);
							DataSourceModel dbModel = new DataSourceModel();
							dbModel.setName(elt.getAttribute("name"));
							if (StringUtil.isBlank(dbModel.getName())) {
								dbModel.setName(name);
							}
							nodeList = elt.getElementsByTagName("url");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setUrl(child.getAttribute("value"));
								} else {
									dbModel.setUrl(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setUrl(elt.getAttribute("url"));
							}
							nodeList = elt.getElementsByTagName("driver");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setDriverClass(child.getAttribute("value"));
								} else {
									dbModel.setDriverClass(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setDriverClass(elt.getAttribute("driver"));
							}
							nodeList = elt.getElementsByTagName("password");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setPassword(child.getAttribute("value"));
								} else {
									dbModel.setPassword(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setPassword(elt.getAttribute("password"));
							}
							nodeList = elt.getElementsByTagName("username");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setUsername(child.getAttribute("value"));
								} else {
									dbModel.setUsername(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setUsername(elt.getAttribute("username"));
							}
							nodeList = elt.getElementsByTagName("schema");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setSchema(child.getAttribute("value"));
								} else {
									dbModel.setSchema(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setSchema(elt.getAttribute("schema"));
							}
							nodeList = elt.getElementsByTagName("catalog");
							if (nodeList != null && nodeList.getLength() > 0) {
								child = (Element) nodeList.item(0);
								if (child.hasAttribute("value")) {
									dbModel.setCatalog(child.getAttribute("value"));
								} else {
									dbModel.setCatalog(StringUtil.trim(child.getTextContent()));
								}
							} else {
								dbModel.setCatalog(elt.getAttribute("catalog"));
							}
							if (name.equals(dbs[0])) {
								diversityModel.setReference(dbModel);
							} else {
								diversityModel.setTarget(dbModel);
							}
						}
						return diversityModel;
					}
				});
		return diversityModel;
	}

}
