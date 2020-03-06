package org.sagacity.tools.diversity.utils;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sagacity.tools.diversity.model.DiversityModel;
import org.sagacity.tools.diversity.model.TableColumnMeta;
import org.sagacity.tools.diversity.model.TableDiffModel;
import org.sagacity.tools.diversity.model.TableForeignModel;
import org.sagacity.tools.diversity.model.TableIndexModel;
import org.sagacity.tools.diversity.model.TableMeta;
import org.sagacity.tools.diversity.model.TablePkModel;

/**
 * 
 * @author zhongxuchen
 *
 */
public class CompareUtils {
	/**
	 * 定义日志
	 */
	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * @todo 两张表进行比较
	 * @param diversityModel
	 * @param tableDiffModel
	 */
	public static void compare(DiversityModel diversityModel, TableDiffModel tableDiffModel) {
		TableMeta sourceMeta = tableDiffModel.getReferenceTable();
		TableMeta targetMeta = tableDiffModel.getTargetTable();
		Boolean ignorePkName = diversityModel.isIgnorePkName();
		Boolean ignoreComment = diversityModel.isIgnorComment();
		Boolean ignoreIndexName = diversityModel.isIgnoreIndexName();
		Boolean ignoreForeignKeyName = diversityModel.isIgnoreForeignKeyName();
		// 列差异
		tableDiffModel.setColumnsDifference(
				compareColumns(diversityModel, ignoreComment, sourceMeta.getColMetas(), targetMeta.getColMetas()));
		// 外键差异
		tableDiffModel.setForeignKeyDifference(
				compareForeignKeys(ignoreForeignKeyName, sourceMeta.getForeignKeys(), targetMeta.getForeignKeys()));
		// 主键差异
		tableDiffModel.setPkDifference(comparePK(ignorePkName, sourceMeta.getPkModel(), targetMeta.getPkModel()));
		// 索引差异
		tableDiffModel
				.setIndexDifference(compareIndexes(ignoreIndexName, sourceMeta.getIndexes(), targetMeta.getIndexes()));
	}

	/**
	 * @todo 比较主键差异
	 * @param ignorePkName
	 * @param source
	 * @param target
	 * @return
	 */
	private static String comparePK(Boolean ignorePkName, TablePkModel source, TablePkModel target) {
		StringBuilder result = new StringBuilder();
		if (!ignorePkName) {
			if (!equals(source.getPkName(), target.getPkName())) {
				result.append("主键名=[" + source.getPkName() + "][" + target.getPkName() + "]不一致!");
			}
		}
		String tmp = compareArray(source.getPkColumns(), target.getPkColumns());
		if (tmp != null)
			result.append("主键列差异:").append(tmp);
		return result.toString();
	}

	/**
	 * @todo 比较外键信息
	 * @param ignoreForeignKeyName
	 * @param source
	 * @param target
	 * @return
	 */
	private static String compareForeignKeys(Boolean ignoreForeignKeyName, TableForeignModel source,
			TableForeignModel target) {
		StringBuilder result = new StringBuilder();
		if (!ignoreForeignKeyName) {
			if (!equals(source.getFkName(), target.getFkName())) {
				result.append("FK Name=[" + source.getFkName() + "][" + target.getFkName() + "]不一致!");
			}
		}
		String tmp = compareArray(source.getFkColumns(), target.getFkColumns());
		if (tmp != null)
			result.append("外键列差异:").append(tmp);
		tmp = compareArray(source.getPkColumns(), target.getPkColumns());
		if (tmp != null)
			result.append("外键对应主键列差异:").append(tmp);
		return result.toString();
	}

	/**
	 * @todo 表字段列信息比较
	 * @param ignoreComment
	 * @param source
	 * @param target
	 * @return
	 */
	private static String compareColumns(DiversityModel diversityModel, Boolean ignoreComment,
			List<TableColumnMeta> source, List<TableColumnMeta> target) {
		int sourceSize = (source == null) ? 0 : source.size();
		int targetSize = (target == null) ? 0 : target.size();
		String referenceDB = diversityModel.getReference().getName();
		String targetDB = diversityModel.getTarget().getName();
		if (sourceSize == 0) {
			return "db:" + referenceDB + " 中的表无字段!";
		}
		if (targetSize == 0) {
			return "db:" + targetDB + " 中的表无字段!";
		}
		StringBuilder result = new StringBuilder();
		for (TableColumnMeta var : source) {
			boolean has = false;
			for (TableColumnMeta var1 : target) {
				if (var.getColName().equalsIgnoreCase(var1.getColName())) {
					has = true;
					StringBuilder msg = new StringBuilder();
					if (!equals(var.getTypeName(), var1.getTypeName()))
						msg.append(" 数据类型[" + var.getTypeName() + "][" + var1.getTypeName() + "]不一致!");
					if (var.getLength() != var1.getLength() || var.getScale() != var1.getScale())
						msg.append(" 类型长度[" + var.getLength() + "," + var.getScale() + "][" + var1.getLength() + ","
								+ var1.getScale() + "]或精度不一致!");
					if (!equals(var.getColDefault(), var1.getColDefault()))
						msg.append(" 默认值[" + var.getColDefault() + "][" + var1.getColDefault() + "]不一样!");
					if (!equals(var.isAutoIncrement(), var1.isAutoIncrement()))
						msg.append(
								" 主键autoIncrement=[" + var.isAutoIncrement() + "][" + var1.isAutoIncrement() + "]不一致!");
					if (!equals(var.isNullable(), var1.isNullable()))
						msg.append(" nullable=[" + var.isNullable() + "][" + var1.isNullable() + "]不一致!");
					if (!ignoreComment && !equals(var.getColRemark(), var1.getColRemark()))
						msg.append(" 备注不一致!");
					if (msg.length() > 0)
						result.append("<br>字段:").append(var.getColName()).append(msg).append("</br>");
				}
			}
			if (!has) {
				result.append("<br>字段:[" + var.getColName() + "]在db:[" + targetDB + "] 中不存在!</br>");
			}
		}

		// 判断targetDB表字段是否在referenceDB表中
		for (TableColumnMeta var : target) {
			boolean has = false;
			for (TableColumnMeta var1 : source) {
				if (var.getColName().equalsIgnoreCase(var1.getColName())) {
					has = true;
					break;
				}
			}
			if (!has) {
				result.append("<br>字段:[" + var.getColName() + "]在db:[" + referenceDB + "]中不存在!</br>");
			}
		}
		String tmp = result.toString();
		if (tmp.equals(""))
			return null;
		return "<br>列数量分别为:[" + sourceSize + "][" + targetSize + "]</br>".concat(tmp);
	}

	/**
	 * @todo 对比索引信息
	 * @param ignoreIndexName
	 * @param source
	 * @param target
	 * @return
	 */
	private static String compareIndexes(Boolean ignoreIndexName, List<TableIndexModel> source,
			List<TableIndexModel> target) {
		int sourceSize = (source == null) ? 0 : source.size();
		int targetSize = (target == null) ? 0 : target.size();
		if (sourceSize != targetSize)
			return "索引不一致,数量分别为:[" + sourceSize + "][" + targetSize + "]";
		if (sourceSize == 0)
			return null;
		StringBuilder result = new StringBuilder();
		int count = 0;
		for (TableIndexModel var1 : source) {
			for (TableIndexModel var2 : target) {
				if (var1.getIndexName().equalsIgnoreCase(var2.getIndexName())) {
					count++;
					String tmp = compareArray(var1.getColumnNames(), var2.getColumnNames());
					if (tmp != null) {
						result.append("索引列不一致:").append(tmp);
					}
					if (!equals(var1.getIndexType(), var2.getIndexType()))
						result.append("索引类型不一致!");
					if (!equals(var1.getOrderType(), var2.getOrderType()))
						result.append("索引排序方式不一致!");
				}
			}
		}
		if (count != sourceSize)
			result.append("索引设置不匹配!");
		return result.toString();
	}

	private static String compareArray(String[] source, String[] target) {
		int sourceSize = (source == null) ? 0 : source.length;
		int targetSize = (target == null) ? 0 : target.length;
		if (sourceSize != targetSize)
			return "列数量分别为:[" + sourceSize + "][" + targetSize + "]";
		if (sourceSize == 0)
			return null;
		int count = 0;
		for (String name : source) {
			for (String var : target) {
				if (name.equalsIgnoreCase(var)) {
					count++;
					break;
				}
			}
		}
		// 列全部相同
		if (count == source.length) {
			return null;
		}
		return "列名称不相同!";
	}

	/**
	 * @todo <b>对象比较</b>
	 * @param target
	 * @param compared
	 * @return
	 */
	private static boolean equals(Object target, Object compared) {
		if (null == target) {
			return target == compared;
		}
		return target.equals(compared);
	}
}
