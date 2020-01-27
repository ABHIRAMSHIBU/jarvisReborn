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

fin = open(JAVA_PIPE_FILE,"r")
fout = open(PYTHON_PIPE_FILE,"w")
try:
    while True:
        fout.print("Hello")
        fout.flush()
        while(True):
            z = fin.read()
            if(z==""):
                continue
            else:
                print(z)
                break
except KeyboardInterrupt:
    fin.close()
    fout.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
