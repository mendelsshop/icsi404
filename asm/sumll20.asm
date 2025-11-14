copy R1 92
copy R3 1
store R1 557
copy R1 557
store R1 58
math add R3 R1
store R1 365
copy R1 365
store R1 -38
math add R3 R1
store R1 872
copy R1 872
store R1 -44
math add R3 R1
store R1 776
copy R1 776
store R1 28
math add R3 R1
store R1 254
copy R1 254
store R1 -54
math add R3 R1
store R1 109
copy R1 109
store R1 -16
math add R3 R1
store R1 655
copy R1 655
store R1 1
math add R3 R1
store R1 432
copy R1 432
store R1 85
math add R3 R1
store R1 249
copy R1 249
store R1 -42
math add R3 R1
store R1 311
copy R1 311
store R1 27
math add R3 R1
store R1 269
copy R1 269
store R1 63
math add R3 R1
store R1 638
copy R1 638
store R1 -16
math add R3 R1
store R1 548
copy R1 548
store R1 -66
math add R3 R1
store R1 723
copy R1 723
store R1 84
math add R3 R1
store R1 733
copy R1 733
store R1 -53
math add R3 R1
store R1 187
copy R1 187
store R1 -1
math add R3 R1
store R1 573
copy R1 573
store R1 4
math add R3 R1
store R1 622
copy R1 622
store R1 -79
math add R3 R1
store R1 362
copy R1 362
store R1 -59
math add R3 R1
store R1 456
copy R1 456
store R1 -21
math add R3 R1
store R1 -1
copy R1 92
copy R2 0 ; sum
load R1 R1 0
branch lt R1 R0 4
load R1 R4 0
math add R4 R2 ; 4
load R1 R1 1
branch ge R1 R0 -4
halt
