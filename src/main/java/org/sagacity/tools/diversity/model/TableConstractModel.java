/**
 * 
 */
package org.sagacity.tools.diversity.model;

import java.io.Serializable;

/**
 * @project sagacity-quickvo
 * @description 表关联的数据模型
 * @author chenrenfei $<a href="mailto:zhongxuchen@hotmail.com">联系作者</a>$
 * @version $id:TableConstractModel.java,Revision:v1.0,Date:Apr 19, 2009 1:46:52
 *          AM $
 */
public class TableConstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5297814770562062800L;

	/**
	 * 关联主键表名称
	 */
	private String fkRefTableName;

	private String pkRefTableName;

	private String pkRefColName;

	/**
	 * 关联表主键列名称
	 */
	private String pkColName;

	/**
	 * 外键列名
	 */
	private String fkColName;
	
	/**
	 * 外键名称
	 */
	private String fkName;

	/**
	 * 主表的主键值跟子表的外键值对比字符串
	 */
	private String pkEqualsFkStr;

	/**
	 * @return the pkEqualsFkStr
	 */
	public String getPkEqualsFkStr() {
		return pkEqualsFkStr;
	}

	/**
	 * @param pkEqualsFkStr
	 *            the pkEqualsFkStr to set
	 */
	public void setPkEqualsFkStr(String pkEqualsFkStr) {
		this.pkEqualsFkStr = pkEqualsFkStr;
	}

	/**
	 * 主键修改对外键的影响 importedKeyNoAction (3)importedKeyCascade (0)importedKeySetNull
	 * (2) importedKeySetDefault (4)importedKeyRestrict(1)
	 */
	private int updateRule;

	/**
	 * 主键记录删除对外键的影响 importedKeyNoAction (3)importedKeyCascade (0) importedKeySetNull
	 * (2)importedKeySetDefault (4)importedKeyRestrict (1)
	 */
	private int deleteRule;

	/**
	 * 级联删除
	 */
	private int cascade = 0;

	/**
	 * 自动加载
	 */
	private String load;

	/**
	 * 自动保存
	 */
	private int autoSave = 1;

	/**
	 * 修改自动先删除子表
	 */
	private String updateSql;

	/**
	 * @return the fkRefTableName
	 */
	public String getFkRefTableName() {
		return fkRefTableName;
	}

	/**
	 * @param fkRefTableName
	 *            the fkRefTableName to set
	 */
	public void setFkRefTableName(String fkRefTableName) {
		this.fkRefTableName = fkRefTableName;
	}

	/**
	 * @return the pkColName
	 */
	public String getPkColName() {
		return pkColName;
	}

	/**
	 * @param pkColName
	 *            the pkColName to set
	 */
	public void setPkColName(String pkColName) {
		this.pkColName = pkColName;
	}

	/**
	 * @return the fkColName
	 */
	public String getFkColName() {
		return fkColName;
	}

	/**
	 * @param fkColName
	 *            the fkColName to set
	 */
	public void setFkColName(String fkColName) {
		this.fkColName = fkColName;
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
	 * @return the pkRefTableName
	 */
	public String getPkRefTableName() {
		return pkRefTableName;
	}

	/**
	 * @param pkRefTableName
	 *            the pkRefTableName to set
	 */
	public void setPkRefTableName(String pkRefTableName) {
		this.pkRefTableName = pkRefTableName;
	}

	/**
	 * @return the pkRefColName
	 */
	public String getPkRefColName() {
		return pkRefColName;
	}

	/**
	 * @param pkRefColName
	 *            the pkRefColName to set
	 */
	public void setPkRefColName(String pkRefColName) {
		this.pkRefColName = pkRefColName;
	}

	/**
	 * @return the load
	 */
	public String getLoad() {
		return load;
	}

	/**
	 * @param load
	 *            the load to set
	 */
	public void setLoad(String load) {
		this.load = load;
	}

	/**
	 * @return the autoSave
	 */
	public int getAutoSave() {
		return autoSave;
	}

	/**
	 * @param autoSave
	 *            the autoSave to set
	 */
	public void setAutoSave(int autoSave) {
		this.autoSave = autoSave;
	}

	/**
	 * @return the cascade
	 */
	public int getCascade() {
		return cascade;
	}

	/**
	 * @param cascade
	 *            the cascade to set
	 */
	public void setCascade(int cascade) {
		this.cascade = cascade;
	}

	/**
	 * @return the updateSql
	 */
	public String getUpdateSql() {
		return updateSql;
	}

	/**
	 * @param updateSql
	 *            the updateSql to set
	 */
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	/**
	 * @return the fkName
	 */
	public String getFkName() {
		return fkName;
	}

	/**
	 * @param fkName the fkName to set
	 */
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	
	
}
