@echo off
java -jar "%~dp0/../${project.build.finalName}.jar" "%cd%" %*
