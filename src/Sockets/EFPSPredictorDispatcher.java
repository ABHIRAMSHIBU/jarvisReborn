package Sockets;

import java.net.InetAddress;
import java.util.HashMap;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import jarvisReborn.Core;
import jarvisReborn.Specification;

public class EFPSPredictorDispatcher {
	int count = 0;
	public EFPSPredictorHandler active;
	static ServerSocket serverSocket;
	public static Stack<Integer> sensorStack = new Stack<Integer>();
	//HashMap is not working if not static in MainCMDHandler.java
	public static HashMap<Integer, EFPSPredictorHandler> activeThread= new HashMap<Integer, EFPSPredictorHandler>();
	@SuppressWarnings("deprecation")
	public EFPSPredictorDispatcher(int portNumber) {
		Core.python=this;
		for(int i=0;i<Core.configParse.data.size();i++) {
			int id=Core.configParse.data.get(i).id;
			for(int j=0;j<Specification.sensorCount;j++) {
				sensorStack.push(id*4+j);
				activeThread.put(id*4+j,null);
			}
		}
		try { 
			    serverSocket = new ServerSocket(portNumber,0, InetAddress.getByName(null));	
			    while(true) {
			    	if(sensorStack.empty()==false) {
			    		int currentid=sensorStack.pop();
			    		Socket clientSocket = serverSocket.accept();
			    		System.out.println("EFPSPredictorDispatcher: Accepted connection "+count);
//			    		(new TelnetServerClientHandler(clientSocket,count)).start();
			    		EFPSPredictorHandler psch = new EFPSPredictorHandler(clientSocket, currentid);
			    		if(activeThread.get(currentid)!=null) {
			    			activeThread.get(currentid).stop();
			    		}
			    		psch.start();
			    		activeThread.replace(currentid,psch);
			    		count++;
			    	}
			    	else {
			    		Thread.sleep(100);
			    	}
			    }
			}
		catch (Exception e) {
			System.out.println("Starting server error!");
		}
	}
}
