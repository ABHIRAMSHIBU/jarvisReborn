LOG_FILE = "/tmp/pythonPipe.log"
f_log = open(LOG_FILE,"w")
f_log.flush()


import os
joblib=None
ArgumentParser=None
try:
    print("a")
    f_log.write("Importing modules\n")
    print("b")
    import joblib as jl
    from argparse import ArgumentParser as ap
    import sklearn 
    import numpy
    joblib=jl
    ArgumentParser=ap
    f_log.write("Successfully imported modules\n")
except:
    if(os.path.exists("/usr/bin/pip3")):
        f_log.write("Modules not found\n")
        os.system("sudo pip3 install joblib")
        os.system("sudo pip3 install argparse")
        os.system("sudo pip3 install sklearn")
        os.system("sudo pip3 install numpy")
        import joblib as jl
        from argparse import ArgumentParser as ap
        joblib=jl
        ArgumentParser=ap
        f_log.write("modules installed\n")
    else:
        f_log.write("Pip not found\n");
        #while(True):
            #import time
            #time.sleep(100);
parser=ArgumentParser()
parser.add_argument('--id', help='Id info for pipe')
args = parser.parse_args()
id=args.__dict__["id"]
PIPE_DIRECTORY = "/run/user/"+str(os.getuid())+"/ssald"
if(not os.path.exists(PIPE_DIRECTORY)):
    os.mkdir(PIPE_DIRECTORY)
PYTHON_PIPE_FILE = PIPE_DIRECTORY+"/EFPSin"+str(id)
JAVA_PIPE_FILE = PIPE_DIRECTORY+"/EFPSout"+str(id)
try:
    os.mkfifo(PYTHON_PIPE_FILE)
except:
    pass
try:
    os.mkfifo(JAVA_PIPE_FILE)
except:
    pass



intial=True
sensorList=None
model = joblib.load("models/LedOut_combined_SVM.model")
try:
    fout = open(PYTHON_PIPE_FILE,"w")
    fin = open(JAVA_PIPE_FILE,"r")
    while True:
        # print("Inside python while",counter)
        
        if(intial==True):
            f_log.write("sending hello")
            fout.write("Hello\n")
            intial=False
        else:
            f_log.write("Predicting "+str(sensorList)+"\n")
            fout.write(str(model.predict(sensorList)[0])+"\n")
        # print("About to write hello")

        fout.flush()
        # print("Flushed")
        z = fin.readline()
        f_log.write("Read the line from fin"+"\n")
        while(z==""):
            z = fin.readline()
            # print("stuck")
        if(z!="Hello\n"):
            sensorList = [[float(i) for i in z.split(",")]]
        f_log.write(str(sensorList))
        f_log.flush()
except KeyboardInterrupt:
    print("got")
    fin.close()
    fout.close()
    f_log.flush()
    f_log.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
