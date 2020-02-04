import os
import joblib
from argparse import ArgumentParser
parser.add_argument('--id', help='Id info for pipe')
args = parser.parse_args()
PIPE_DIRECTORY = "/run/user/"+str(os.getuid())+"/ssald"
LOG_FILE = "/tmp/pythonPipe.log"
f_log = open(LOG_FILE,"w")
if(not os.path.exists(PIPE_DIRECTORY)):
    os.mkdir(PIPE_DIRECTORY)
PYTHON_PIPE_FILE = PIPE_DIRECTORY+"/EFPSin"
JAVA_PIPE_FILE = PIPE_DIRECTORY+"/EFPSout"
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
model = joblib.load("models/LedOut_combined_SVM.model")
try:
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
    fin.close()
    fout.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
