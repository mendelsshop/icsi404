# if assembler is not compiled compile it
if [ ! -d bin/Assembler ]; then
	javac -d bin -sourcepath src src/Assembler/Assembler.java
fi
# then run it with in and out files
java --enable-preview  -cp bin Assembler.Assembler -- $1 $2
