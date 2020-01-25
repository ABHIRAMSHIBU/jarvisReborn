import os
import pandas
DISJOINT_DIR="disjoint"
RAW_DIR="raw"
files = [os.path.join(DISJOINT_DIR,i) for i in os.listdir(DISJOINT_DIR)]
files.sort()
import pandas as pd
import numpy as np
x=np.array([])
for i in files:
    m = pd.read_csv(i,header=None)
    # print(np.array(m[]))
    x=np.append(x,np.array(m[0]))
df = pd.DataFrame(data=x, index=None, columns=None, dtype=None, copy=False)
df.to_csv(os.path.join(RAW_DIR,os.path.basename(files[0])+"_combined.csv"),index=False)
