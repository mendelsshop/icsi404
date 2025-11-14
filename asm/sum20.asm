copy R1 54
math add R1 R0 R2
copy R3 1
store R2 20
math add R3 R2
store R2 -25
math add R3 R2
store R2 83
math add R3 R2
store R2 64
math add R3 R2
store R2 77
math add R3 R2
store R2 57
math add R3 R2
store R2 -18
math add R3 R2
store R2 58
math add R3 R2
store R2 -64
math add R3 R2
store R2 -9
math add R3 R2
store R2 -70
math add R3 R2
store R2 30
math add R3 R2
store R2 -90
math add R3 R2
store R2 89
math add R3 R2
store R2 -64
math add R3 R2
store R2 77
math add R3 R2
store R2 -76
math add R3 R2
store R2 97
math add R3 R2
store R2 -19
math add R3 R2
store R2 9
math add R3 R2
store R2 -54
copy R2 20
load R1 R2 0
math add R3 R1
math add R1 R2 ; set end address
copy R4 0
load R1 R5 0
math add R5 R4
math add R3 R1
branch lt R1 R2 -4
halt
