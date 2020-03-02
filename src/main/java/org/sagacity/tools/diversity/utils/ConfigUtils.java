/**
 * 
 */
package org.sagacity.tools.diversity.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.sagacity.tools.diversity.DiversityConstants;
import org.sagacity.tools.diversity.model.DataSourceModel;
import org.sagacity.tools.diversity.model.DiversityModel;
import org.sagacity.tools.diversity.utils.callback.XMLCallbackHandler;

/**
 * @author zhong
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
						String value;
						Element child;
						for (String name : dbs) {
							elt = (Element) root.getElementsByTagName(name).item(0);
							DataSourceModel dbModel = new DataSourceModel();
							dbModel.setName(elt.getAttribute("name"));
							if (StringUtil.isBlank(dbModel.getName())) {
								dbModel.setName(name);
							}
							if (elt.getElementsByTagName("url") != null) {
								child = (Element) elt.getElementsByTagName("url").item(0);
								value = child.getAttribute("value");
								dbModel.setUrl(value == null ? child.getNodeValue().trim() : value);
							} else {
								dbModel.setUrl(elt.getAttribute("url"));
							}
							if (elt.getElementsByTagName("driver") != null) {
								child = (Element) elt.getElementsByTagName("driver").item(0);
								value = child.getAttribute("value");
								dbModel.setDriverClass(value == null ? child.getNodeValue().trim() : value);
							} else {
								dbModel.setDriverClass(elt.getAttribute("driver"));
							}
							if (elt.getElementsByTagName("password") != null) {
								child = (Element) elt.getElementsByTagName("password").item(0);
								value = child.getAttribute("value");
								dbModel.setPassword(value == null ? child.getNodeValue().trim() : value);
							} else {
								dbModel.setPassword(elt.getAttribute("password"));
							}
							if (elt.getElementsByTagName("username") != null) {
								child = (Element) elt.getElementsByTagName("username").item(0);
								value = child.getAttribute("value");
								dbModel.setUsername(value == null ? child.getNodeValue().trim() : value);
							} else {
								dbModel.setUsername(elt.getAttribute("username"));
							}
							if (elt.getElementsByTagName("schema") != null) {
								child = (Element) elt.getElementsByTagName("schema").item(0);
								value = child.getAttribute("value");
								dbModel.setSchema(value == null ? child.getNodeValue().trim() : value);
							} else {
								dbModel.setSchema(elt.getAttribute("schema"));
							}
							if (elt.getElementsByTagName("catalog") != null) {
								child = (Element) elt.getElementsByTagName("catalog").item(0);
								value = child.getAttribute("value");
								dbModel.setCatalog(value == null ? child.getNodeValue().trim() : value);
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
