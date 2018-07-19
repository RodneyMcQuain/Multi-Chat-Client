package client;

import java.io.DataInputStream;
import java.io.IOException;

import utilities.ThreadUtils;

public class ClientRecieveMessage implements Runnable {
	private DataInputStream in;
	private Thread clientRecieveMessage;

	private volatile boolean isRunning = true;
	
	public ClientRecieveMessage(DataInputStream in) {
		this.in = in;
				
		startClientRecieveMessageThread();
	}
	
	public void startClientRecieveMessageThread() {
		clientRecieveMessage = new Thread(this);
		clientRecieveMessage.start();
	}
	
	@Override
	public void run() {
		String message = "";
				
		ThreadUtils.sleep(500); //wait for server
			
		while (isRunning) {
			message = readMessage();
			
			ClientGUI.printToChatBox(message);
		}
	}
	
	public String readMessage() {
		String message = "";
		
		try {
			message = in.readUTF();			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			stopRunning();
		}
		
		return message;
	}
	
	public void stopRunning() {
		isRunning = false;
	}
}
