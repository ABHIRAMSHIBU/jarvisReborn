from tqdm import tqdm
import os
RAW_DIR="raw"
DENOISE_DIR="processed/denoised"
if(not os.path.exists(RAW_DIR)):
    print("Raw directory with raw unprocessed data ",RAW_DIR," does not exist. Quitting Now ...")
    quit()
else:
    files=os.listdir(RAW_DIR)
    
if(not os.path.exists(DENOISE_DIR)):
    print("Denoise directory does not exist")
    print("Creating denoise directory ",DENOISE_DIR)
    os.mkdir(DENOISE_DIR)

d={}
def clean(name):
    d[name]=[]
    n_lines=len(open("raw/"+name).read().split("\n"))
    f=open("raw/"+name,"r")
    f1=open("processed/denoised/"+name,"w")
    # from IPython.display import clear_output
    error=0
    for i in tqdm(range(n_lines)):
        z=f.readline().strip()
        if(z==""):
            error+=1
        else:
            error=0
            try:
                z=abs(float(z))
                if(z>1):
                    z=1
                f1.write(str(z)+"\n")
            except:
                # clear_output()
                d[name].append([i,z])
                #print("Error occured while processing ",z)
        if(error==10):
            break
    f.close()
    f1.close()
    print("Done!")

for i in files:
    clean(i)


for i in d.items():
	file_name,error_list  = i
	if(error_list):
		print("Error items found for file:",file_name)
		for j in error_list:
			print("Line no:\t",j[0],"\tError point:\t",j[1])
