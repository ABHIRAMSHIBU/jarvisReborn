DENOISE_DIR = "processed/denoised"
PLOT_DIR = "processed/plots"
LABEL_DIR = "processed/labels"
import os
if(not os.path.exists(DENOISE_DIR)):
    print("Denoise path ",DENOISE_DIR,"does not exist")
    quit()
else:
    denoised_files = [os.path.join(DENOISE_DIR,i) for i in os.listdir(DENOISE_DIR)]
    print("Files present in ",DENOISE_DIR)
    for i in denoised_files:
        print(i)
if(not os.path.exists(LABEL_DIR)):
    print("Directory for labels does not exist")
    print("Creating the directory",LABEL_DIR)
    os.mkdir(LABEL_DIR)

if(not os.path.exists(PLOT_DIR)):
    print("Directory to save plotted images: ",PLOT_DIR,"not Found")
    print("Creating the directory: ",PLOT_DIR)
    os.mkdir(PLOT_DIR)


import pandas
import matplotlib.pyplot as plt
def visualize(file_name):
    print("Visualizing file:",file_name)
    m = pandas.read_csv(file_name,header=None)
    m.plot()
    fig = plt.gcf()
    plt.title(file_name)
    plt.show(block=False)
    fig.savefig(os.path.join(PLOT_DIR,os.path.splitext(os.path.basename(file_name))[0]))
    return m



import pickle
def label(file_name,m):
    d={}
    d[1]=[]
    d[0]=[]
    while True:
        s = input("Do you want to continue yes(Default)/no")
        if(not(s=="" or s=="yes")):
            break
        x1,x2 = [int(i) for i in input("Enter the time limits x1,x2 seperated by ,: ").split(",")]
        if(x1==-1):
            x1=0
        if(x2==-1):
            x2=len(m)
        label = int(input("Label the region in (x1,x2) = ({x1},{x2}) as 1(Normal) or 0(Failure): ".format(x1=x1,x2=x2)))
        if(label==1):
            d[1].append((x1,x2))
        else:
            d[0].append((x1,x2))
            pickle.dump(d,open(os.path.join(LABEL_DIR,os.path.splitext(os.path.basename(file_name))[0]+".label"),"wb"))
for i in denoised_files:
    m=visualize(i)
    label(i,m)
