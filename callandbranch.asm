copy R1 45
copy R2 67
branch le R1 R2 R0 1
halt
copy R3 67
callif ge R2 R2 R0 8
math add R4 R1 R5
halt
copy R6 2
push add R2 R6 R31
callif gt R2 R2 R21 5
pop R4
return
