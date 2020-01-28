import os
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

try:
    while True:
        fout.write("Hello\n")
        fout.flush()
        z = fin.readline()
        while(z==""):
            z = fin.readline()
            # print("stuck")
        print(z)
except KeyboardInterrupt:
    fin.close()
    fout.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
