# sagacity-db-diversity
数据库差异比较工具，用于对比生产、开发、UAT不同环境的表、索引差异


使用方法:
## 1、创建一个目录:db-diversity
## 2、打包形成一个jar包,如：sagacity-diversity-4.1.2-jar-with-dependencies.jar(含驱动jar) 改名:sagacity-diversity-all.jar 
    放于db-diversity目录下面，如需其它数据库，创建drivers目录,将驱动放于其中。
## 3、编写一个数据库配置文件，db-diversity.xml 内容如下:
^^^xml
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
^^^
