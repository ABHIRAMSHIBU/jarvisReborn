package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;

import jarvisReborn.Details;


public class Telnet{
	Socket socket;
	String ip;
	int port;
	boolean failed=false;
	public boolean status=false,run=false;
	public void reconnect() {
		Socket pingSocket = null;
		try {
			socket.close();
		}
		catch(Exception e){
//			socket=null;
		}
		try {
				pingSocket = new Socket(ip, port);
				socket=pingSocket;
				status=true;
		} catch (IOException e) {
			System.out.println("Telnet Error occured!");
			status=false;
		}
	}
	public boolean checkTelnet(int n) {
		String reply = echo("13\r");
		int z=5;
		if(failed==true) {
			z=1;
		}
		if(n<z) {
			if(reply.equals("No input available")) {
				//System.out.println("Iteration "+n);
				reconnect();
				if(n==4) {
					failed=true;
				}
				return checkTelnet(n+1);
			}
			failed=false;
			return true;
		}
		else {
			return false;
		}
	}
	public boolean pinStatus(int pin) {
		run=false;
		String reply=echo(pin+"\r");
		try{
			if(reply != null) {
				if(Integer.valueOf(reply.substring(0, 1))==1) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		catch(java.lang.NumberFormatException e) {
			return false;
		}
	}
	public String echo(String data) {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Telnet:IOError");
		}
		try {
			while(socket.getInputStream().available()>0) {
				socket.getInputStream().read();
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		out.println(data);
		String z="";
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int j=0;
			int retryCount=Details.FETCH_RETRY_COUNT;
			while(j<retryCount) {
				if(socket.getInputStream().available()>0) {
					z = (in.readLine());
					break;
				}
				else {
					z = "No input available";
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j++;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		run=true;
		return z;
	}
	public Telnet() {
	
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket("192.168.43.146", 23);
					socket=pingSocket;
					status=true;
			} catch (IOException e) {
				System.out.println("Telnet Error occured!");
				status=false;
			}
	}
	public Telnet(String ip) {
		this(ip,23);
	}
	public Telnet(String ip, int port) {
		this.ip=ip;
		this.port=port;
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket(ip, port);
					socket=pingSocket;
					status=true;
			} catch (IOException e) {
				System.out.println("Telnet Error occured!");
				status=false;
			}
	}
}
