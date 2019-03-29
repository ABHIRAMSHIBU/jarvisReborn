package Sockets;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import jarvisReborn.Core;

public class PythonServer {
	int count = 0;
	public PythonServerClientHandler active;
	static ServerSocket serverSocket;
	@SuppressWarnings("deprecation")
	public PythonServer(int portNumber) {
		Core.python=this;
		try { 
			    serverSocket = new ServerSocket(portNumber,0, InetAddress.getByName(null));	
			    while(true) {
			    	
			    	Socket clientSocket = serverSocket.accept();
			    	System.out.println("Accepted connection "+count);
//			    	(new TelnetServerClientHandler(clientSocket,count)).start();
			    	try {
			    		active.stop();
			    	}
			    	catch(Exception e) {
			    		System.out.println("Python Client dead, unable to stop");
			    	}
			    	PythonServerClientHandler psch = new PythonServerClientHandler(clientSocket, count);
			    	psch.start();
			    	active=psch;
			    	count++;
			    }
			}
		catch (Exception e) {
			System.out.println("Starting server error!");
		}
	}
}
