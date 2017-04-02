package application;

import java.io.IOException;
import java.net.ServerSocket;

public class Receiver {
	private ServerSocket serverSocket = null;
	private MessageHandler message = null;
	
	Receiver(MessageHandler message){
		try {
			this.serverSocket = new ServerSocket(4445);
			this.message = message;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
    	Thread socketServer = new Thread(new ServerThread(serverSocket, message));
    	socketServer.start();
    	System.out.println("Receiver started.");
	}
	
	public void kill(){
		if((serverSocket != null) && (!serverSocket.isClosed())){try {
			serverSocket.close();
			System.out.println("Receiver stopped.");
		} catch (IOException e) {
			e.printStackTrace();
		}}
	}
}
