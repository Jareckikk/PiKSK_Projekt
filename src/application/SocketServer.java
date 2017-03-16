package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable{

	public void run(){

		try{
		    Socket socket=null;
		    ServerSocket serverSocket=null;
		    System.out.println("Server Listening......");    
	    	serverSocket = new ServerSocket(4445); // can also use static final PORT_NUM , when defined

	    while(!Thread.currentThread().isInterrupted()){
	        socket = serverSocket.accept();
	        System.out.println("connection Established");
	        listenIn(socket);
	        }
	    
		}catch(IOException e){
	    e.printStackTrace();
	    System.out.println("Server error");
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
		    System.out.println(line);
	
	        if(is!=null){is.close();}
	        if(os!=null){os.close();}
	        if(socket!=null){socket.close();}
	    } catch(IOException ie){
	        System.out.println("Error");
		}
	   
	    
	
}

	
}
