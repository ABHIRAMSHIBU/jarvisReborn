import re
import random
class Device:
    def __init__(self,name,sensor1,sensor2):
        self.name=name
        self.sensor1 = sensor1
        self.sensor2 = sensor2
        self.state=[0 for i in range(20)]
    def display_details(self):
        print("Device name :{name}, State = {state}, Sensor value = {sensor}".format(name=self.name,state=self.state,sensor=self.sensor))
    def recieve(self,msg):
        words = msg.split()
        if(len(words)==1 and "sensor" not in words): #input 13 output 13 state
            if(words[0].isdigit()):
                print(self.state)
                return str(self.state[int(words[0])])
        elif(len(words)==2): # input 13 1 output 13 1
                if(words[1].isdigit() and words[0].isdigit()):
                    self.state[int(words[0])] = int(words[1])
                    print(self.state)
                    return str(words[0])+" "+str(self.state[int(words[0])])
                else:
                    print(words,"not both are digits")

        elif(msg=="sensor"): #input sennsor output self.sensor
            self.sensor1 = random.uniform(0,3.3)
            self.sensor2 = random.uniform(0,3.3)
            return str(self.sensor1)+" "+str(self.sensor2)
        else:
            return -1
#save this and run


