package Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import jarvisReborn.Core;



public class TelnetServerClientHandler extends Thread {
		Socket clientSocket;
		int id;
		TelnetServerClientHandler(Socket clientSocket,int id) {
			this.clientSocket=clientSocket;
			this.id = id;
		}
		public void run() {
			try { 
					System.out.println("Thread starting with id "+id);
				    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				    while(true) {
				    	String message = in.readLine();
				    	if(message==null) {
				    		System.out.println("Client "+id+" disconnect!");
				    		return;
				    	}
				    	/*int i = message.indexOf(" ");
						int j = message.indexOf(" ", i+1);
						int z = message.indexOf(" ",j+1);
						int pin = Integer.valueOf(message.substring(i+1,j));
						int operation = Integer.valueOf(message.substring(j+1,z));
						message = Core.telnet[Integer.valueOf(message.substring(z+1))].echo(pin+" "+operation+"\r");*/
				    	System.out.println("Telnet "+id+" : "+message);
				    }
			}
			catch (Exception e) {
				System.out.println("Starting server error!");
			}
		}
}
