copy R1 1
copy R2 -27
callif gt R1 R2 1
halt
push xor R1 R2 R0
copy R3 5
callif eq R1 R3 R1 2
pop R4
return
copy R5 1
copy R6 30
push or R6 R3
callif eq R5 R1 -8
pop R7
return
