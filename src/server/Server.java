package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Vector;

import utilities.ThreadUtils;

public class Server implements Runnable {
	public static final int PORT_NUM = 5001;
	private static Vector<ClientHandler> clients;

	private ServerSocket serverSocket;
		
	public Server(int portNum) {
        clients = new Vector<ClientHandler>();	
        
        initializeServerSocket(portNum);
        
        startServerThread();
	}
	
	public void initializeServerSocket(int portNum) {
        try {
			serverSocket = new ServerSocket(portNum);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void startServerThread() {
        Thread serverThread = new Thread(this);
        serverThread.start();

        System.out.println("Server started!\n");
	}
	
	@Override
	public void run() {		
		boolean isRunning = true;
		
		try {
			while(isRunning) {
				Socket clientSocket = serverSocket.accept();
				
				DataInputStream in = ThreadUtils.createInputStream(clientSocket);
				String username = in.readUTF();
				
				ClientHandler clientHander = createClientHandler(clientSocket, username);
				
				ThreadUtils.sleep(1000); //waiting for client thread to be fully initialized
				
				clients.add(clientHander);
				
				new ServerSendMessage(clients, clientSocket);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public ClientHandler createClientHandler(Socket socket, String username) {
		
		ClientHandler clientHandler = new ClientHandler(socket, username);
		clientHandler.sendWelcomeMessage("Hello " + username + ", you connected to server at " + LocalDateTime.now());
		
		return clientHandler;
	}
	
	public static Vector<ClientHandler> getClients() {
		return clients;
	}

	public static void main(String[] args) {
		new Server(PORT_NUM);
	}
}
