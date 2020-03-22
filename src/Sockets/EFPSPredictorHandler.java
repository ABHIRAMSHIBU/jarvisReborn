package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class EFPSPredictorHandler extends Thread {
	Socket clientSocket;
	int id;
	public PrintWriter out; // Python IO should be available
	public BufferedReader in;
	public Boolean writeEnable=false;
	public Boolean readEnable=false;
	public Boolean noInput=false;
	public String writeString;
	public String readString;
	public String data="0";
	
	int count=0;
	public boolean exit=false;
	EFPSPredictorHandler(Socket clientSocket,int id) {
		this.clientSocket=clientSocket;
		this.id = id;
		
	}
	public void run() {
		try { 
			System.out.println("EFPSPredictorHandler: Thread starting with id "+id);
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    this.out=out;
		    this.in=in;
		    out.println(id);
		    Thread readWatch=new Thread() {
		    	public void run() {
		    		while(true) {
			    		try {
							String input=in.readLine();
							System.out.println("EFPSPredictorHander: id-"+id+" "+input);
							if(input==null) {
								System.out.println("EFPSPredictorHander: id-"+id+" Exiting");
								exit=true;
								break;
							}
							readString=input;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("EFPSPredictorHander: id-"+id+" Exiting");
							exit=true;
							break;
						}
			    		readEnable=true;
		    		}
		    	}
		    };
		    readWatch.start();
		    while(true) {
		    	if(exit==true) {
		    		if(EFPSPredictorDispatcher.sensorStack.search(id)==-1) {
		    			EFPSPredictorDispatcher.sensorStack.push(id);
		    		}
		    		break;
		    	}
		    	if(readEnable==true) {
		    		//Process command here
		    		//failData
		    		//setFailed
		    		//unsetFailed
		    		if(readString.indexOf(" ")!=-1) {
		    			int space=readString.indexOf(" ");
		    			String command = readString.substring(0,space);
		    			data = readString.substring(space+1);
		    			if(command.equals("failData")) {
		    				System.out.println("EFPSPredictorHander: id-"+id+" data="+data);
		    				
		    			}
		    		}
		    		else {
		    			readString=readString.replace("\n","");
		    			readString=readString.replace("\r","");
		    			System.out.println("EFPSPredictorHander: id-"+id+" Tried to read "+readString);
		    			if(readString.equals("setFailed")) {
		    				out.println("setFailed OK");
		    				System.out.println("EFPSPredictorHander: id-"+id+" setFailed called");
		    			}
		    			else if(readString.equals("unsetFailed")) {
		    				out.println("unsetFailed OK");
		    			}
		    		}
		    		readEnable=false;
		    	}
		    	Thread.sleep(10);
		    }	
		}
		catch (Exception e) {
			System.out.println("Client python handler crash... Restarting..");
			run();
		}
	}
}
