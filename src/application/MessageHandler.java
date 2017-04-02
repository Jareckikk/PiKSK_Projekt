package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;

public class MessageHandler {
	List<String> operands = new ArrayList<String>();
	List<String> operators = new ArrayList<String>();
	private VariableList varList;
	private Label statusNote = null;
	private Integer lastRsp = null;
		
	MessageHandler(Label label, VariableList varList){
		this.statusNote = label;
		this.varList = varList;
	}
	
	public boolean parseLine(String line){
		line = line.replaceAll("\\s+",""); //Usuwamy wszystkie bia³e znaki.
		if(line.matches("{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])}"
						+ "\\.([a-zA-Z])\\w*(=\\d+)*>"
						+ "([\\+\\-\\/\\*]"
						+ "<\"(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\"\\.([a-zA-Z])\\w*(=\\d+)*)*")) {
			
			boolean openBracket = false;
			for(int i = 0; i < line.length(); i++){
				if(line.charAt(i) == '<'){
					openBracket = true;
				}
				if(line.charAt(i) == '>' && openBracket){
					openBracket = false;
					operands.add(line.substring(0, i + 1));
					line = line.substring(i + 1);
					i = -1;
					continue;
				} else if(line.charAt(i) == '>' && !openBracket) {
					return false;
				}
				if(openBracket){continue;}
				if(Character.toString(line.charAt(i)).matches("[\\+|\\-|\\/|\\*]")){
					operators.add(Character.toString(line.charAt(i)));
					line = line.substring(i + 1);
					i = -1;
				} else {return false;}
			}
		} else {return false;}
		
		if(operands.size() > 1){ //Moze byæ tylko jedno polecenie przypisania w poleceniu
			for(String s : operands){
				if(s.matches("{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])}\\.([a-zA-Z])\\w*=\\d+")){
					return false;
				}
			}
		}
		
		if(operands.size() - operators.size() != 1){ //Operandów musi byæ o jeden wiêcej ni¿ operatorów
			return false;
		}
		
		for(String s : operators){
			System.out.println(s);
		}
		for(String s : operands){
			System.out.println(s);
		}
		return true;
	}
	
	public void send(String msg){		
		if(this.parseLine(msg)){
			
			if(operands.size() == 1){
				Command cmd;
				try {
					cmd = new Command(operands.get(0));
					ClientThread.send(cmd.address, cmd.getMsg());
					if(cmd.value != null){
						while(this.lastRsp == null){} //Waiting for response
						statusNote.setText(lastRsp.toString());
						lastRsp = null;
					}
				} catch (Exception e) {
					statusNote.setText("B³¹d w poleceniu!");
				}		
			}
			
			
			//TODO
			if(operands.size() > 1){
				
			} else {
				statusNote.setText("Nieprawid³owe polecenie!");
			}
			
			operators.clear();
			operands.clear();
		} else{
			statusNote.setText("Nieprawid³owe polecenie!");
		}
	}
	
	public void registerMsg(String msg){
		//x=1
		if(msg.matches("{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])}\\.([a-zA-Z])\\w*=\\d+")){
			try {
				Command cmd = new Command(msg);
				if(this.varList.isVarExist(cmd.variable)){
					this.varList.addVariable(cmd.variable, cmd.value.toString());
				} else {
					ClientThread.send(cmd.address, "NaN");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
						
		} else if(msg.matches("{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])}\\.([a-zA-Z])\\w*")){ //x
			try {
				Command cmd = new Command(msg);
				if(this.varList.isVarExist(cmd.variable)){				
					ClientThread.send(cmd.address, this.varList.getValue(cmd.variable));
				} else {
					ClientThread.send(cmd.address, "NaN");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(msg.matches("\\d+")){ //23 //TODO
			this.lastRsp = Integer.parseInt(msg);
		} else if(msg.matches("NaN")){//NaN
			statusNote.setText("NaN");
		} else {
			statusNote.setText("Odebrano nieprawid³owe polecenie!");
		}
		
		
		
		
		
	}
	
	class Command {
		String address;
		String variable;
		Integer value;
		boolean valueSet = false;
		
		Command(String operand) throws Exception{//TODO
			operand = operand.substring(1); //Wywalamy {
			
			for(int i = 0; i < operand.length(); i++){
				if(operand.charAt(i) == '"'){
					this.address = operand.substring(0, i + 1);
					operand = operand.substring(i + 1);
					break;
				}
			}
			if(!address.matches("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])")){
				throw new Exception("Incorrect IP address!");
			}
			
			if(operand.matches("\\.([a-zA-Z])\\w*=\\d+>")){
				operand = operand.substring(1, operand.length() - 1);
				String[] opvalue = operand.split("=");
				this.variable = opvalue[0];
				this.value = Integer.parseInt(opvalue[1]);
				this.valueSet = true;
			} else if(operand.matches("\\.([a-zA-Z])\\w*>")){
				operand = operand.substring(1, operand.length() - 1);
				this.variable = operand;
			} else {
				throw new Exception("Incorrect parameteres!");
			}
		}
		
		String getMsg(){
			if(valueSet){
				return this.variable + "=" + this.value;
			} else {
				return this.variable;
			}
		}		
	}
	
}
