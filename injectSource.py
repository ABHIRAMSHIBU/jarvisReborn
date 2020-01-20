#!/usr/bin/env python3
# Copyleft 2020 Abhiram Shibu
# Project SSAL-OS
import os
file="/etc/apt/sources.list"
data=open(file).read()
if("deb https://download.docker.com/linux/raspbian/ stretch stable" not in data):
	os.system("echo \"deb https://download.docker.com/linux/raspbian/ stretch stable\" >> /etc/apt/sources.list")
	print("Patched /etc/apt/sources.list")
else:
	print("Already patched /etc/apt/sources.list")

