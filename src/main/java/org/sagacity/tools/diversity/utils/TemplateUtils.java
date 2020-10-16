/**
 * 
 */
package org.sagacity.tools.diversity.utils;

import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @project sagacity-core
 * @description 基于freemarker的模版工具引擎，提供日常项目中模版和数据对象的结合处理
 * @author zhongxuchen <a href="mailto:zhongxuchen@hotmail.com">联系作者</a>
 * @version id:TemplateUtils.java,Revision:v1.0,Date:2008-11-24 下午11:07:07
 */
@SuppressWarnings({ "rawtypes" })
public class TemplateUtils {
	/**
	 * 定义全局日志
	 */
	private final static Logger logger = LoggerUtil.getLogger();
	private static Configuration cfg = null;

	public static TemplateUtils me = new TemplateUtils();

	public static TemplateUtils getInstance() {
		return me;
	}

	/**
	 * 编码格式，默认utf-8
	 */
	private String encoding = "UTF-8";

	/**
	 * 设置编码格式
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String create(Map root, String templateStr) {
		if (root == null) {
			return null;
		}
		String result = null;
		StringWriter writer = null;
		try {
			init();
			StringTemplateLoader templateLoader = new StringTemplateLoader();
			templateLoader.putTemplate("string_template", templateStr);
			cfg.setTemplateLoader(templateLoader);
			Template template = null;
			if (StringUtil.isNotBlank(this.encoding)) {
				template = cfg.getTemplate("string_template", this.encoding);
			} else {
				template = cfg.getTemplate("string_template");
			}
			writer = new StringWriter();
			template.process(root, writer);
			writer.flush();
			result = writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} finally {
			IOUtil.closeQuietly(writer);
		}
		return result;
	}

	/**
	 * 销毁实例
	 */
	public static void destory() {
		cfg = null;
	}

	public void init() {
		if (cfg == null) {
			cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			if (StringUtil.isNotBlank(this.encoding)) {
				cfg.setDefaultEncoding(this.encoding);
			}
		}
	}
}
