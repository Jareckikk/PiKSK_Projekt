package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppHandler {
	private Map varMap = new HashMap();
	
	
	public Map getMap(){
		return this.varMap;
	}
	
	public void parseLine(String line){
		
	}
	
	public void sendMsg(String msg) throws IOException{
		
		//TODO Dodaæ parsowanie i wyci¹ganie adresu z 'msg' zamiast wpisywac statycznie
		ClientSocket.send("192.168.0.14", msg);
	}
	
}
