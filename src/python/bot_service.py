import response
import telnetlib
import time
HOST = "localhost"
PORT = "9999"
socket = tn = telnetlib.Telnet(host=HOST,port=PORT)
try:
	while(True):
		data=socket.read_until('\n'.encode('utf-8')).decode("utf-8")
		resp=response.response(data.strip("\n"),"123")
		resp=resp[0]+"\n"
		print("Ans:",resp)
		socket.write(resp.encode("utf-8"))
		print("Data recived:",data)
		time.sleep(0.5)
except KeyboardInterrupt:
	#logger.exception(Exception)
	print("Ctrl+C detected quiting")
socket.close()
