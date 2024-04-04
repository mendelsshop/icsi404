copy R1 5
call 6
math add R3 R0 R5
copy R1 7
call 6
halt
copy R2 0
copy R4 1
branch ne R1 R2 2
copy R3 1
return
math sub R1 R4 R1
call 8
math add R1 R4 R1
math mul R1 R3
return
