1、将数据库驱动放在libs目录下面
2、将打包的sagacity-db-diversity.jar 放于libs目录下面
3、修改db-diversity.xml配置，将数据库配置正确
4、编写start-compare.bat，内容如下:
   java -cp ./libs/* org.sagacity.tools.diversity.StartBooter ./db-diversity.xml 
   点击start-compare.bat
5、生成diversity-report.html