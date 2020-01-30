import os
import joblib
PIPE_DIRECTORY = "/run/user/"+str(os.getuid())+"/ssald"
if(not os.path.exists(PIPE_DIRECTORY)):
    os.mkdir(PIPE_DIRECTORY)
PYTHON_PIPE_FILE = PIPE_DIRECTORY+"/in"
JAVA_PIPE_FILE = PIPE_DIRECTORY+"/out"
try:
    os.mkfifo(PYTHON_PIPE_FILE)
except:
    pass
try:
    os.mkfifo(JAVA_PIPE_FILE)
except:
    pass

fout = open(PYTHON_PIPE_FILE,"w")
fin = open(JAVA_PIPE_FILE,"r")


intial=True
sensorList=None
os.chdir("../")
model = joblib.load("processed/models/LedOut_combined_SVM.model")
try:
    while True:
        # print("Inside python while",counter)
        if(intial==True):
            fout.write("Hello\n")
            intial=False
        else:
            fout.write(str(model.predict(sensorList)[0])+"\n")
        # print("About to write hello")

        fout.flush()
        # print("Flushed")
        z = fin.readline()
        print("Read the line from fin")
        while(z==""):
            z = fin.readline()
            # print("stuck")
        if(z!="Hello\n"):
            sensorList = [[float(i) for i in z.split(",")]]
        print(sensorList)
except KeyboardInterrupt:
    fin.close()
    fout.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
