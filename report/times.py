import sys
import subprocess

best = 100000
worst = -100000

array = [i for i in range(25)]

for i in range(25):
    x = subprocess.run(["java", "@/tmp/cp_7wbws0bu38u32qjdkj4ufs4v5.argfile", sys.argv[1]], stdout=subprocess.PIPE, stderr=subprocess.PIPE).stdout.decode('utf-8')
    x  = (x.splitlines()[-1].split()[1])
    print(x)
    x = int(x)
    array[i] = x
    if x <= best:
        best = x
    if x >= worst:
        worst = x

print(f'average {sum(array)/len(array)}')
print(f'worst {worst}')
print(f'best {best}')
