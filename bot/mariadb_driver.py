#!/usr/bin/env python3
class MariaDriver:
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
    def get_distinct_switch_from_room(self,room_id):
        query = "select * from ssal_switches where id="+str(room_id)+";"
        self.mycursor.execute(query)
        try:
            return list(self.mycursor)
        except:
            return None
    def get_rooms(self):
        query = "select distinct lower(name),id from ssal_rooms;"
        self.mycursor.execute(query)
        try:
            return list(self.mycursor)
        except:
            return None
    def add_device(self,room_id,dev_id,dev_name):
        #exception not handled ( room does not exist )
        query = "insert into ssal_switches(id,id_switch,name) values ("+str(room_id)+","+str(dev_id)+","+"\""+dev_name+"\");"
        self.mycursor.execute(query);
        self.mydb.commit()
    def add_room(self,room_id,room_name):
        query = "insert into ssal_rooms(id,name) values ("+str(room_id)+","+"\""+room_name+"\");"
        self.mycursor.execute(query);
        self.mydb.commit()
    def delete_room(self,room_id):
        query = "delete from ssal_rooms where id ="+str(room_id)+";"
        self.mycursor.execute(query);
        self.mydb.commit()
    def delete_device(self,room_id,dev_id):
        query = "delete from ssal_switches where id="+str(room_id)+" and id_switch="+str(dev_id)+";"
        self.mycursor.execute(query);
        self.mydb.commit()
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
d = MariaDriver()
#d.add_device(0,6,"Cooler")
#d.add_room(7,"Dining Room")
#d.delete_room(7)
# d.delete_device(0,7)
# print(d.id_to_room(2))
# print(d.id_to_switches(2,2))
# print(d.room_to_id("drwaing room"))
# print(d.switch_to_id("testpin",True))
print(d.get_distinct_switch_from_room(1))
print(d.get_rooms())