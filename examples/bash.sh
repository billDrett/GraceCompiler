#!/bin/bash

mydir="/home/bill/Dropbox/Eksamino8/metaglwtistes/GraceCompiler/examples"

for file in "$(find $mydir -type f)"
do
	usr/bin/java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $file
	echo 
done