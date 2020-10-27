package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-db-diversity
 * @description 主键模型
 * @author zhong 
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class TablePkModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6006356541366377136L;

	/**
	 * 主键名称
	 */
	private String pkName;

	/**
	 * 主键列
	 */
	private String[] pkColumns;

	/**
	 * @return the pkName
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * @param pkName
	 *            the pkName to set
	 */
	public void setPkName(String pkName) {
		this.pkName = pkName;
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

}
