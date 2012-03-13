@echo off
set path=../soft/eclipse/jre;%path%
set file=%1
set maxk=%2

set cp=lib/ads-project.jar
set cp=%cp%;lib/args4j-2.0.1.jar
set cp=%cp%;lib/commons-jexl-2.1.1.jar
set cp=%cp%;lib/commons-logging-1.1.1.jar

set indir=samples
set outdir=output


set infile=%indir%/%file%.txt
set outfile=%outdir%/%file%.csv

java -classpath %cp% mta.ads.smid.app.ConsoleApplication -in %infile% -out %outfile% -maxK %maxk%

pause