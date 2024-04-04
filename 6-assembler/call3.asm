copy R1 5
copy R2 6
callif ne R1 R2 2
math not R31 R0 R3
halt
copy R3 2
shift left R1 R3 R31
return
