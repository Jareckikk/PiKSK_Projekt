package application;

import java.io.IOException;
import java.net.ServerSocket;

public class Receiver {
	private ServerSocket serverSocket = null;
	
	Receiver(){
		try {
			serverSocket = new ServerSocket(4445);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
    	Thread socketServer = new Thread(new ServerThread(serverSocket));
    	socketServer.start();
	}
	
	public void kill(){
		if((serverSocket != null) && (!serverSocket.isClosed())){try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}}
	}
}
