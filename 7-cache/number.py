import random
def int_to_twos_complement_format(num, bits):
    mask = (1 << bits) - 1
    return "{:0{}b}".format(num & mask, bits)

def main():
    sum = 0
    for _ in range(20):
        num = random.randrange(-100,100)
        sum += num
        print(f'{int_to_twos_complement_format(num, 32) }')
    print(sum)

if __name__ == "__main__":
    main()
