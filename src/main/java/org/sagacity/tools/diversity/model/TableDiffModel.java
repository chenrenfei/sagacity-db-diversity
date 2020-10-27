package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-db-diversity
 * @description 表差异模型
 * @author zhong 
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class TableDiffModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2716155512101337124L;

	/**
	 * 表名
	 */
	private String tableName;

	private TableMeta referenceTable;
	private TableMeta targetTable;

	private boolean reference;

	private boolean target;

	/**
	 * 主键约束差异
	 */
	private String pkDifference;

	/**
	 * 列差异
	 */
	private String columnsDifference;

	/**
	 * 外键差异
	 */
	private String foreignKeyDifference;

	/**
	 * 索引差异
	 */
	private String indexDifference;

	/**
	 * 差异内容
	 */
	private String tableDifference;

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the refrence
	 */
	public boolean isReference() {
		return reference;
	}

	/**
	 * @param refrence
	 *            the refrence to set
	 */
	public void setReference(boolean reference) {
		this.reference = reference;
	}

	/**
	 * @return the target
	 */
	public boolean isTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(boolean target) {
		this.target = target;
	}

	/**
	 * @return the pkDifference
	 */
	public String getPkDifference() {
		return pkDifference;
	}

	/**
	 * @param pkDifference the pkDifference to set
	 */
	public void setPkDifference(String pkDifference) {
		this.pkDifference = pkDifference;
	}

	/**
	 * @return the columnsDifference
	 */
	public String getColumnsDifference() {
		return columnsDifference;
	}

	/**
	 * @param columnsDifference the columnsDifference to set
	 */
	public void setColumnsDifference(String columnsDifference) {
		this.columnsDifference = columnsDifference;
	}

	/**
	 * @return the foreignKeyDifference
	 */
	public String getForeignKeyDifference() {
		return foreignKeyDifference;
	}

	/**
	 * @param foreignKeyDifference the foreignKeyDifference to set
	 */
	public void setForeignKeyDifference(String foreignKeyDifference) {
		this.foreignKeyDifference = foreignKeyDifference;
	}

	/**
	 * @return the indexDifference
	 */
	public String getIndexDifference() {
		return indexDifference;
	}

	/**
	 * @param indexDifference the indexDifference to set
	 */
	public void setIndexDifference(String indexDifference) {
		this.indexDifference = indexDifference;
	}

	/**
	 * @return the tableDifference
	 */
	public String getTableDifference() {
		return tableDifference;
	}

	/**
	 * @param tableDifference the tableDifference to set
	 */
	public void setTableDifference(String tableDifference) {
		this.tableDifference = tableDifference;
	}

	/**
	 * @return the referenceTable
	 */
	public TableMeta getReferenceTable() {
		return referenceTable;
	}

	/**
	 * @param referenceTable
	 *            the referenceTable to set
	 */
	public void setReferenceTable(TableMeta referenceTable) {
		this.referenceTable = referenceTable;
	}

	/**
	 * @return the targetTable
	 */
	public TableMeta getTargetTable() {
		return targetTable;
	}

	/**
	 * @param targetTable
	 *            the targetTable to set
	 */
	public void setTargetTable(TableMeta targetTable) {
		this.targetTable = targetTable;
	}

}
