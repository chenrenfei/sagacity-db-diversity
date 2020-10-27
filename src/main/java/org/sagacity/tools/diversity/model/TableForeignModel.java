package org.sagacity.tools.diversity.model;

/**
 * @project sagacity-db-diversity
 * @description 外键模型
 * @author zhong 
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class TableForeignModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2219062607139562617L;

	private String pkTable;

	/**
	 * 外键对应表
	 */
	private String fkTable;

	/**
	 * 外键字段
	 */
	private String[] fkColumns;

	private String[] pkColumns;

	private int updateRule;

	private int deleteRule;

	/**
	 * 外键名称
	 */
	private String fkName;

	/**
	 * @return the fkName
	 */
	public String getFkName() {
		return fkName;
	}

	/**
	 * @param fkName
	 *            the fkName to set
	 */
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	/**
	 * @return the pkTable
	 */
	public String getPkTable() {
		return pkTable;
	}

	/**
	 * @param pkTable
	 *            the pkTable to set
	 */
	public void setPkTable(String pkTable) {
		this.pkTable = pkTable;
	}

	/**
	 * @return the fkColumns
	 */
	public String[] getFkColumns() {
		return fkColumns;
	}

	/**
	 * @param fkColumns
	 *            the fkColumns to set
	 */
	public void setFkColumns(String[] fkColumns) {
		this.fkColumns = fkColumns;
	}

	/**
	 * @return the pkColumns
	 */
	public String[] getPkColumns() {
		return pkColumns;
	}

	/**
	 * @param pkColumns
	 *            the pkColumns to set
	 */
	public void setPkColumns(String[] pkColumns) {
		this.pkColumns = pkColumns;
	}

	/**
	 * @return the updateRule
	 */
	public int getUpdateRule() {
		return updateRule;
	}

	/**
	 * @param updateRule
	 *            the updateRule to set
	 */
	public void setUpdateRule(int updateRule) {
		this.updateRule = updateRule;
	}

	/**
	 * @return the deleteRule
	 */
	public int getDeleteRule() {
		return deleteRule;
	}

	/**
	 * @param deleteRule
	 *            the deleteRule to set
	 */
	public void setDeleteRule(int deleteRule) {
		this.deleteRule = deleteRule;
	}

	/**
	 * @return the fkTable
	 */
	public String getFkTable() {
		return fkTable;
	}

	/**
	 * @param fkTable the fkTable to set
	 */
	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

}
