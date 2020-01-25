choice_string= '''
What Normalization do you want to perform.\n
[0]. MinMaxScaler
[1]. StandardScaler
[2]. RobustScaler
'''
import os
SCALER_DIR = "processed/scaler"
PLOT_DIR = "processed/plots"
LABELLED_DATA_DIR = "processed/labelled_data"


if(not os.path.exists(SCALER_DIR)):
    print("The directory to save the scalers ",SCALER_DIR,"does not exist")
    print("Creating",SCALER_DIR)
    os.mkdir(SCALER_DIR)



from sklearn.externals import joblib
import matplotlib.pyplot as plt
#import the scalers
from sklearn.preprocessing import MinMaxScaler
# minMaxScaler = MinMaxScaler()
# minMaxScaler.fit(x)
# minMaxScalerOut  = minMaxScaler.transform(x).T[0]
# plt.plot(minMaxScalerOut)
# plt.show()

from sklearn.preprocessing import StandardScaler
# standardScaler = StandardScaler()
# standardScaler.fit(x)
# standardScalerOut = standardScaler.transform(x).T[0]
# plt.plot(standardScalerOut)
# plt.show()

from sklearn.preprocessing import RobustScaler
# robustScaler = RobustScaler()
# robustScaler.fit(x)
# robustScalerOut = robustScaler.transform(x).T[0]
# plt.plot(robustScalerOut)
# plt.show()


import pandas as pd
import numpy as np


def scaler_pipeline(file_name,Scaler_Name,Scaler):
    m=pd.read_csv(file_name)
    #create a numpy list from the "Current" column
    x = np.array([m["Current"]]).T
    scaler=Scaler()
    scaler.fit(x)
    scalerOut = scaler.transform(x).T[0]
    plt.plot(x)
    fig = plt.gcf()
    plt.title(Scaler_Name)
    plt.show()
    fig.savefig(os.path.join(PLOT_DIR,os.path.splitext(os.path.basename(file_name))[0]+"_"+Scaler_Name))
    return scaler

scalerClassList=[MinMaxScaler,StandardScaler,RobustScaler]
scalerName=["MinMaxScaler","StandardScaler","RobustScaler"]
scalerObjectList=[]
file_list = [os.path.join(LABELLED_DATA_DIR,i) for i in os.listdir(LABELLED_DATA_DIR)]
for j in file_list:
    for i in range(len(scalerClassList)):
        scaler=scaler_pipeline(j, scalerName[i], scalerClassList[i])
        scalerObjectList.append(scaler)
    choice = int(input(choice_string))
    basepath = os.path.basename(j).split(".")[0]
    scaler_save_location= os.path.join(SCALER_DIR,basepath+"_"+scalerName[choice]+".pkl")
    joblib.dump(scalerObjectList[choice],scaler_save_location)
    print("The scaler choice ",scalerName[choice],"is saved at location ",scaler_save_location)
