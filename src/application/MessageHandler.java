package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
	List<String> operands = new ArrayList<String>();
	List<String> operators = new ArrayList<String>();
	
	
	public boolean parseLine(String line){
		line = line.replaceAll("\\s+","");
		boolean openBracket = false;
		
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) == '<'){
				openBracket = true;
			}
			if(line.charAt(i) == '>' && openBracket){
				openBracket = false;
				operands.add(line.substring(0, i)); //TODO nie dziala, poprawic
				line = line.substring(i + 1);
				i = 0;
				continue;
			} else if(line.charAt(i) == '>' && !openBracket) {
				return false;
			}
			if(openBracket){continue;} //TODO dodaæ walidacje koñca linii
			if(Character.toString(line.charAt(i)).matches("[\\+\\-\\/\\*]")){
				operators.add(Character.toString(line.charAt(i)));
				line = line.substring(i + 1);
				i = 0;
			} else {return false;}
		}
		for(String s : operators){
			System.out.println(s);
		}
		for(String s : operands){
			System.out.println(s);
		}
		return false;
	}
	
	public void send(String msg) throws IOException{
		
		//<"127.0.0.1".x>+<"192.32.12.3".y>
		
		this.parseLine(msg);
		//TODO Dodaæ parsowanie i wyci¹ganie adresu z 'msg' zamiast wpisywac statycznie
		//ClientThread.send("192.168.0.14", msg);
	}
}
