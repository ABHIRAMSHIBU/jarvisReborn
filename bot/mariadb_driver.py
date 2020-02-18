import mysql.connector
mydb = mysql.connector.connect(host="localhost",user="ssal",password="PqOnei4xt973wToR",database="ssal")
mycursor = mydb.cursor()
def id_to_room(id):
    query = "select * from ssal_rooms where id = "+str(id)+";"
    mycursor.execute(query)
    try:
        return dict(mycursor)[0]
    except:
        return None
def room_to_id(room_name):
    query = "select id from ssal_rooms where lower(name) = \""+room_name+"\";"
    mycursor.execute(query)
    try:
        return list(mycursor)[0][0]
    except:
        return None
def switch_to_id(switch_name,get_room=False):
    query = "select id,id_switch from ssal_switches where name = \""+switch_name+"\";"
    mycursor.execute(query)
    try:
        if(get_room):
            l=list(mycursor)
            out=[]
            for i in l:
                ans={}
                ans["room_id"]=i[0]
                ans["switch_id"]=i[1]
                out.append(ans)
            return out
        else:
            l=list(mycursor)
            out=[]
            for i in l:
                out.append(i[1])
            return out
    except:
        return None
def id_to_switches(id,id_switch=None):
    try:
        if(id_switch==None):
            query = "select * from ssal_switches where id = "+str(id)+";"
            mycursor.execute(query)
            result=list(mycursor)
            out=dict()
            for i in result:
                out[i[1]]=i[2]
            if(len(result.keys)==0):
                return out
        else:
            query = "select * from ssal_switches where id = "+str(id)+" and id_switch = "+str(id_switch)+";"
            mycursor.execute(query)
            result=list(mycursor)
            return result[0][2]
    except:
        pass
    return None
def print_all():
    #print()
    for i in mycursor:
        print(i)
print(id_to_room(2))
print(id_to_switches(2,2))
#print_all()
print(room_to_id("drwaing room"))
print(switch_to_id("testpin",True))
