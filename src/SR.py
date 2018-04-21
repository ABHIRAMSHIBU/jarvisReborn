import speech_recognition as sr
r = sr.Recognizer()
#try:
    #os.remove("voice.wav") # Remove previously recived voice
#except:
    #print("File dont exist!")  # Handle exception if file is already deleted
#file_id = update.message.voice.file_id
#print("File id:"+file_id)
#newFile = bot.get_file(file_id) # get file from telegram servers
#print("Voice message detected.")
#newFile.download('voice.ogg')  # download file
#os.system("ffmpeg -i / voice.wav 2>null") # convert to wav from ogg 
with sr.AudioFile('/tmp/test.wav') as source: # use voice.wav instad of mic
        #print("Converting audio file.")
        audio =r.record(source)     # recording to google format
f=open("/tmp/RESULT.txt","w")
try:
    message=r.recognize_google(audio)   # speech recognize from recorded audio
    f.write(message)
    
    #print("Recognized message is: "+message)
    #update.message.reply_text(str(message))   # Feedback to user
    flag=1    # Set that voice reognition has passed without any failure
except:
#print("Google recognize failure!, fallback.") # print error message
    f.write("Fail!")
    flag=0 # set voice not recognized
f.close()
