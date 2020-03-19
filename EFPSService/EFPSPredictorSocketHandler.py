#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 16 23:40:07 2020

@author: abhiram
"""
class EFPSSocketHandler:
    def __init__(self):
        self.sensor=-1
        import telnetlib
        from influxdb import DataFrameClient
        
        sock=telnetlib.Telnet("127.0.0.1",9999)
        sensor=sock.read_until(b"\n")
        sensor=int(sensor)
        self.influxdbClient = DataFrameClient('localhost', 8086, '', '', 'ssal')
        
        self.sock=sock
        self.sensor=sensor
    def sendFailData(self,l):
        sendData=",".join([str(i) for i in l])
        #print("".join(str(l)[1:-1].split(" ")))
        if(l):
            self.sock.write(("failData "+sendData+"\n").encode())
        else:
            self.sock.write(("failData "+"-1"+"\n").encode())
    def getSensorData(self,count):
        sensor_df = self.influxdbClient.query(" select * from \""+str(self.sensor)+"\" group by * order by desc LIMIT "+str(count))
        return sensor_df[list(sensor_df.keys())[0]]
    def close(self):
        self.sock.close()
    def setFailed(self):
        self.sock.write(("setFailed\n").encode())
        return self.sock.read_until(b"\n")
    def unsetFailed(self):
        self.sock.write(("unsetFailed\n").encode())
        return self.sock.read_until(b"\n")
eh = EFPSSocketHandler()
eh.sendFailData([1,2,4])
print(eh.setFailed())
eh.close()
