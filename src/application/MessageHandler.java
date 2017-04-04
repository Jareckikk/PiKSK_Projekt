package application;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class MessageHandler {
	List<String> operands = new ArrayList<String>();
	List<String> operators = new ArrayList<String>();
	private VariableList varList;
	private Label statusNote = null;
	private String lastRsp = "";
		
	MessageHandler(Label label, VariableList varList){
		this.statusNote = label;
		this.varList = varList;
	}
	
	public boolean parseLine(String line){
		line = line.replaceAll("\\s+",""); //Usuwamy wszystkie bia³e znaki.
		
		//Sprawdzamy czy zgodny ze wzorcem -> {12.13.31.3}.x+{2.2.2.2}.y=3/{34.1.3.3}.y <- on jest b³êdny, bo y=3 moze wytapic tylko samotnie, sprawdzamy to pozniej
		if(line.matches("\\{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\}\\.([a-zA-Z])\\w*(=\\d+)*"
				+ "([\\+\\-\\/\\*]\\{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\}\\.([a-zA-Z])\\w*(=\\d+)*)*")) {
			
			//Rozdzielamy wszystkie dzia³ania
			boolean openBracket = false;
			for(int i = 0; i < line.length(); i++){
				if(Character.toString(line.charAt(i)).matches("[\\+|\\-|\\/|\\*]")){ //Szukamy + - / *
					operators.add(Character.toString(line.charAt(i))); //{1.1.1.1}.x lub {2.2.2.2}.x=1 trafia do operatorow
					operands.add(line.substring(0, i)); // +-/* trafia do operandow
					line = line.substring(i + 1); //wyrzucamy wylapane operatory i operandy
					i = -1;
				}
				if(i == line.length() - 1){ //Jesli ostatni lub jedyny, to odrazu do operandow
					operands.add(line);
				}				
			}
		} else {
			clearAll();
			return false;}
		
		if(operands.size() > 1){ //Moze byæ tylko jedno polecenie przypisania w poleceniu
			for(String s : operands){
				if(s.matches("\\{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\}\\.([a-zA-Z])\\w*=\\d+")){
					clearAll();
					return false;
				}
			}
		}
		
		if(operands.size() - operators.size() != 1){ //Operandów musi byæ o jeden wiêcej ni¿ operatorów
			clearAll();
			return false;
		}
	
		/*
		for(String s : operators){
			System.out.println(s);
		}
		for(String s : operands){
			System.out.println(s);
		}*/

		return true;
	}
	
	public void send(String msg){		
		if(this.parseLine(msg)){
			
			if(operands.size() == 1){
				Command cmd;
				try {
					cmd = new Command(operands.get(0));
					ClientThread.send(cmd.address, cmd.getMsg());
					if(cmd.value == null){
						int cnt = 0;
						while(this.lastRsp.equals("")){  //Waiting for response
							Thread.sleep(1);
							cnt++;
							if(cnt == 1000){
								setText("Problem z odbiorem wiadomoœci!");
								break;
							}
						}
						setText(lastRsp.toString());
						lastRsp = "";
					}
				} catch (Exception e) {
					e.getMessage();
					setText("B³¹d w poleceniu!");
				}		
			}
			
			if(operands.size() > 1){
				String result = "";
				for(int i = 0; i < operands.size(); i++){
					Command cmd;
					try {
						cmd = new Command(operands.get(i));
						ClientThread.send(cmd.address, cmd.getMsg());	
						
						int cnt = 0;
						while(this.lastRsp.equals("")){  //Waiting for response
							Thread.sleep(1);
							cnt++;
							if(cnt == 1000){
								setText("Problem z odbiorem wiadomoœci!");
								return;
							}
						}
						if(lastRsp.equals("NaN")){
							setText("NaN");
							lastRsp = "";
							return;
						}		
						result += lastRsp.toString();
						lastRsp = "";
						
					} catch (Exception e) {
						e.getMessage();
						setText("B³¹d w poleceniu!");
					}		
					
					if(i < operators.size()){
						result += operators.get(i);
					}				
				}
				System.out.println("Wynik: " + result); 
				Expression exp = new Expression(result);
				setText("Wynik: " + result + " = " + exp.eval().toBigInteger().toString());
			} 
			
			clearAll();
		} else{
			setText("Nieprawid³owe polecenie! 13");
		}
	}
	
	public void registerMsg(String msg){
		//x=1
		if(msg.matches("\\{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\}\\.([a-zA-Z])\\w*=\\d+")){
			try {
				Command cmd = new Command(msg);
				if(this.varList.isVarExist(cmd.variable)){
					System.out.println(cmd.variable + " " + cmd.value.toString());
					this.varList.addVariable(cmd.variable, cmd.value.toString());
					this.varList.refresh();
				} else {
					ClientThread.send(cmd.address, "NaN");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
						
		} else if(msg.matches("\\{(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\}\\.([a-zA-Z])\\w*")){ //x //TODO
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
			this.lastRsp = msg;
		} else if(msg.matches("NaN")){//NaN
			this.lastRsp = "NaN";
			setText("NaN");
		} else {
			setText("Odebrano nieprawid³owe polecenie!");
		}	
	}
	
	private void clearAll(){
		operators.clear();
		operands.clear();
	}
	
	private void setText(String text){
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	statusNote.setText(text);
		    }
		});
	}
	
	class Command {
		String address;
		String variable;
		Integer value;
		boolean valueSet = false;
		
		Command(String operand) throws Exception{			
			for(int i = 0; i < operand.length(); i++){
				if(operand.charAt(i) == '}'){
					this.address = operand.substring(1, i);
					operand = operand.substring(i + 1);
					break;
				}
			}
			if(!address.matches("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])")){
				throw new Exception("Incorrect IP address!");
			}
			
			if(operand.matches("\\.([a-zA-Z])\\w*=\\d+")){
				operand = operand.substring(1, operand.length());
				String[] opvalue = operand.split("=");
				this.variable = opvalue[0];
				this.value = Integer.parseInt(opvalue[1]);
				this.valueSet = true;
			} else if(operand.matches("\\.([a-zA-Z])\\w*")){
				operand = operand.substring(1, operand.length());
				this.variable = operand;
			} else {
				throw new Exception("Incorrect parameteres!");
			}	
		}
		
		public String getMsg(){
			if(valueSet){
				return this.variable + "=" + this.value;
			} else {
				return this.variable;
			}
		}		
	}
	
}
