# if assembler is not compiled compile it
if [ ! -d bin/Assembler ] || [ $1 = '-r' ]; then
	javac -d bin -sourcepath src src/Assembler/AssemblerTest.java
fi
# then run it with in and out files
if [ $1 = '-r' ]; then
	java --enable-preview  -cp bin Assembler.AssemblerTest -- $2 $3
else
	java --enable-preview  -cp bin Assembler.AssemblerTest -- $1 $2
fi
