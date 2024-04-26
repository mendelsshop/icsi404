copy R1 10 ; start address
copy R2 20
math add R1 R2 ; set end address
copy R3 0 ; sum
copy R4 1
load R1 R5 0
math add R5 R3
math add R4 R1
branch lt R1 R2 -4
halt
