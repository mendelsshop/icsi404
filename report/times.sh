ARRAY=()
for VAR in y
do
	
	x=$(cd /home/mendel/Documents/ualbany/spring24/icsi404/7-cache ; /usr/bin/env /home/mendel/.sdkman/candidates/java/22.ea.34-open/bin/java @/tmp/cp_7wbws0bu38u32qjdkj4ufs4v5.argfile $1 | grep clock | awk -F " " '{print $2}')
	ARRAY+=(x)
done


