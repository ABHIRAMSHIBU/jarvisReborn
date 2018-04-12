package Sockets;

import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer {
	int count = 0;
	static ServerSocket serverSocket;
	public TelnetServer(int portNumber) {
		try { 
			    serverSocket = new ServerSocket(portNumber);
			    while(true) {
			    	Socket clientSocket = serverSocket.accept();
			    	System.out.println("Accepted connection "+count);
			    	(new TelnetServerClientHandler(clientSocket,count)).start();
			    	count++;
			    }
			}
		catch (Exception e) {
			System.out.println("Starting server error!");
		}
	}
}
