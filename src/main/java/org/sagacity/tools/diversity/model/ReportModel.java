package org.sagacity.tools.diversity.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * 报告模型
 * 
 * @author zhong
 *
 */
public class ReportModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3216536597830741877L;

	private String reportTime;

	private int referenceTableCnt;

	private int targetTableCnt;

	private String targetDB;

	private String referenceDB;

	/**
	 * 表的差异
	 */
	private Collection<TableDiffModel> tableDiffs;

	/**
	 * @return the tableDiffs
	 */
	public Collection<TableDiffModel> getTableDiffs() {
		return tableDiffs;
	}

	/**
	 * @param tableDiffs the tableDiffs to set
	 */
	public void setTableDiffs(Collection<TableDiffModel> tableDiffs) {
		this.tableDiffs = tableDiffs;
	}

	/**
	 * @return the reportTime
	 */
	public String getReportTime() {
		return reportTime;
	}

	/**
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	/**
	 * @return the referenceTableCnt
	 */
	public int getReferenceTableCnt() {
		return referenceTableCnt;
	}

	/**
	 * @param referenceTableCnt the referenceTableCnt to set
	 */
	public void setReferenceTableCnt(int referenceTableCnt) {
		this.referenceTableCnt = referenceTableCnt;
	}

	/**
	 * @return the targetTableCnt
	 */
	public int getTargetTableCnt() {
		return targetTableCnt;
	}

	/**
	 * @param targetTableCnt the targetTableCnt to set
	 */
	public void setTargetTableCnt(int targetTableCnt) {
		this.targetTableCnt = targetTableCnt;
	}

	public String getTargetDB() {
		return targetDB;
	}

	public void setTargetDB(String targetDB) {
		this.targetDB = targetDB;
	}

	public String getReferenceDB() {
		return referenceDB;
	}

	public void setReferenceDB(String referenceDB) {
		this.referenceDB = referenceDB;
	}

}
