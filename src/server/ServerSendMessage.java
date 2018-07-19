package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import client.Client;
import client.ClientGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import utilities.ThreadUtils;

public class ServerSendMessage implements Runnable {
	private DataInputStream in;
	private Socket socket;
	private Vector<ClientHandler> clients;
	private Thread serverSendMessage;
	
	public ServerSendMessage(Vector<ClientHandler> clients, Socket socket) {
		this.clients = clients;
		this.socket = socket;
		
		intializeInputStream(socket);
				
		populateClientBox();
		
		startServerSendMessageThread();
	}
	
	public void intializeInputStream(Socket socket) {
		try {
			in = new DataInputStream(socket.getInputStream());	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServerSendMessageThread() {
		serverSendMessage = new Thread(this);
		serverSendMessage.start();
	}
	
	public void populateClientBox() {
		String username = "";
		
		new JFXPanel(); //init javafx toolkit

		ClientGUI.clearClientsBox();
		for (ClientHandler client : clients) {
			username = client.getUsername();
			
			ClientGUI.printClientUsername(username);
		}
		ThreadUtils.sleep(10);
	}
	
	@Override
	public void run() {		
		boolean isRunning = true;
		String message = "";	
				
		while (isRunning) {
			message = readMessage();
			
    		for (ClientHandler client : clients) {
				sendMessageToSpecifiedClient(client, message);
    		}
		}
	}
	
	public String readMessage() {
		String message = "";
		
		try {
			message = in.readUTF();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return message;  
	}
	
	public void sendMessageToSpecifiedClient(ClientHandler client, String message) {
		DataOutputStream out;
		out = client.getOut();
		
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			removeClientThatThrewException(client);	
		}
	}

	public void removeClientThatThrewException(ClientHandler client) {
		clients.remove(client);
	}
}
