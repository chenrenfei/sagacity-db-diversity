package org.sagacity.tools.diversity.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sagacity.tools.diversity.model.DataSourceModel;
import org.sagacity.tools.diversity.model.DiversityModel;
import org.sagacity.tools.diversity.model.FunctionModel;
import org.sagacity.tools.diversity.model.ProduceColumn;
import org.sagacity.tools.diversity.model.ProduceModel;
import org.sagacity.tools.diversity.model.TableColumnMeta;
import org.sagacity.tools.diversity.model.TableForeignModel;
import org.sagacity.tools.diversity.model.TableIndexModel;
import org.sagacity.tools.diversity.model.TableMeta;
import org.sagacity.tools.diversity.model.TablePkModel;
import org.sagacity.tools.diversity.utils.DBUtil.DbType;
import org.sagacity.tools.diversity.utils.callback.PreparedStatementResultHandler;

/**
 * 数据库工具
 * 
 * @author zhong
 *
 */
public class DBHelper {
	/**
	 * 定义全局日志
	 */
	private final static Logger logger = LoggerUtil.getLogger();

	/**
	 * 数据库连接
	 */
	private static Map<String, Connection> connMap = new HashMap<String, Connection>();

	private static Map<String, DataSourceModel> dbConfigMap = new HashMap<String, DataSourceModel>();

	private static DataSourceModel dbConfig = null;

	private static Connection conn;

	/**
	 * @todo 获取数据库连接
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	public static boolean registConnection(DiversityModel diversityModel) throws Exception {
		try {
			DataSourceModel[] dbConfigs = { diversityModel.getReference(), diversityModel.getTarget() };
			for (DataSourceModel dbConfig : dbConfigs) {
				Class.forName(dbConfig.getDriverClass());
				Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(),
						dbConfig.getPassword());
				connMap.put(dbConfig.getName(), conn);
				dbConfigMap.put(dbConfig.getName(), dbConfig);
			}
			return true;
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			logger.info("数据库驱动未能加载，请在/libs 目录下放入正确的数据库驱动jar包!");
			throw cnfe;
		} catch (SQLException se) {
			logger.info("获取数据库连接失败!");
			throw se;
		}
	}

	public static void switchDB(String dbName) {
		dbConfig = dbConfigMap.get(dbName);
		conn = connMap.get(dbName);
	}

	/**
	 * @todo 关闭数据库并销毁
	 */
	public static void close() {
		try {
			Iterator<Connection> iter = connMap.values().iterator();
			while (iter.hasNext()) {
				Connection conn = iter.next();
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @todo 获取符合条件的表和视图
	 * @param includes
	 * @param excludes
	 * @return
	 * @throws Exception
	 */
	public static List<TableMeta> getTableAndView(final String[] includes, final String[] excludes) throws Exception {
		int dbType = DBUtil.getDbType(conn);
		String[] types = new String[] { "TABLE" };
		PreparedStatement pst = null;
		ResultSet rs = null;
		// 数据库表注释，默认为remarks，不同数据库其名称不一样
		String commentName = "REMARKS";
		// oracle数据库
		if (dbType == DbType.ORACLE || dbType == DbType.ORACLE12) {
			pst = conn.prepareStatement("select * from user_tab_comments");
			rs = pst.executeQuery();
			commentName = "COMMENTS";
		} // mysql数据库
		else if (dbType == DbType.MYSQL || dbType == DbType.MYSQL8) {
			StringBuilder queryStr = new StringBuilder("SELECT TABLE_NAME,TABLE_SCHEMA,TABLE_TYPE,TABLE_COMMENT ");
			queryStr.append(" FROM INFORMATION_SCHEMA.TABLES where 1=1 ");
			if (dbConfig.getSchema() != null)
				queryStr.append(" and TABLE_SCHEMA='").append(dbConfig.getSchema()).append("'");
			else if (dbConfig.getCatalog() != null)
				queryStr.append(" and TABLE_SCHEMA='").append(dbConfig.getCatalog()).append("'");
			if (types != null) {
				queryStr.append(" and (");
				for (int i = 0; i < types.length; i++) {
					if (i > 0)
						queryStr.append(" or ");
					queryStr.append(" TABLE_TYPE like '%").append(types[i]).append("'");
				}
				queryStr.append(")");
			}
			pst = conn.prepareStatement(queryStr.toString());
			rs = pst.executeQuery();
			commentName = "TABLE_COMMENT";
		} else {
			rs = conn.getMetaData().getTables(dbConfig.getCatalog(), dbConfig.getSchema(), null, types);
		}
		return (List<TableMeta>) DBUtil.preparedStatementProcess(commentName, pst, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws Exception {
						List<TableMeta> tables = new ArrayList<TableMeta>();
						String tableName;
						// 是否包含标识，通过正则表达是判断是否是需要获取的表
						boolean is_include = false;
						while (rs.next()) {
							is_include = false;
							tableName = rs.getString("TABLE_NAME");
							if (includes != null && includes.length > 0) {
								for (int i = 0; i < includes.length; i++) {
									if (StringUtil.matches(tableName, includes[i])) {
										is_include = true;
										break;
									}
								}
							} else
								is_include = true;
							if (excludes != null && excludes.length > 0) {
								for (int j = 0; j < excludes.length; j++) {
									if (StringUtil.matches(tableName, excludes[j])) {
										is_include = false;
										break;
									}
								}
							}
							if (is_include) {
								TableMeta tableMeta = new TableMeta();
								tableMeta.setTableName(tableName);
								tableMeta.setSchema(dbConfig.getSchema());
								// tableMeta.setSchema(rs.getString("TABLE_SCHEMA"));
								tableMeta.setTableType(rs.getString("TABLE_TYPE"));
								tableMeta.setTableRemark(rs.getString(obj.toString()));
								tables.add(tableMeta);
							}
						}
						this.setResult(tables);
					}
				});
	}

	/**
	 * @todo 获取表名的注释
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private static String getTableRemark(String tableName) throws Exception {
		final int dbType = DBUtil.getDbType(conn);
		PreparedStatement pst = null;
		ResultSet rs;
		// sybase or sqlserver
		String tableComment = null;
		if (dbType == DbType.SQLSERVER || dbType == DbType.SQLSERVER2012 || dbType == DbType.SQLSERVER2014
				|| dbType == DbType.SQLSERVER2016) {
			StringBuilder queryStr = new StringBuilder();
			queryStr.append("select cast(isnull(f.value,'') as varchar(1000)) COMMENTS");
			queryStr.append(" from syscolumns a");
			queryStr.append(" inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties'");
			queryStr.append(" left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0");
			queryStr.append(" where a.colorder=1 and d.name=?");
			pst = conn.prepareStatement(queryStr.toString());
			pst.setString(1, tableName);
			rs = pst.executeQuery();
			tableComment = (String) DBUtil.preparedStatementProcess(null, pst, rs,
					new PreparedStatementResultHandler() {
						public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
							while (rs.next()) {
								this.setResult(rs.getString("COMMENTS"));
							}
						}
					});
		}
		return tableComment;
	}

	/**
	 * @todo 获取表的字段信息
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static List<TableColumnMeta> getTableColumnMeta(String tableName) throws Exception {
		final int dbType = DBUtil.getDbType(conn);
		PreparedStatement pst = null;
		ResultSet rs;
		HashMap filedsComments = null;
		// sybase or sqlserver
		if (dbType == DbType.SQLSERVER || dbType == DbType.SQLSERVER2012 || dbType == DbType.SQLSERVER2014
				|| dbType == DbType.SQLSERVER2016 || dbType == DbType.SYBASE) {
			if (dbType == DbType.SQLSERVER || dbType == DbType.SQLSERVER2012 || dbType == DbType.SQLSERVER2014
					|| dbType == DbType.SQLSERVER2016) {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("SELECT a.name COLUMN_NAME,");
				queryStr.append(" cast(isnull(g.[value],'') as varchar(1000)) as COMMENTS");
				queryStr.append(" FROM syscolumns a");
				queryStr.append(" inner join sysobjects d on a.id=d.id ");
				queryStr.append(" and d.xtype='U' and d.name<>'dtproperties'");
				queryStr.append(" left join syscomments e");
				queryStr.append(" on a.cdefault=e.id");
				queryStr.append(" left join sys.extended_properties g");
				queryStr.append(" on a.id=g.major_id AND a.colid = g.minor_id");
				queryStr.append(" where d.name=?");
				queryStr.append(" order by a.id,a.colorder");
				pst = conn.prepareStatement(queryStr.toString());
				pst.setString(1, tableName);
				rs = pst.executeQuery();
				filedsComments = (HashMap) DBUtil.preparedStatementProcess(null, pst, rs,
						new PreparedStatementResultHandler() {
							public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
								HashMap filedHash = new HashMap();
								while (rs.next()) {
									TableColumnMeta colMeta = new TableColumnMeta();
									colMeta.setColName(rs.getString("COLUMN_NAME"));
									colMeta.setColRemark(rs.getString("COMMENTS"));
									filedHash.put(rs.getString("COLUMN_NAME"), colMeta);
								}
								this.setResult(filedHash);
							}
						});
			}
			String queryStr = "{call sp_columns ('" + tableName + "')}";
			pst = conn.prepareCall(queryStr);
			rs = pst.executeQuery();
			final HashMap metaMap = filedsComments;
			return (List<TableColumnMeta>) DBUtil.preparedStatementProcess(null, null, rs,
					new PreparedStatementResultHandler() {
						public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
							List<TableColumnMeta> result = new ArrayList<TableColumnMeta>();
							String isAutoIncrement;
							while (rs.next()) {
								TableColumnMeta colMeta;
								if (dbType == DbType.SQLSERVER || dbType == DbType.SQLSERVER2012
										|| dbType == DbType.SQLSERVER2014 || dbType == DbType.SQLSERVER2016) {
									if (metaMap == null) {
										colMeta = new TableColumnMeta();
										colMeta.setColName(rs.getString("COLUMN_NAME"));
										colMeta.setColRemark(rs.getString("REMARKS"));
									} else
										colMeta = (TableColumnMeta) metaMap.get(rs.getString("COLUMN_NAME"));
								} else
									colMeta = new TableColumnMeta();
								colMeta.setColDefault(clearDefaultValue(StringUtil.trim(rs.getString("column_def"))));
								colMeta.setDataType(rs.getInt("data_type"));
								colMeta.setTypeName(rs.getString("type_name"));
								if (rs.getInt("char_octet_length") != 0)
									colMeta.setLength(rs.getInt("char_octet_length"));
								else
									colMeta.setLength(rs.getInt("precision"));
								colMeta.setPrecision(colMeta.getLength());
								// 字段名称
								colMeta.setColName(rs.getString("column_name"));
								colMeta.setScale(rs.getInt("scale"));
								colMeta.setNumPrecRadix(rs.getInt("radix"));
								try {
									isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
									if (isAutoIncrement != null && (isAutoIncrement.equalsIgnoreCase("true")
											|| isAutoIncrement.equalsIgnoreCase("YES")
											|| isAutoIncrement.equalsIgnoreCase("Y") || isAutoIncrement.equals("1"))) {
										colMeta.setAutoIncrement(true);
									} else
										colMeta.setAutoIncrement(false);
								} catch (Exception e) {
								}
								if (colMeta.getTypeName().toLowerCase().indexOf("identity") != -1)
									colMeta.setAutoIncrement(true);
								// 是否可以为空
								if (rs.getInt("nullable") == 1)
									colMeta.setNullable(true);
								else
									colMeta.setNullable(false);
								result.add(colMeta);
							}
							this.setResult(result);
						}
					});
		}

		// oracle 数据库
		if (dbType == DbType.ORACLE || dbType == DbType.ORACLE12) {
			StringBuilder queryStr = new StringBuilder();
			queryStr.append("SELECT t1.*,t2.DATA_DEFAULT FROM (SELECT COLUMN_NAME,COMMENTS");
			queryStr.append("  FROM user_col_comments");
			queryStr.append("  WHERE table_name =?) t1");
			queryStr.append("  LEFT JOIN(SELECT COLUMN_NAME,DATA_DEFAULT");
			queryStr.append("            FROM user_tab_cols");
			queryStr.append("            WHERE table_name =?) t2");
			queryStr.append("  on t1.COLUMN_NAME=t2.COLUMN_NAME");
			pst = conn.prepareStatement(queryStr.toString());
			pst.setString(1, tableName);
			pst.setString(2, tableName);
			rs = pst.executeQuery();
			filedsComments = (HashMap) DBUtil.preparedStatementProcess(null, pst, rs,
					new PreparedStatementResultHandler() {
						public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
							HashMap filedHash = new HashMap();
							while (rs.next()) {
								TableColumnMeta colMeta = new TableColumnMeta();
								colMeta.setColName(rs.getString("COLUMN_NAME"));
								colMeta.setColRemark(rs.getString("COMMENTS"));
								colMeta.setColDefault(StringUtil.trim(rs.getString("DATA_DEFAULT")));
								filedHash.put(rs.getString("COLUMN_NAME"), colMeta);
							}
							this.setResult(filedHash);
						}
					});
		}

		final HashMap metaMap = filedsComments;
		if (dbType == DbType.MYSQL || dbType == DbType.MYSQL8)
			rs = conn.getMetaData().getColumns(dbConfig.getCatalog(), dbConfig.getSchema(), tableName, "%");
		else
			rs = conn.getMetaData().getColumns(dbConfig.getCatalog(), dbConfig.getSchema(), tableName, null);
		return (List<TableColumnMeta>) DBUtil.preparedStatementProcess(metaMap, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						List<TableColumnMeta> result = new ArrayList<TableColumnMeta>();
						String isAutoIncrement;
						while (rs.next()) {
							TableColumnMeta colMeta;
							if (metaMap == null) {
								colMeta = new TableColumnMeta();
								colMeta.setColName(rs.getString("COLUMN_NAME"));
								colMeta.setColDefault(clearDefaultValue(StringUtil.trim(rs.getString("COLUMN_DEF"))));
								colMeta.setColRemark(rs.getString("REMARKS"));
							} else
								colMeta = (TableColumnMeta) metaMap.get(rs.getString("COLUMN_NAME"));
							if (colMeta != null) {
								colMeta.setDataType(rs.getInt("DATA_TYPE"));
								colMeta.setTypeName(rs.getString("TYPE_NAME"));
								colMeta.setLength(rs.getInt("COLUMN_SIZE"));
								colMeta.setPrecision(colMeta.getLength());
								colMeta.setScale(rs.getInt("DECIMAL_DIGITS"));
								colMeta.setNumPrecRadix(rs.getInt("NUM_PREC_RADIX"));
								try {
									isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
									if (isAutoIncrement != null && (isAutoIncrement.equalsIgnoreCase("true")
											|| isAutoIncrement.equalsIgnoreCase("YES")
											|| isAutoIncrement.equalsIgnoreCase("Y") || isAutoIncrement.equals("1"))) {
										colMeta.setAutoIncrement(true);
									} else
										colMeta.setAutoIncrement(false);
								} catch (Exception e) {
								}
								if (dbType == DbType.ORACLE12) {
									if (colMeta.getColDefault() != null
											&& colMeta.getColDefault().toLowerCase().endsWith(".nextval")) {
										colMeta.setAutoIncrement(true);
										colMeta.setColDefault(colMeta.getColDefault().replaceAll("\"", "\\\\\""));
									}
								}
								if (rs.getInt("NULLABLE") == 1)
									colMeta.setNullable(true);
								else
									colMeta.setNullable(false);
								result.add(colMeta);
							}
						}
						this.setResult(result);
					}

				});
	}

	/**
	 * @todo 处理sqlserver default值为((value))问题
	 * @param defaultValue
	 * @return
	 */
	private static String clearDefaultValue(String defaultValue) {
		if (defaultValue == null)
			return null;
		// 针对postgresql
		if (defaultValue.indexOf("(") != -1 && defaultValue.indexOf(")") != -1 && defaultValue.indexOf("::") != -1) {
			return defaultValue.substring(defaultValue.indexOf("(") + 1, defaultValue.indexOf("::"));
		}
		if (defaultValue.startsWith("((") && defaultValue.endsWith("))"))
			return defaultValue.substring(2, defaultValue.length() - 2);
		else if (defaultValue.startsWith("(") && defaultValue.endsWith(")"))
			return defaultValue.substring(1, defaultValue.length() - 1);

		return defaultValue;
	}

	/**
	 * @todo <b>获取表的外键信息</b>
	 * @author zhongxuchen
	 * @date 2011-8-15 下午10:48:12
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static TableForeignModel getTableImpForeignKeys(String tableName) throws Exception {
		// conn.getMetaData().getf
		ResultSet rs = conn.getMetaData().getImportedKeys(dbConfig.getCatalog(), dbConfig.getSchema(), tableName);
		return (TableForeignModel) DBUtil.preparedStatementProcess(null, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						TableForeignModel result = new TableForeignModel();
						Map<String, String> map = new HashMap<String, String>();
						while (rs.next()) {
							result.setPkTable(rs.getString("PKTABLE_NAME"));
							result.setFkTable(rs.getString("FKTABLE_NAME"));
							result.setFkName(rs.getString("FK_NAME"));
							result.setUpdateRule(rs.getInt("UPDATE_RULE"));
							result.setDeleteRule(rs.getInt("DELETE_RULE"));
							String pkColName = rs.getString("PKCOLUMN_NAME");
							String fkColName = rs.getString("FKCOLUMN_NAME");
							// 避免重复
							if (!map.containsKey(pkColName.toUpperCase())) {
								if (result.getFkColumns() == null) {
									result.setFkColumns(new String[] { fkColName });
									result.setPkColumns(new String[] { pkColName });
								} else {
									String[] fkColumns = new String[result.getFkColumns().length + 1];
									String[] pkColumns = new String[result.getPkColumns().length + 1];
									System.arraycopy(result.getFkColumns(), 0, fkColumns, 0,
											result.getFkColumns().length);
									System.arraycopy(result.getPkColumns(), 0, pkColumns, 0,
											result.getPkColumns().length);
									fkColumns[fkColumns.length - 1] = fkColName;
									pkColumns[pkColumns.length - 1] = pkColName;
									result.setPkColumns(pkColumns);
									result.setFkColumns(fkColumns);
								}
								map.put(pkColName.toUpperCase(), fkColName);
							}
						}
						this.setResult(result);
					}
				});
	}

	/**
	 * @todo <b>获取表的主键约束名称</b>
	 * @author zhongxuchen
	 * @date 2011-8-15 下午10:48:01
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static TablePkModel getTablePKConstraint(String tableName) throws Exception {
		ResultSet rs = conn.getMetaData().getPrimaryKeys(dbConfig.getCatalog(), dbConfig.getSchema(), tableName);
		return (TablePkModel) DBUtil.preparedStatementProcess(null, null, rs, new PreparedStatementResultHandler() {
			public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
				TablePkModel tablePkModel = new TablePkModel();
				List<String> pkCols = new ArrayList<String>();
				while (rs.next()) {
					tablePkModel.setPkName(rs.getString("PK_NAME"));
					pkCols.add(rs.getString("COLUMN_NAME"));
				}
				String[] cols = new String[pkCols.size()];
				pkCols.toArray(cols);
				tablePkModel.setPkColumns(cols);
				this.setResult(tablePkModel);
			}
		});
	}

	/**
	 * 获取表索引信息
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static List<TableIndexModel> getTableIndex(String tableName) throws Exception {
		ResultSet rs = conn.getMetaData().getIndexInfo(dbConfig.getCatalog(), dbConfig.getSchema(), tableName, false,
				false);
		return (List<TableIndexModel>) DBUtil.preparedStatementProcess(null, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						List<TableIndexModel> indexes = new ArrayList<TableIndexModel>();
						HashMap<String, TableIndexModel> map = new HashMap<String, TableIndexModel>();
						String indexName;
						while (rs.next()) {
							indexName = rs.getString("INDEX_NAME");
							TableIndexModel index = map.get(indexName);
							if (index == null) {
								index = new TableIndexModel();
								index.setIndexName(indexName);
								index.setColumnNames(new String[] { rs.getString("COLUMN_NAME") });
								map.put(indexName, index);
								indexes.add(index);
							} else {
								String[] columns = new String[index.getColumnNames().length + 1];
								System.arraycopy(index.getColumnNames(), 0, columns, 0, index.getColumnNames().length);
								columns[index.getColumnNames().length] = rs.getString("COLUMN_NAME");
								index.setColumnNames(columns);
							}
							index.setOrderType(rs.getString("ASC_OR_DESC"));
							int type = rs.getInt("TYPE");
							if (type == 0)
								index.setIndexType("无索引");
							else if (type == 1)
								index.setIndexType("聚集索引");
							else if (type == 2)
								index.setIndexType("哈希表索引");
							else
								index.setIndexType("其它索引");
						}
						this.setResult(indexes);
					}
				});
	}

	/**
	 * @todo 获取存储过程信息
	 * @return
	 * @throws Exception
	 */
	public static List<ProduceModel> getProduceInfo() throws Exception {
		ResultSet rs = conn.getMetaData().getProcedures(dbConfig.getCatalog(), dbConfig.getSchema(), "%");
		return (List<ProduceModel>) DBUtil.preparedStatementProcess(null, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						List<ProduceModel> list = new ArrayList<ProduceModel>();
						while (rs.next()) {
							ProduceModel produceModel = new ProduceModel();
							produceModel.setProduceName(rs.getString("PROCEDURE_NAME"));
							produceModel.setProductType(rs.getString("PROCEDURE_TYPE"));
							list.add(produceModel);
						}
						this.setResult(list);
					}
				});
	}

	/**
	 * @todo 获取存储过程列信息
	 * @return
	 * @throws Exception
	 */
	public static List<ProduceColumn> getProduceColumns() throws Exception {
		ResultSet rs = conn.getMetaData().getProcedureColumns(dbConfig.getCatalog(), dbConfig.getSchema(), "%", "%");
		return (List<ProduceColumn>) DBUtil.preparedStatementProcess(null, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						List<ProduceColumn> list = new ArrayList<ProduceColumn>();
						while (rs.next()) {
							ProduceColumn produceColumn = new ProduceColumn();
							produceColumn.setProduceName(rs.getString("PROCEDURE_NAME"));
							produceColumn.setColumnName(rs.getString("COLUMN_NAME"));
							produceColumn.setColumnType(rs.getString("COLUMN_TYPE"));
							list.add(produceColumn);
						}
						this.setResult(list);
					}
				});
	}

	/**
	 * @todo 获取函数信息
	 * @return
	 * @throws Exception
	 */
	public static List<FunctionModel> getFunctionInfo() throws Exception {
		ResultSet rs = conn.getMetaData().getFunctionColumns(dbConfig.getCatalog(), dbConfig.getSchema(), "%", "%");
		return (List<FunctionModel>) DBUtil.preparedStatementProcess(null, null, rs,
				new PreparedStatementResultHandler() {
					public void execute(Object obj, PreparedStatement pst, ResultSet rs) throws SQLException {
						List<FunctionModel> list = new ArrayList<FunctionModel>();
						HashMap<String, FunctionModel> tmp = new HashMap<String, FunctionModel>();
						String functionName;
						String columnName;
						while (rs.next()) {
							functionName = rs.getString("FUNCTION_NAME");
							columnName = rs.getString("COLUMN_NAME");
							FunctionModel function = tmp.get(functionName);
							if (function == null) {
								function = new FunctionModel();
								function.setFunctionName(functionName);
								function.setColumns(new String[] { columnName });
								tmp.put(functionName, function);
								list.add(function);
							} else {
								String[] columns = new String[function.getColumns().length + 1];
								System.arraycopy(function.getColumns(), 0, columns, 0, function.getColumns().length);
								columns[function.getColumns().length] = columnName;
							}
						}
						this.setResult(list);
					}
				});
	}

	public static int getDBType() throws Exception {
		return DBUtil.getDbType(conn);
	}

}
