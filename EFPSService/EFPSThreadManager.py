#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 16 23:23:27 2020

@author: abhiram
"""
import EFPSPredictorSocketHandler
import TimeSeriesAnomalyDetector
import time
threadCount=8;
mcuList=[0,1]
from threading import Thread
pendingThreads=[i for i in range(8)]
threadMap=[None for i in range(8)]
threadSensorMap=[None for i in range(8)]
threadModelMap=[1 for i in range(8)] #default Anomaly Model
#0 means Pretrained Model
#1 means Anomaly Model
while(True):
    if(len(pendingThreads)==0):
        time.sleep(100);
    else:
        id=pendingThreads.pop()
        #dispatchThread(id)
def dispatchPredictorThread(id):
    global pendingThreads
    global threadMap
    global theadSensorMap
    try:
        socket = EFPSPredictorSocketHandler.EFPSSocketHandler()
        threadMap[id]=socket
        threadSensorMap[id]=socket.sensor
        #do prediction stuff
    except:
        pendingThreads.append(id)
        
def test():
  global l
  l[1]=0  
thread=Thread(target=test)
thread.start()
