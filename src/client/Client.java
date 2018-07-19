package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import utilities.ThreadUtils;

public class Client {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String username;
	
	public Client(String username, String ip, int portNum, int timeout) {
		this.username = username;
		
		socket = ThreadUtils.createClientSocket(ip, portNum, timeout);
		out = ThreadUtils.createOutputStream(socket);
		in = ThreadUtils.createInputStream(socket);
		
		new ClientRecieveMessage(in);
	}
	
	public void sendMessage(String message) {
		try {
			out.writeUTF(username + ": " + message);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void sendServerMessage(String message) {
		try {
			out.writeUTF(message);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}

	public String getUsername() {
		return username;
	}
	
	public DataOutputStream getOut() {
		return out;
	}
}
