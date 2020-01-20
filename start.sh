#!/usr/bin/env bash
# Copyleft Abhiram Shibu
# Project SSAL-OS
echo "Project SSAL-OS docker container for influxdb is starting.. Please Wait.."
echo "Port 8086 and 8083 will be listning"
sudo docker run -p 8086:8086 -p 8083:8083 -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro -v $PWD/database:/var/lib/influxdb influxdb -config /etc/influxdb/influxdb.conf
echo "Docker container has quit"
