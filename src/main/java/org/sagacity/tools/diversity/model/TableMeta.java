/**
 * 
 */
package org.sagacity.tools.diversity.model;

import java.io.Serializable;
import java.util.List;

/**
 * @project sagacity-quickvo
 * @description 数据库表Meta信息
 * @author chenrenfei <a href="mailto:zhongxuchen@hotmail.com">联系作者</a>
 * @version id:TableMeta.java,Revision:v1.0,Date:2009-2-24 
 */
public class TableMeta implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2472018681037095103L;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 别名
	 */
	private String tableAlias;

	/**
	 * 表名注释
	 */
	private String tableRemark;

	/**
	 * 数据库schema
	 */
	private String schema;

	/**
	 * 是视图还是table
	 */
	private String tableType;

	/**
	 * 字段信息
	 */
	private List<TableColumnMeta> colMetas;

	/**
	 * 外键
	 */
	private TableForeignModel foreignKeys;

	/**
	 * 表的索引信息
	 */
	private List<TableIndexModel> indexes;

	/**
	 * 主键约束
	 */
	private TablePkModel pkModel;

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public List<TableColumnMeta> getColMetas() {
		return colMetas;
	}

	public void setColMetas(List<TableColumnMeta> colMetas) {
		this.colMetas = colMetas;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableRemark() {
		if (tableRemark == null)
			return "";
		return tableRemark;
	}

	public void setTableRemark(String tableRemark) {
		this.tableRemark = tableRemark;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the pkModel
	 */
	public TablePkModel getPkModel() {
		return pkModel;
	}

	/**
	 * @param pkModel
	 *            the pkModel to set
	 */
	public void setPkModel(TablePkModel pkModel) {
		this.pkModel = pkModel;
	}

	/**
	 * @return the foreignKeys
	 */
	public TableForeignModel getForeignKeys() {
		return foreignKeys;
	}

	/**
	 * @param foreignKeys
	 *            the foreignKeys to set
	 */
	public void setForeignKeys(TableForeignModel foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	/**
	 * @return the indexes
	 */
	public List<TableIndexModel> getIndexes() {
		return indexes;
	}

	/**
	 * @param indexes
	 *            the indexes to set
	 */
	public void setIndexes(List<TableIndexModel> indexes) {
		this.indexes = indexes;
	}

}
