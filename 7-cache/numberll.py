import random
import sys


used_list: list[int] = [0]

def get_new_random() -> int:
    next = random.randrange(800)
    while next in used_list and next + 1 not in used_list  and next + 2 not in used_list  and next + 3 not in used_list:
        next = random.randrange(800)
    used_list.append(next)
    return next;

def main(n):
    offset = 91
    sum = 0
    print(f"copy R1 {offset}")
    print("copy R3 1")
    for _ in range(n):
        pointer = get_new_random()
        if pointer < 0:
            print("to big pointer")
            sys.exit(1)
         
        pointer += offset
        print(f'store R1 {pointer}')
        print(f'copy R1 {pointer}')
        num = random.randrange(-100,100)
        sum += num
        print(f'store R1 {num}')
        print('math add R3 R1')
    print('store R1 -1')
    print(sum)

if __name__ == "__main__":
    main(20)
