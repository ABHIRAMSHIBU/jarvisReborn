package jarvisReborn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Telnet{
	Socket socket;
	public boolean pinStatus(int pin) {
		String reply=echo(pin+"\r");
		if(Integer.valueOf(reply.substring(0, 1))==1) {
			return true;
		}
		else {
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
			e.printStackTrace();
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
			z = (in.readLine());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return z;
	}
	public Telnet() {
	
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket("192.168.43.9", 23);
					socket=pingSocket;
			} catch (IOException e) {
				return;
			}
			//System.out.println("Sending 13 0");
			/*
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			//out.println("13 0\r");
			try {
				System.out.println(in.readLine());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
	}
	public Telnet(String ip) {
		this(ip,23);
	}
	public Telnet(String ip, int port) {
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket(ip, port);
					socket=pingSocket;
			} catch (IOException e) {
				return;
			}
	}
}
