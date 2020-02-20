1、将数据库驱动放在drivers目录下面(jdk9以上不支持自动加载,使用java -Djava.ext.dirs=./drivers 方式加载)
2、将打包的sagacity-db-diversity.jar 放于此路径下面
3、修改db-diversity.xml配置，将数据库配置正确
4、点击start-compare.bat
5、生成diversity-report.html