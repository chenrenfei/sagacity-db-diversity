package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-db-diversity
 * @description 数据库对比配置模型
 * @author zhong
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class DiversityModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6263058778285775410L;

	/**
	 * 忽略注释
	 */
	private boolean ignorComment = true;

	/**
	 * 忽视主键名称
	 */
	private boolean ignorePkName = true;

	/**
	 * 忽视外键名称
	 */
	private boolean ignoreForeignKeyName = true;

	/**
	 * 忽视外部关联名称
	 */
	private boolean ignoreExportKeyName = true;

	/**
	 * 忽视索引名称
	 */
	private boolean ignoreIndexName = true;

	/**
	 * 包含的表
	 */
	private String[] include;

	/**
	 * 排除的表
	 */
	private String[] exclude;

	/**
	 * 参照数据库
	 */
	private DataSourceModel reference;

	/**
	 * 目标对比数据库
	 */
	private DataSourceModel target;

	/**
	 * 只显示差异
	 */
	private Boolean onlyDiffer = true;

	/**
	 * 输出报告的模板
	 */
	private String reportTemplate = "classpath:org/sagacity/tools/diversity/report-template.ftl";

	/**
	 * 报告输出文件
	 */
	private String reportFile = "diversity-report.html";

	/**
	 * @return the refrence
	 */
	public DataSourceModel getReference() {
		return reference;
	}

	/**
	 * @param refrence the refrence to set
	 */
	public void setReference(DataSourceModel reference) {
		this.reference = reference;
	}

	/**
	 * @return the target
	 */
	public DataSourceModel getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(DataSourceModel target) {
		this.target = target;
	}

	/**
	 * @return the onlyDiffer
	 */
	public Boolean getOnlyDiffer() {
		return onlyDiffer;
	}

	/**
	 * @param onlyDiffer the onlyDiffer to set
	 */
	public void setOnlyDiffer(Boolean onlyDiffer) {
		this.onlyDiffer = onlyDiffer;
	}

	/**
	 * @return the reportTemplate
	 */
	public String getReportTemplate() {
		return reportTemplate;
	}

	/**
	 * @param reportTemplate the reportTemplate to set
	 */
	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	/**
	 * @return the reportFile
	 */
	public String getReportFile() {
		return reportFile;
	}

	/**
	 * @param reportFile the reportFile to set
	 */
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	/**
	 * @return the include
	 */
	public String[] getInclude() {
		return include;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(String[] include) {
		this.include = include;
	}

	/**
	 * @return the exclude
	 */
	public String[] getExclude() {
		return exclude;
	}

	/**
	 * @param exclude the exclude to set
	 */
	public void setExclude(String[] exclude) {
		this.exclude = exclude;
	}

	/**
	 * @return the ignorComment
	 */
	public boolean isIgnorComment() {
		return ignorComment;
	}

	/**
	 * @param ignorComment the ignorComment to set
	 */
	public void setIgnorComment(boolean ignorComment) {
		this.ignorComment = ignorComment;
	}

	/**
	 * @return the ignorePkName
	 */
	public boolean isIgnorePkName() {
		return ignorePkName;
	}

	/**
	 * @param ignorePkName the ignorePkName to set
	 */
	public void setIgnorePkName(boolean ignorePkName) {
		this.ignorePkName = ignorePkName;
	}

	/**
	 * @return the ignoreForeignKeyName
	 */
	public boolean isIgnoreForeignKeyName() {
		return ignoreForeignKeyName;
	}

	/**
	 * @param ignoreForeignKeyName the ignoreForeignKeyName to set
	 */
	public void setIgnoreForeignKeyName(boolean ignoreForeignKeyName) {
		this.ignoreForeignKeyName = ignoreForeignKeyName;
	}

	/**
	 * @return the ignoreExportKeyName
	 */
	public boolean isIgnoreExportKeyName() {
		return ignoreExportKeyName;
	}

	/**
	 * @param ignoreExportKeyName the ignoreExportKeyName to set
	 */
	public void setIgnoreExportKeyName(boolean ignoreExportKeyName) {
		this.ignoreExportKeyName = ignoreExportKeyName;
	}

	/**
	 * @return the ignoreIndexName
	 */
	public boolean isIgnoreIndexName() {
		return ignoreIndexName;
	}

	/**
	 * @param ignoreIndexName the ignoreIndexName to set
	 */
	public void setIgnoreIndexName(boolean ignoreIndexName) {
		this.ignoreIndexName = ignoreIndexName;
	}

}
