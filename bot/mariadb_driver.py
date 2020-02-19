#!/usr/bin/env python3
class Driver:
    def __init__(self):
        import mysql.connector
        self.mydb = mysql.connector.connect(host="localhost",user="ssal",password="PqOnei4xt973wToR",database="ssal")
        self.mycursor = self.mydb.cursor()
    def id_to_room(self,id):
        query = "select * from ssal_rooms where id = "+str(id)+";"
        self.mycursor.execute(query)
        try:
            return dict(self.mycursor)[0]
        except:
            return None
    def room_to_id(self,room_name):
        query = "select id from ssal_rooms where lower(name) = \""+room_name+"\";"
        self.mycursor.execute(query)
        l=list(self.mycursor)
        try:
            return l[0][0]
        except:
            return None
    def switch_to_id(self,switch_name,get_room=False):
        query = "select id,id_switch from ssal_switches where name = \""+switch_name+"\";"
        self.mycursor.execute(query)
        try:
            if(get_room):
                l=list(self.mycursor)
                out=[]
                for i in l:
                    ans={}
                    ans["room_id"]=i[0]
                    ans["switch_id"]=i[1]
                    out.append(ans)
                return out
            else:
                l=list(self.mycursor)
                out=[]
                for i in l:
                    out.append(i[1])
                return out
        except:
            return None
    def id_to_switches(self,id,id_switch=None):
        try:
            if(self.id_switch==None):
                query = "select * from ssal_switches where id = "+str(id)+";"
                self.mycursor.execute(query)
                result=list(self.mycursor)
                out=dict()
                for i in result:
                    out[i[1]]=i[2]
                if(len(result.keys)==0):
                    return out
            else:
                query = "select * from ssal_switches where id = "+str(id)+" and id_switch = "+str(id_switch)+";"
                self.mycursor.execute(query)
                result=list(self.mycursor)
                return result[0][2]
        except:
            pass
        return None
    def print_all():
        #print()
        for i in self.mycursor:
            print(i)
    #print(id_to_room(2))
    #print(id_to_switches(2,2))
    #print_all()
    #print(room_to_id("drwaing room"))
    #print(switch_to_id("testpin",True))
d = Driver()
print(d.id_to_room(2))
print(d.id_to_switches(2,2))
print(d.room_to_id("drwaing room"))
print(d.switch_to_id("testpin",True))
