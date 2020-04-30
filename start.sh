#!/usr/bin/env bash
# Copyleft Abhiram Shibu
# Project SSAL-OS
echo "Project SSAL-OS docker container for influxdb is starting.. Please Wait.."
echo "Port 8086 and 8083 will be listning"
sudo docker container rm db
sudo docker run -p 8086:8086 -p 8083:8083 -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro -v $PWD/influx.log:/var/log/influx.log -v $PWD/database:/var/lib/influxdb --name "db" influxdb -config /etc/influxdb/influxdb.conf --log-opt max-size=50m 
if [ $? -ne 0 ]
then
	echo "Some problem occured, trying to start with name"
	sudo docker container start "db"
	echo "Trying to attach with name"
	sudo docker attach "db"
else
	echo "Everything was as expected"
fi
echo "Docker container has quit"
