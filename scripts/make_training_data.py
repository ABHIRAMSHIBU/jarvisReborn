LABELLED_DATA_DIR = "processed/labelled_data"
SCALER_DIR = "processed/scaler"
TRAINING_DIR = "processed/train"
import os
import pandas as pd
import numpy as np
if(not os.path.exists(TRAINING_DIR)):
    print("The training directory ",TRAINING_DIR," does not exist")
    print("creating ",TRAINING_DIR)
    os.mkdir(TRAINING_DIR)

file_names= [os.path.join(LABELLED_DATA_DIR,i) for i in os.listdir(LABELLED_DATA_DIR)]
file_base_names =[os.path.basename(i).split(".")[0] for i in file_names]


scaler_file_names=[]
for i in file_base_names:
    for j in os.listdir(SCALER_DIR):
        if(i in j):
            scaler_file_names.append(os.path.join(SCALER_DIR,j))
if(len(scaler_file_names)!=len(file_base_names)):
    print("Scaler count and file count dont match")

from sklearn.externals import joblib
scalerList=[]
for i in scaler_file_names:
    scalerList.append(joblib.load(i))

for i in range(len(file_names)):
    m = pd.read_csv(file_names[i])
    current = np.array(m["Current"])
    fail  = np.array(m["Class"])
    current=current[:-1*(len(current)%100)]
    current=current.reshape(-1,100)
    fail=fail[:-1*(len(fail)%100)]
    fail = fail.reshape(-1,100)
    fail=(fail==1)
    c=[]
    for j in range(len(fail)):
        c.append(np.all(fail[j]))
    columns = ["t"+str(i) for i in range(100)]
    df = pd.DataFrame(data=current,columns=columns)
    c=np.array(c)
    c=c.astype("int")
    df["Class"]=c
    output_file_name = os.path.join(TRAINING_DIR,file_base_names[i]+".csv")
    df.to_csv(output_file_name,index=False)
