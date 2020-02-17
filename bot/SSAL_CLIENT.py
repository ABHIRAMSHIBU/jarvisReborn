#!/usr/bin/env python3.7
class ssal:
    import telnetlib
    tn=None
    def connect(self):
        HOST = "localhost"
        self.tn = self.telnetlib.Telnet(host=HOST,port=9998,timeout=10)
        self.tn.read_until(b"SSAL Command line Ready!\n")
    def reset(self,mcu):
        self.tn.write(("$reset "+str(mcu)+"\r\n").encode())
        output=self.tn.read_until(b"\n")
        if(output.decode().strip(">").strip()=="Reset Success!"):
            return 0
        else:
            return -1
    def test(self,pin, mcu):
        self.tn.write(("$test "+str(pin)+" "+str(mcu)+"\r\n").encode())
        output=self.tn.read_until(b"\n")
        if(output.decode().strip(">").strip()=="Error contacting ESP"):
            return -1
        if(output.decode().strip(">").strip()=="0"):
            return 0
        else:
            return 1
    def get(self,pin, mcu):
        self.tn.write(("$get "+str(pin)+" "+str(mcu)+"\r\n").encode())
        output=self.tn.read_until(b"\n")
        if(output.decode().strip(">").strip()=="false"):
            return 0
        else:
            return 1
    def set(self,pin, operation,mcu):
        self.tn.write(("$set "+str(pin)+" "+str(operation)+" "+str(mcu)+"\r\n").encode())
        output=self.tn.read_until(b"\n")
        if(output.decode().strip(">").strip()=="Error contacting ESP"):
            return -1
        if(output.decode().strip(">").strip()==str(pin)+" "+str(operation)):
            return 0
        else:
            return -1
    def close(self):
        self.tn.close()
    def __init__(self):
        self.connect()
    def __del__(self):
        self.close()