#!/usr/bin/env bash
# Copyleft 2020 Abhiram Shibu
# Project SSAL-OS
sudo systemctl start docker.service
sudo docker pull influxdb
echo "Now you may run start.sh"
