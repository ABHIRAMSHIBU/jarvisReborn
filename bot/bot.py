#!/usr/bin/python3.7
from telegram.ext import Updater, CommandHandler,Job, Filters, MessageHandler
import telegram
import SSAL_CLIENT
from rasahandler import chatbot
from mariadb_driver import MariaDriver
import os
import speech_recognition as sr
r = sr.Recognizer()

client=SSAL_CLIENT.ssal()
rasa = chatbot()
mariadb = MariaDriver()

def mariaHandle(msg,state): #hardcoded response
    rooms = [i[0] for i in mariadb.get_rooms()]
    room_ids = [i[1] for i in mariadb.get_rooms()]
    chosen_room_name=None
    chosen_room_id=None
    chosen_switch_id = None
    chosen_switch_name=None
    # print(rooms)
    for i in range(len(rooms)):
        # print(type(i.strip()),msg.strip().lower())
        if((rooms[i].strip() in msg.strip().lower())==True):
            response_message = "Room: "+rooms[i]
            chosen_room_name = rooms[i]
            chosen_room_id = room_ids[i]
            break
        # else:
        #     response_message = "Please say a valid room name"
    if(chosen_room_id!=None):
        switches =  mariadb.get_distinct_switch_from_room(chosen_room_id)
        switch_names = [i[2] for i in switches]
        switch_ids = [i[1] for i in switches]
        for i in range(len(switch_names)):
            if(switch_names[i].strip().lower() in msg.strip().lower()):
                chosen_switch_name = switch_names[i]
                chosen_switch_id = switch_ids[i]
                break
    if(chosen_room_id == None and chosen_switch_id==None):
        response_message = "Please say a valid room name"
    if(chosen_room_id!=None and  chosen_switch_id == None):
        response_message = "Please specify valid switch in room "+chosen_room_name
    if(chosen_room_id!=None and chosen_switch_id!=None):
        if(state==1):
            client_return_val = client.set(chosen_switch_id,1,chosen_room_id)
            if(client_return_val==-1 and False):
                response_message = "SSAL Client error for room: "+chosen_room_name+" switch: "+chosen_switch_name
            else:
                response_message = "The "+chosen_room_name + " is "+chosen_switch_name+ " is turned on"       
        elif(state==0):
            client_return_val = client.set(chosen_switch_id,0,chosen_room_id)
            if(client_return_val==-1 and False):
                response_message = "SSAL Client error for room: "+chosen_room_name+" switch: "+chosen_switch_name
            else:
                response_message = "The "+chosen_room_name + " is "+chosen_switch_name+ " is turned off"  
    return response_message

    # return chosen_room_name,chosen_switch_name,chosen_room_id,chosen_switch_id

def handleText(msg):
    response_message = "No Response"
    rasa_response=rasa.predict_nlu(msg)
    if(rasa_response["intent"]["name"]=="set_pin_on"): 
        response_message = mariaHandle(msg,1)
    elif(rasa_response["intent"]["name"]=="set_pin_off"):
        response_message = mariaHandle(msg,0)
    else:
        response_message = rasa.predict_chat(msg)
        if(response_message):
            response_message = rasa.predict_chat(msg)[0]["text"]
        else:
            response_message = "Rasa Backend ERROR"
    return response_message

def allHandle(bot,update):
    msg=update.message.text
    print(update.message.from_user.name,"send",msg)
    response_message=handleText(msg)
    print("bot send",response_message)
    update.message.reply_text(str(response_message))

def start(bot,update):
    user_name=update.message.from_user.first_name
    update.message.reply_text("Hi "+user_name)
    update.message.reply_text("Welcome to SSAL")
def _set(bot,update):
    msg=update.message.text
    msg=[int(i) for i in msg.strip("/set").strip().split(",")]
    update.message.reply_text(client.set(msg[0],msg[1],msg[2]))
def test(bot,update):
    msg=update.message.text
    msg=[int(i) for i in msg.strip("/test").strip().split(",")]
    update.message.reply_text(client.test(msg[0],msg[1]))
def get(bot,update):
    msg=update.message.text
    msg=[int(i) for i in msg.strip("/get").strip().split(",")]
    update.message.reply_text(client.get(msg[0],msg[1]))
def reset(bot,update):
    msg=update.message.text
    msg=int(msg.strip("/reset").strip())
    update.message.reply_text(client.reset(msg))
try: 
   key=open("conf.ini",'r').read().strip()
except: 
   print("Error occured, try running setup.py")
   exit()
def voice_audio(bot,update):
    update.message.reply_text("Doing speech recognition on audio.. Please wait")
    #print("Voice detected...")
    voiceMessage = bot.get_file(update.message.voice.file_id)
    #print("Got file object...")
    if(os.path.exists("voice.ogg")):
        os.remove("voice.ogg")
        os.remove("voice.wav")
    voiceMessage.download('voice.ogg')
    os.system("ffmpeg -i voice.ogg voice.wav 2> /dev/null")
    print("Converted into voice.wav")
    #print("Downloaded File as voice.ogg")
    with sr.AudioFile('voice.wav') as source: # use voice.wav instad of mic
          print("Virtually recording audio file...")
          audio =r.record(source)     # recording to google format
    try:
       message=r.recognize_google(audio)   # speech recognize from recorded audio
       print("Recognized message is: "+message)
       update.message.reply_text(str(message))   # Feedback to user
       update.message.reply_text(handleText(message))
    except:
       print("Google recognize failure!, fallback.") # print error message
       update.message.reply_text("Internal error occured, speech recognition failure!")
    
updater = Updater(key)
updater.dispatcher.add_handler(CommandHandler('start', start))
updater.dispatcher.add_handler(CommandHandler('set', _set))
updater.dispatcher.add_handler(CommandHandler('get', get))
updater.dispatcher.add_handler(CommandHandler('test', test))
updater.dispatcher.add_handler(CommandHandler('reset', reset))
unknown_handler = MessageHandler(Filters.chat, allHandle)
updater.dispatcher.add_handler(MessageHandler(Filters.voice,voice_audio))
updater.dispatcher.add_handler(unknown_handler)
updater.start_polling()
#updater.idle()

