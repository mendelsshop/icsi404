copy R1 8
call 3 
halt
copy R2 1
branch lt R2 R1 2
copy R3 1
return
push sub R1 0
math sub R1 R2 R1
call 4
pop R1
math mul R1 R3
return
