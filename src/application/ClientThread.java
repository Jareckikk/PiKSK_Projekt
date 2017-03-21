package application;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread {
	
	public static void send(String ipAddress, String msg) throws IOException{

	    InetAddress address=InetAddress.getByName(ipAddress);
	    Socket s1=null;
	    PrintWriter os=null;

	    try {
	        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
	        os= new PrintWriter(s1.getOutputStream());
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }

        os.println(msg);
        os.flush();       
        if(s1!=null){s1.close();}
        if(os!=null){os.close();}
	}
}
