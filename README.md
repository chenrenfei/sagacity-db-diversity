# * 睿智平台下面的小工具,强烈推荐比mybatis强大的 sqltoy-orm，可以让查询性能优化大幅提升！
  https://github.com/chenrenfei/sagacity-sqltoy
  
# * sagacity-db-diversity
数据库差异比较工具，用于对比生产、开发、UAT不同环境的表、索引差异


# 使用方法:
## 1、创建一个目录:db-diversity
## 2、打包形成一个jar包,如：sagacity-diversity-4.1.2-jar-with-dependencies.jar(含驱动jar) 改名:sagacity-diversity-all.jar 
    放于db-diversity目录下面，如需其它数据库，创建drivers目录,将驱动放于其中。
## 3、编写一个数据库配置文件，db-diversity.xml 内容如下(修改正确数据库连接配置):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<diversity xmlns="http://www.sagframe.com/schema/diversity"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.sagframe.com/schema/diversity http://www.sagframe.com/schema/sagacity-diversity.xsd"
	include-tables="(?i)\w+">
	<!-- 测试 -->
	<reference-db name="test">
		<url><![CDATA[jdbc:mysql://192.168.56.101:3306/sagacity?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8]]>
		</url>
		<driver value="com.mysql.cj.jdbc.Driver" />
		<username value="dev" />
		<password value="dev" />
		<catalog value="dev" />
		<schema value="dev" />
	</reference-db>

	
	<target-db name="uat">
		<url><![CDATA[jdbc:mysql://192.168.56.102:3306/sagacity?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8]]>
		</url>
		<driver value="com.mysql.cj.jdbc.Driver" />
		<username value="uat" />
		<password value="uat" />
		<catalog value="uat" />
		<schema value="uat" />
	</target-db>
	
</diversity>
```
## 4、编写一个bat文件start-compare.bat
```
java -jar sagacity-diversity-all.jar ./db-diversity.xml
cmd
```
## 5、执行完成后会在当前目录下生成diversity-report.html
	
<!doctype html>
<html>
<head>
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
	<td colspan="9" align="middle" style="font-weight:bold; font-size:16px;">数据库:[dev] vs [uat] 差异报告-2019年10月17日14时37分50秒</td>
	</tr>
	   <tr>
		<th>类型</th>
		<th>对象名称</th>
		<th>参照数据库</th>
		<th>对比数据库</th>
		<th>字段差异</th>
		<th>主键差异</th>
		<th>索引差异</th>
		<th>外键差异</th>
		<th>备注</th>
	   </tr>
	   <tr>
		<td>表</td>
		<td>crm_member_bind</td>
		<td>存在</td>
		<td>存在</td>
		<td><br>列数量分别为:[8][9]</br><br>字段:[MOBILE]在referenceDB中不存在!</br></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>t_student</td>
		<td>存在</td>
		<td><font color="red">不存在</font></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>crm_enterprise_copy1</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_enterprise_category</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_attr_define</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_cate_attr</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_cate_qualification</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_category</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_sku_attr_value</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_goods_spu</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_manufacturer</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_payment_terms</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_pic_store</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
	   <tr>
		<td>表</td>
		<td>gd_trademark</td>
		<td><font color="red">不存在</font></td>
		<td>存在</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	   </tr>
  </table>
</body>
</html>



