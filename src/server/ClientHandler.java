package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import utilities.ThreadUtils;

public class ClientHandler {
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String username;
	
	public ClientHandler(Socket socket, String username) {
		this.socket = socket;
		this.username = username;

		out = ThreadUtils.createOutputStream(socket);
		in = ThreadUtils.createInputStream(socket);
	}
	
	public void sendMessage(String message) {
		try {
			out.writeUTF(username + ": " + message);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void sendWelcomeMessage(String message) {
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
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

	public DataInputStream getIn() {
		return in;
	}
}
