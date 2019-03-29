package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class PythonServerClientHandler extends Thread {
	Socket clientSocket;
	int id;
	public PrintWriter out; // Python IO should be available
	public BufferedReader in;
	public Boolean writeEnable=false;
	public Boolean readEnable=false;
	public Boolean noInput=false;
	public String writeString;
	public String readString;
	int count=0;
	PythonServerClientHandler(Socket clientSocket,int id) {
		System.out.println("Inside the Python Server Client constructor");
		this.clientSocket=clientSocket;
		this.id = id;
	}
	public void run() {
		try { 
			System.out.println("Thread starting with id "+id);
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    this.out=out;
		    this.in=in;
		    Thread readWatch=new Thread() {
		    	public void run() {
		    		while(true) {
			    		try {
							String input=in.readLine();
							readString=input;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
		    	}
		    };
		    readWatch.start();
		    while(true) {
		    	synchronized (this) {
		    		if(writeString!=null) {
			    		//writeString="MEH!";
			    		System.out.println("Write called "+writeString);
//			    		Thread.sleep(10000);
			    		this.out.println(writeString);
			    		writeEnable=false;
			    		
			    		//System.exit(0);
			    		writeString=null;
			    	}
//		    		if(readEnable) {
//		    			System.out.println("In called");
//			    		if(in.ready()) {
//			    			noInput=false;
//			    			readString=in.readLine();
//			    			readEnable=false;
//			    		}
//			    		else {
//			    			System.out.println("Not ready");
//			    		}
//			    	}
				}
		    	Thread.sleep(1);
		    }	
		}
		catch (Exception e) {
			System.out.println("Client python handler crash... Restarting..");
			run();
		}
	}
}
