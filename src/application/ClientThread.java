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
	    System.out.println("tukej1");
	    try {
	    	System.out.println("Tukej1,5 " + address.getHostAddress());
	        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
	        System.out.println("tukej2");
	        os= new PrintWriter(s1.getOutputStream());
	        System.out.println("tukej3");
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }
	    
	    if(msg.equals("NaN")){
	    	System.out.println("Wysy³am NaN");
	    	os.println("NaN");
	    } else if(msg.matches("\\d+")){
	    	System.out.println("Wysy³am " + msg);
	    	os.println(msg);
	    }
	    else {
	    	System.out.println("Wysy³am: {" + InetAddress.getLocalHost().getHostAddress() + "}." + msg);
	        os.println("{" + InetAddress.getLocalHost().getHostAddress() + "}." + msg);
	    }
	    

        os.flush();       
        if(s1!=null){s1.close();}
        if(os!=null){os.close();}
	}
}
