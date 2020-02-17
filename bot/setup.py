#!/usr/bin/python3
import os
def gettoken():
	a=input("Enter the access token provided by botfather:")
	while(":" not in a):
		print("You enterned a invalid token, please enter valid token!")
		a=input("Enter the access token provided by botfather:")
	return a
def main():
	print("Bot Setup, Written by Abhiram Shibu")
	f_opened=0
	c=input("Have you created a bot using bot father?[y/n]")
	if('Y' in c or 'y' in c):
		print("Setting up conf.ini")
		token=gettoken().strip()
		if(os.path.exists("conf.ini")):
			print("Configuration file exists! Renaming file to conf.ini.bak")
			z=os.popen("mv conf.ini conf.ini.bak")
			if("Permission denied" in z):
				print("Sorry you dont have enough permission to move the file\nABORT! CODE 0x11")
				os.abort()
		try:
			file=open("conf.ini","w")
			f_opened=1
		except:
			print("Error occured!\nThis could be due to invalid permission!\nCheck if the disk is readonly!")
		if(f_opened):
			file.write(token)
			file.close()
			print("Success! All done! Enjoy!")
		else:
			print("FAILED! ABORTED")
			os.abort()
	else:
		print("You have to create a bot using botfather( https://telegram.me/botfather )")
		print("Help needed? Goto link : https://core.telegram.org/bots#6-botfather")
main()
