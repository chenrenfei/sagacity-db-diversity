package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-db-diversity
 * @description 数据库函数模型
 * @author zhong
 * @version v1.0, Date:2020-10-27
 * @modify 2020-10-27,修改说明
 */
public class FunctionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1830320551768489440L;

	private String functionName;

	private String[] columns;

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}

}
