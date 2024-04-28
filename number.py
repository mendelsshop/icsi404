import random
def int_to_twos_complement_format(num, bits):
    mask = (1 << bits) - 1
    return "{:0{}b}".format(num & mask, bits)

def main(n):
    sum = 0
    print("copy R1 20")
    print("math add R1 R0 R2")
    print("copy R3 1")
    print(f'store R2 {n}')
    for _ in range(n):
        num = random.randrange(-100,100)
        sum += num
        print("math add R3 R2")
        print(f'store R2 {num }')
    print(sum)

if __name__ == "__main__":
    main(20)
