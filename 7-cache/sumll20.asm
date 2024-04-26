copy R1 10 ; start address
copy R2 0 ; sum
copy R3 1
load R1 R1 0
branch lt R1 R0 4
load R1 R4 0
math add R4 R2
load R1 R1 1
branch ge R1 R0 -4
halt
