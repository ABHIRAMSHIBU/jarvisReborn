#!/usr/bin/env python
from ssal_core_simulator_main import *
d=Device("0",0.2,0.1)
def handle_client(conn,addr,ID):
    global id
    print("Waiting client thread")
    while(True):
        n=conn.recv(100)
        if(ID != 0):
            conn.send(b"Not Allowed!\r\n")
        else:
            if(n==b""):
                break
            try:
                print(n.decode(),end="")
                processedOutput=str(d.recieve(n.decode().strip()))
                print(processedOutput)
                conn.send(((processedOutput)+"\r\n").encode())
                print("ID=",ID)
            except UnicodeDecodeError:
                print("decode error handled")
    if(ID==0):
        pwf.write("idzf")
        pwf.flush()



import socket
import sys
import os

HOST = '0.0.0.0'	# Symbolic name, meaning all available interfaces
PORT = 23	# Arbitrary non-privileged port

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print("Socket error");
    print(msg)
    sys.exit()

print('Socket bind complete')

#Start listening on socket
s.listen(10)
print('Socket now listening')

#now keep talking with the client
id=0  #set global id
pr,pw=os.pipe()
import fcntl
fcntl.fcntl(pr, fcntl.F_SETFL, os.O_NONBLOCK) 
prf=os.fdopen(pr,"r")
pwf=os.fdopen(pw,"w")
try:
    while 1:
        #wait to accept a connection - blocking call
        conn, addr = s.accept()
        z=prf.read(4)
        if(z=="idzf"):
            id=0
        print("Read z value pipe : ",z)
        pid=os.fork()
        if(pid !=  0):
            print('Connected with ' + addr[0] + ':' + str(addr[1]))
            print("Parent continuing")
            id+=1
        else:
            handle_client(conn,addr,id)
except KeyboardInterrupt:
    print("Bye")
s.close()
