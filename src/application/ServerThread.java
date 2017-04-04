package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread implements Runnable{
	ServerSocket serverSocket;
	MessageHandler message;
	
	ServerThread(ServerSocket ss, MessageHandler msg){
		this.serverSocket = ss;
		this.message = msg;
	}
	
	public void run(){
	    Socket socket=null;

		try{
		    while(!Thread.currentThread().isInterrupted()){
		        socket = this.serverSocket.accept();
		        System.out.println("connection Established");
		        listenIn(socket);
		    }
		} catch (SocketException e) {
			System.out.println("Server closed.");
		}catch(IOException e){
		    e.printStackTrace();
		    System.out.println("Server error");
	    }
		
		try {
			if(serverSocket!=null){serverSocket.close();}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listenIn(Socket socket){  
	    String line=null;
	    BufferedReader  is = null;
	    PrintWriter os=null;

	    try{
	        is= new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        os=new PrintWriter(socket.getOutputStream());

		    line=is.readLine();   
		    System.out.println("Odebrano: " + line);
		    this.message.registerMsg(line);
		    
	        if(is!=null){is.close();}
	        if(os!=null){os.close();}
	        if(socket!=null){socket.close();}
	    } catch(IOException ie){
	        System.out.println("Error");
		}
	   
	    
	
}

	
}
