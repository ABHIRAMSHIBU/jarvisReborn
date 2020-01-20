#!/usr/bin/env bash
# Copyleft 2020 Abhiram Shibu
# Project SSAL-OS
#Depends on apt-fast
sudo apt-fast install apt-transport-https ca-certificates software-properties-common -y
curl -fsSL get.docker.com -o get-docker.sh
sh get-docker.sh
sudo usermod -aG docker $(whomai)
sudo curl https://download.docker.com/linux/raspbian/gpg | sudo gpg
#No need to patch, already patched by get-docker.sh
#sudo python3 injectSource.py
sudo apt-get update
sudo apt-get upgrade
echo "sudo systemctl start docker.service"
sudo systemctl start docker.service
