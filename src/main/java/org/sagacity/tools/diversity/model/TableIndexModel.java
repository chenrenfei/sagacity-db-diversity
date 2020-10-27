package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-db-diversity
 * @description 数据库索引模型
 * @author zhongxuchen
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class TableIndexModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2281115359486280776L;

	/**
	 * 索引名称
	 */
	private String indexName;

	/**
	 * 索引类型
	 */
	private String indexType;

	/**
	 * 索引列
	 */
	private String[] columnNames;

	private String orderType;

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName
	 *            the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return the indexType
	 */
	public String getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}
