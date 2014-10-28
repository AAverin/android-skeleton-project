@ECHO OFF

REM Start-up script for Retrace -- companion tool for ProGuard, free class file
REM shrinker, optimizer, obfuscator, and preverifier for Java bytecode.
REM
REM Note: when passing file names containing spaces to this script,
REM       you'll have to add escaped quotes around them, e.g.
REM       "\"C:/My Directory/My File.txt\""

IF EXIST "%PROGUARD_HOME%" GOTO home
SET PROGUARD_HOME=..
:home

java -jar C:\Work\Soft\IDE\AndroidStudio\sdk\tools\proguard\lib\retrace.jar %*
