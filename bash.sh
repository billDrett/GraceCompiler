#!bin/bash

mydir="/home/bill/Dropbox/Eksamino8/metaglwtistes/GraceCompiler/testcaseSemantic"
fileList=`ls ${mydir}/*.grc`

for file in ${fileList}
do
	echo " " $file
	echo " "
	java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $file
done