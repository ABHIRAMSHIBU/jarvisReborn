choice_string= '''
What Normalization do you want to perform.\n
[1]. StandardScaler
[2]. MinMaxScaler
[3]. RobustScaler
'''
import os
SCALER_DIR = "processed/scaler"
if(not os.path.exists(SCALER_DIR)):
    print("The directory to save the scalers ",SCALER_DIR,"does not exist")
    print("Creating",SCALER_DIR)
    os.mkdir(SCALER_DIR)

import pandas as pd
pd.read_csv()
#create a numpy list from the "Current" column
import numpy as np
x = np.array([m["Current"]]).T
print(x.shape)

from sklearn.externals import joblib
import matplotlib.pyplot as plt
#import the scalers
from sklearn.preprocessing import MinMaxScaler
minMaxScaler = MinMaxScaler()
minMaxScaler.fit(x)
minMaxScalerOut  = minMaxScaler.transform(x).T[0]
plt.plot(minMaxScalerOut)
plt.show()

from sklearn.preprocessing import StandardScaler
standardScaler = StandardScaler()
standardScaler.fit(x)
standardScalerOut = standardScaler.transform(x).T[0]
plt.plot(StandardScalerOut)
plt.show()

from sklearn.preprocessing import RobustScaler
robustScaler = RobustScaler()
robustScaler.fit(x)
robustScalerOut = robustScaler.transform(x).T[0]
plt.plot(robustScalerOut)
plt.show()


choice = int(input(choice_string))
print(choice)
