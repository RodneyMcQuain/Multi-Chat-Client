package utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadUtils {
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public static Socket createClientSocket(String ip, int portNum, int timeout) {
		Socket socket = null;
		
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, portNum), timeout);
			socket.setSoTimeout(timeout);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return socket;
	}
	
	public static DataInputStream createInputStream(Socket socket) {
		DataInputStream in = null;
		
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return in;
	}
	
	public static DataOutputStream createOutputStream(Socket socket) {
		DataOutputStream out = null;
		
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	public static ObjectInputStream createObjectInputStream(Socket socket) {
		ObjectInputStream objectIn = null;
		
		try {
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return objectIn;
	}
	
	public static ObjectOutputStream createObjectOutputStream(Socket socket) {
		ObjectOutputStream objectOut = null;
		
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return objectOut;
	}
}
