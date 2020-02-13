<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<title>数据库差异对比报告</title>
	<style>
	.table{font-size:14px;
	width:100%;
	text-align: center;	
	border-spacing: 0;
	table-layout: auto;
	line-height:32px;}	
		
	</style>
</head>

<body>
	<table border="1" cellspacing="0" class="table">
	<tr>
	<td colspan="9" align="middle" style="font-weight:bold; font-size:16px;">数据库:[${root.referenceDB}] vs [${root.targetDB}] 差异报告-${root.reportTime}</td>
	</tr>
	   <tr>
		<th>类型</th>
		<th>对象名称</th>
		<th>数据库[${root.referenceDB}]</th>
		<th>数据库[${root.targetDB}]</th>
		<th>字段差异</th>
		<th>主键差异</th>
		<th>索引差异</th>
		<th>外键差异</th>
		<th>备注</th>
	   </tr>
	<#if (root.tableDiffs?exists)>
	<#list root.tableDiffs as row>
	   <tr>
		<td>表</td>
		<td>${row.tableName}</td>
		<td><#if (row.reference)>存在<#else><font color="red">不存在</font></#if></td>
		<td><#if (row.target)>存在<#else><font color="red">不存在</font></#if></td>
		<td><#if (row.columnsDifference?exists)>${row.columnsDifference}</#if></td>
		<td><#if (row.pkDifference?exists)>${row.pkDifference}</#if></td>
		<td><#if (row.indexDifference?exists)>${row.indexDifference}</#if></td>
		<td><#if (row.foreignKeyDifference?exists)>${row.foreignKeyDifference}</#if></td>
		<td><#if (row.tableDifference?exists)>${row.tableDifference}</#if></td>
	   </tr>
    </#list>
    </#if>
  </table>
</body>
</html>
