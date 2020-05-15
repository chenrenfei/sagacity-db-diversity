package org.sagacity.tools.diversity.utils;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.sagacity.tools.diversity.DiversityConstants;

/**
 * 提供基于jdk自带的日志框架,大幅减小quickvo最终打包的jar大小
 * 
 * @author zhongxuchen
 * @version 4.11.9 Date:2020-05-15
 */
public class LoggerUtil {
	private static Logger logger = null;

	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("sagacity.quickvo");
			logger.setLevel(Level.ALL);
			try {
				Handler handler = new FileHandler(FileUtil.linkPath(DiversityConstants.BASE_DIR, "db-diversity.log"));
				handler.setFormatter(new SimpleFormatter());
				logger.addHandler(handler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return logger;
	}
}
