package org.sagacity.tools.diversity;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.sagacity.tools.diversity.model.DiversityModel;
import org.sagacity.tools.diversity.model.ReportModel;
import org.sagacity.tools.diversity.model.TableDiffModel;
import org.sagacity.tools.diversity.model.TableMeta;
import org.sagacity.tools.diversity.utils.ClassLoaderUtil;
import org.sagacity.tools.diversity.utils.CompareUtils;
import org.sagacity.tools.diversity.utils.ConfigUtils;
import org.sagacity.tools.diversity.utils.DBHelper;
import org.sagacity.tools.diversity.utils.DateUtil;
import org.sagacity.tools.diversity.utils.FileUtil;
import org.sagacity.tools.diversity.utils.IOUtil;
import org.sagacity.tools.diversity.utils.StringUtil;
import org.sagacity.tools.diversity.utils.TemplateUtils;

/**
 * @description 两个数据库比较工具,用于不同环境下数据库表、存储过程、函数等信息的比较,帮助项目在开发、UAT、生产等不同环境比较分析差异,快速定位问题保持环境一致性
 * @author zhongxuchen
 */
public class StartBooter {
	/**
	 * 定义日志
	 */
	private Logger logger = null;

	/**
	 * 初始化
	 */
	public void init() {
		try {
			String realLogFile = DiversityConstants.LOG_CONFIG;
			if (realLogFile.charAt(0) == '/')
				realLogFile = realLogFile.substring(1);
			URL url = Thread.currentThread().getContextClassLoader().getResource(realLogFile);
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(realLogFile);
			ConfigurationSource source = new ConfigurationSource(stream, url);
			Configurator.initialize(null, source);
			File driverPath = new File(DiversityConstants.BASE_DIR, DiversityConstants.DRIVER_PATH);
			if (!driverPath.exists()) {
				System.out.println("请正确配置jdbc驱动类,将驱动放于路径:" + driverPath.getAbsolutePath());
			} else {
				ClassLoaderUtil
						.loadJarFiles(FileUtil.getPathFiles(driverPath, new String[] { "[\\w|\\-|\\.]+\\.jar$" }));
			}
			logger = LogManager.getLogger(getClass());
			logger.info("完成环境初始化!");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("加载log4j配置和jdbc驱动类失败,请检查配置!");
		}
	}

	/**
	 * @todo 分析数据库差异
	 * @param diversityModel
	 * @return
	 */
	public ReportModel diversity(DiversityModel diversityModel) throws Exception {
		ReportModel reportModel = new ReportModel();
		String reference = diversityModel.getReference().getName();
		String target = diversityModel.getTarget().getName();
		DBHelper.registConnection(diversityModel);
		DBHelper.switchDB(reference);
		List<TableMeta> sourceTables = DBHelper.getTableAndView(diversityModel.getInclude(),
				diversityModel.getExclude());
		//排序
		Collections.sort(sourceTables, new Comparator<TableMeta>() {
			public int compare(TableMeta a, TableMeta b) {
				return a.getTableName().compareToIgnoreCase(b.getTableName());
			}
		});
		DBHelper.switchDB(target);
		List<TableMeta> targetTables = DBHelper.getTableAndView(diversityModel.getInclude(),
				diversityModel.getExclude());
		//排序
		Collections.sort(targetTables, new Comparator<TableMeta>() {
			public int compare(TableMeta a, TableMeta b) {
				return a.getTableName().compareToIgnoreCase(b.getTableName());
			}
		});
		// 将两个库的表合并到一个集合中
		LinkedHashMap<String, TableDiffModel> result = new LinkedHashMap<String, TableDiffModel>();
		String tableName;
		reportModel.setReferenceTableCnt(sourceTables.size());
		reportModel.setTargetTableCnt(targetTables.size());
		for (TableMeta table : sourceTables) {
			tableName = table.getTableName();
			TableDiffModel diffModel = new TableDiffModel();
			diffModel.setTableName(tableName);
			diffModel.setReference(true);
			diffModel.setReferenceTable(table);
			result.put(tableName.toLowerCase(), diffModel);
		}
		for (TableMeta table : targetTables) {
			tableName = table.getTableName();
			TableDiffModel diffModel = result.get(tableName.toLowerCase());
			if (diffModel == null) {
				diffModel = new TableDiffModel();
				diffModel.setTableName(tableName);
			}
			diffModel.setTarget(true);
			diffModel.setTargetTable(table);
			result.put(tableName.toLowerCase(), diffModel);
		}
		
		
		Iterator<TableDiffModel> iter = result.values().iterator();
		TableDiffModel diffModel;
		// 是否忽视注释的差异
		boolean ignoreComment = diversityModel.isIgnorComment();

		/**
		 * 开始进行比较
		 */
		while (iter.hasNext()) {
			diffModel = iter.next();
			tableName = diffModel.getTableName();
			TableMeta sourceMeta = diffModel.getReferenceTable();
			TableMeta targetMeta = diffModel.getTargetTable();
			// 两个库表都存在
			if (diffModel.isReference() && diffModel.isTarget()) {
				StringBuilder message = new StringBuilder();
				if (!ignoreComment) {
					if (!sourceMeta.getTableRemark().equals(targetMeta.getTableRemark()))
						message.append(
								"表注释:[" + sourceMeta.getTableRemark() + "][" + targetMeta.getTableRemark() + "]不同!");
				}
				DBHelper.switchDB(reference);
				sourceMeta.setColMetas(DBHelper.getTableColumnMeta(tableName));
				sourceMeta.setPkModel(DBHelper.getTablePKConstraint(tableName));
				sourceMeta.setForeignKeys(DBHelper.getTableImpForeignKeys(tableName));
				sourceMeta.setIndexes(DBHelper.getTableIndex(tableName));
				DBHelper.switchDB(target);
				targetMeta.setColMetas(DBHelper.getTableColumnMeta(tableName));
				targetMeta.setPkModel(DBHelper.getTablePKConstraint(tableName));
				targetMeta.setForeignKeys(DBHelper.getTableImpForeignKeys(tableName));
				targetMeta.setIndexes(DBHelper.getTableIndex(tableName));
				logger.info("正在比较表:{}", tableName);
				CompareUtils.compare(diversityModel, diffModel);
				if (StringUtil.isBlank(diffModel.getColumnsDifference())
						&& StringUtil.isBlank(diffModel.getForeignKeyDifference())
						&& StringUtil.isBlank(diffModel.getIndexDifference())
						&& StringUtil.isBlank(diffModel.getPkDifference())
						&& StringUtil.isBlank(diffModel.getTableDifference()))
					iter.remove();
			}
		}
		// 关闭数据库
		DBHelper.close();
		reportModel.setReportTime(DateUtil.format2China(DateUtil.getNowTime()));
		reportModel.setTableDiffs(result.values());
		return reportModel;
	}

	/**
	 * @todo 输出报告
	 * @param reportModel
	 * @param templateFile
	 * @param reportFile
	 * @throws Exception
	 */
	public void report(ReportModel reportModel, String templateFile, String reportFile) throws Exception {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("root", reportModel);
		String templateStr = IOUtil.inputStream2String(FileUtil.getResourceAsStream(templateFile),
				DiversityConstants.ENCODING);
		String result = TemplateUtils.getInstance().create(root, templateStr);
		String outFile;
		if (FileUtil.isRootPath(reportFile))
			outFile = reportFile;
		else
			outFile = FileUtil.linkPath(DiversityConstants.BASE_DIR, reportFile);
		logger.info("检测报告开始生成:{}", outFile);
		FileUtil.putStringToFile(result, outFile, DiversityConstants.ENCODING);
	}

	/**
	 * 执行总控制
	 */
	public void submit() {
		try {
			// 初始化加载log4j配置和jdbc驱动
			init();
			// 解析配置
			logger.info("解析配置文件!");
			DiversityModel diversityModel = ConfigUtils.parse();
			// 连接数据库进行分析
			logger.info("开始分析对比2个数据库查询!");
			ReportModel reportModel = diversity(diversityModel);
			// 产生结果报告
			logger.info("开始生成检测报表!");
			report(reportModel, diversityModel.getReportTemplate(), diversityModel.getReportFile());
			logger.info("完成生成检测报表!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		StartBooter diversity = new StartBooter();
		if (args != null && args.length > 0)
			DiversityConstants.CONFIG_FILE = args[0];
		if (args != null && args.length > 1)
			DiversityConstants.BASE_DIR = args[1];
		else
			DiversityConstants.BASE_DIR = System.getProperty("user.dir");
		// test
		if (args == null || args.length == 0) {
			DiversityConstants.BASE_DIR = "D:/personal/sagacity-db-diversity/src/test/resources";
			DiversityConstants.CONFIG_FILE = "db-diversity.xml";
		}
		diversity.submit();
	}
}
