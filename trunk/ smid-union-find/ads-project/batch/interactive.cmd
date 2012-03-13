@echo off
set path=../soft/eclipse/jre;%path%

set cp=lib/ads-project.jar
set cp=%cp%;lib/args4j-2.0.1.jar
set cp=%cp%;lib/commons-jexl-2.1.1.jar
set cp=%cp%;lib/commons-logging-1.1.1.jar

java -classpath %cp% mta.ads.smid.app.ConsoleApplication -i