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
        fout.write("Hello")
        fout.flush()
        z = fin.read()
        while(z==""):
            z = fin.read()
        print(z)
        break
except KeyboardInterrupt:
    fin.close()
    fout.close()
    os.remove(PYTHON_PIPE_FILE)
    os.remove(JAVA_PIPE_FILE)
