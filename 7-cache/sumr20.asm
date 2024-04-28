copy R1 60
math add R1 R0 R2
copy R3 1
store R2 20
math add R3 R2
store R2 87
math add R3 R2
store R2 53
math add R3 R2
store R2 96
math add R3 R2
store R2 -59
math add R3 R2
store R2 -38
math add R3 R2
store R2 -45
math add R3 R2
store R2 91
math add R3 R2
store R2 77
math add R3 R2
store R2 44
math add R3 R2
store R2 -64
math add R3 R2
store R2 -72
math add R3 R2
store R2 -49
math add R3 R2
store R2 -3
math add R3 R2
store R2 -10
math add R3 R2
store R2 87
math add R3 R2
store R2 1
math add R3 R2
store R2 94
math add R3 R2
store R2 24
math add R3 R2
store R2 -98
math add R3 R2
store R2 68
load R1 R2 0 ; l.length
math add R3 R1 ; i
math add R1 R0 R5 ; z 
math add R1 R2 ; l.length + i
math add R3 R1 ; i++
branch lt R1 R2 -2 ; while i < l.length + i
copy R4 0 ; sum = 0
load R1 R6 0
math add R6 R4
math sub R1 R3 R1 ; i-- 
branch ge R1 R5 -4 ; while i > z
halt
