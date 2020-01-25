import os
LABEL_DIR="processed/labels"
DENOISE_DIR = "processed/denoised"
RAW_DIR = "raw"
LABELLED_DATA_DIR = "processed/labelled_data"

if(not os.path.exists(DENOISE_DIR)):
    print("directory with denoised data",DENOISE_DIR,"does not exist. Quitting ...")
    quit()

if(not os.path.exists(LABEL_DIR)):
    print("directory with labels",LABEL_DIR,"does not exist. Quitting ...")
    quit()
else:
    label_file_list = [os.path.join(LABEL_DIR,os.path.basename(i).split(".")[0]+".label") for i in os.listdir(RAW_DIR)]
    denoised_file_list = [os.path.join(DENOISE_DIR,i) for i in os.listdir(RAW_DIR)]
if(not os.path.exists(RAW_DIR)):
    print("directory with raw data does not exist.. Needed to identify processed directory structure")
    print("Quitting now .. ")
    quit()
if(not os.path.exists(LABELLED_DATA_DIR)):
    print("directory to store labelled data ",LABELLED_DATA_DIR," does not exist")
    print("Creating",LABELLED_DATA_DIR)
    os.mkdir(LABELLED_DATA_DIR)


import pickle
import pandas
import matplotlib.pyplot as plt
for i in range(len(label_file_list)):
    m = pandas.read_csv(denoised_file_list[i],header=None)
    print(denoised_file_list[i])
    d= pickle.load(open(label_file_list[i],"rb"))
    print(label_file_list[i])
    print(d)
    m["Class"]=-1
    m=m.rename(columns={0:"Current"})
    if(d[1]):
        print("d[1]=",d[1])
        for limit in d[1]:
            x1,x2 = limit[0],limit[1]
            m.iloc[x1:x2,1]=1
    if(d[0]):
        print("d[0]=",d[0])
        for limit in d[0]:
            x1,x2 = limit[0],limit[1]
            m.iloc[x1:x2,1]=0
    m.plot()
    plt.show()
    output_file_name=os.path.join(LABELLED_DATA_DIR,os.path.basename(denoised_file_list[i]).split(".")[0]+".csv")
    print("writing to output file:",output_file_name)
    m.to_csv(output_file_name,index=False)
