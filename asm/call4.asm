copy R1 1
copy R2 -3
callif lt R2 R1 R1 4
math or R31 R2
halt
math not R2 R0 R31
return
