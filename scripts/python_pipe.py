import os
PIPE_DIRECTORY = "/run/user/"+str(os.getuid())+"/ssald"
if(not os.path.exists(PIPE_DIRECTORY)):
    os.mkdir(PIPE_DIRECTORY)
PIPE_FILE = PIPE_DIRECTORY+"/in"
os.mkfifo(PIPE_FILE)
