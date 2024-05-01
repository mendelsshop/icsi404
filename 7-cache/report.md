Without cache
# Caching Result with array summing:

As per the instructions, I tested my CPU with and without cache in three and a half different scenarios.
I found that caches do improve performance, but how much it helps depends on what your doing.
If it is array based (or anything that in memory is sequential), a cache can help a lot going from 51766 clock cycles down to at best 10206, which is about an 80% decrease in the clock cycle.
Compare this to jumpy linked list which while it does yield improvements with the cache, they are not as big as with arrays going from 75122 to at best 40942, nearly halving the clock cycle with about a 45% reduction in clock cycle. 
We see similiar numbers when summing an array in reverse going from a clock cycle of 65534 down to at best 14834, with about a 77% reduction in clock cycle, the reason it not as bit as with a simple array is that we are doing more instructions in general when going in reverse.
I tried going in reverse without traversing to the end, and without cache the numbers are very similiar to non reversed array with a clock cycle of 53272, but interestingly the best result with caching is 14352, which is not much better then the going in reverse with a traversal.

## Why do I keep mentioning best case?

Well the cache eviction is random, and it may evict something that should not be eviected yet. 
Here are the clock cycles after 25 run for each test:

|run|array |linked list |array reverse |array reverse (no traversal) |
|---|---|---|---|---|
1 | 12246 | 44022 | 23674 | 23192
2 | 13606 | 41622 | 25714 | 25232
3 | 11566 | 40942 | 20954 | 25232
4 | 10206 | 41622 | 18234 | 15712
5 | 12246 | 41622 | 24354 | 20472
6 | 12246 | 42302 | 18234 | 19792
7 | 10886 | 41622 | 18234 | 25232
8 | 12926 | 41622 | 25714 | 24552
9 | 12246 | 43102 | 25034 | 20472
10 | 12246 | 41622 | 14834 | 24552
11 | 11566 | 41622 | 25714 | 23192
12 | 11566 | 42302 | 23674 | 20472
13 | 11566 | 41622 | 25714 | 25232
14 | 12246 | 45982 | 18234 | 25232
15 | 10886 | 41622 | 20274 | 19792
16 | 12246 | 41742 | 15514 | 25232
17 | 10886 | 43102 | 25034 | 14352
18 | 12246 | 42302 | 20274 | 15032
19 | 11566 | 41622 | 24354 | 15032
20 | 12246 | 40942 | 25034 | 23192
21 | 12246 | 41622 | 26394 | 24552
22 | 12246 | 40942 | 23674 | 23872
23 | 12926 | 40942 | 23674 | 25912
24 | 11566 | 40942 | 22994 | 25912
25 | 12926 | 41982 | 20274 | 25232
best | 10205 | 40942 | 14834 | 14352
worst | 13606 | 45982 | 26394 | 25192 
average | 11974 | 41975 | 22232 | 22267
no cache | 51766 | 75122 | 65534 | 53272

I should note that I did at one time maybe see the linked list go down to about 20000, but its all random (cache eviction), and did it happen when I ran it forthe final results.

## Two Types or reverse summing

What reverse summing meant was a little confusing so to be on the safe side I did two approaches.

Reverse summing traversal means we first have to go to the end before we start summing, something like:
```
i = 40 // start address
z = i // copy start address
sum = 0

while i < l.length + i
    i++
while i > z
    sum += l[i--]
```

While reverse summing without traversal is:
```
z = 40 // start addres
sum = 0
i = z + l.length
while i > z
    sum += l[i--]
```

Which is why I was a little suprised that there was not much difference between the reverse summing approaches

## conclusion:

In conclusion, caching really helps when stuff are very close in memory, yet it still improves clock cycle when code is not cache friendly (like linked lists), and does slow down clock cycle in worst case scenarios. Another important thing is to have a good cache eviction policy, which will make espically loading your instruction be faster.
