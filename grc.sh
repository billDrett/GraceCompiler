#!/bin/bash

input=$1
output=$2
assemblyfile="$output.s"

java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $input $assemblyfile $3 $4
gcc -o $output -m32 $assemblyfile
