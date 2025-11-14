copy R1 6
call 3
halt
copy R2 1
copy R3 2
branch le R3 R1 2 ;if R2 <= R1
math add R1 R4 ;r4 += r1
return
math sub R1 R2 R1 ; R1--
call 5 ;fib n -1
math sub R1 R2 R1 ;R1--
call 5 ;fib n - 2 
math add R1 R3 R1 ;R1+=2
return
