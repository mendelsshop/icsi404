import random
import sys

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)
def int_to_twos_complement_format(num, bits):
    mask = (1 << bits) - 1
    return "{:0{}b}".format(num & mask, bits)

used_list: list[int] = [0]

def get_new_random() -> int:
    next = random.randrange(800)
    while next in used_list and next + 1 not in used_list  and next + 2 not in used_list  and next + 3 not in used_list:
        next = random.randrange(800)
    used_list.append(next)
    return next;

def main():
    sum = 0
    memory = [int_to_twos_complement_format(0, 32) for i in range(800)]
    start = 0
    for _ in range(20):
        pointer = get_new_random()
        eprint(pointer)
        if pointer < 0:
            print("to big pointer")
            sys.exit(1)
        memory[start] = int_to_twos_complement_format(pointer+10, 32)
        start = pointer
        num = random.randrange(-100,100)
        sum += num
        memory[start] = int_to_twos_complement_format(num, 32)
        eprint(pointer+1)
        start += 1
        memory[start] = int_to_twos_complement_format(-1, 32)
    for str in memory:
        print(str)
    print(sum)

if __name__ == "__main__":
    main()
