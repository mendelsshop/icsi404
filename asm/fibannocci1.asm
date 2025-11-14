copy R1 10 ; set paramter to 10
call 3 ; fib 4
halt ; program done
; fib start x = R1
copy R2 2
copy R4 1
branch ge R1 R2 2 ; if x >= 2
; x < 2
math add R1 R3 ; result += x
return
; x > 1
push add R1 0 ; save x
math sub R1 R4 R1 ; x--
call 5 ; call x (x - 1)
math sub R1 R4 R1 ; x--
call 5 ; call x (x - 2)
pop R1 ; restore x
return
