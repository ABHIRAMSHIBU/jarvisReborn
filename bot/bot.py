#!/usr/bin/python3.7
from telegram.ext import Updater, CommandHandler,Job, Filters, MessageHandler
import telegram
import SSAL_CLIENT
client=SSAL_CLIENT.ssal()
def allHandle(bot,update):
    msg=update.message.text
    update.message.reply_text(msg)
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

updater = Updater(key)
updater.dispatcher.add_handler(CommandHandler('start', start))
updater.dispatcher.add_handler(CommandHandler('set', _set))
updater.dispatcher.add_handler(CommandHandler('get', get))
updater.dispatcher.add_handler(CommandHandler('test', test))
updater.dispatcher.add_handler(CommandHandler('reset', reset))
unknown_handler = MessageHandler(Filters.chat, allHandle)
updater.dispatcher.add_handler(unknown_handler)
updater.start_polling()
#updater.idle()

