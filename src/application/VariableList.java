package application;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VariableList {
	HashMap<String, String> varMap = new HashMap<String, String>();
	
	private Map getMap(){
		return this.varMap;
	}
	
	public String addVariable(String varName, String varValue){
		if(!varName.matches("([a-zA-Z])\\w+")){
			return "Niepoprawna nazwa zmiennej!";
		}
		if(!varValue.matches("(\\d+)")){
			return "Niepoprawna wartoœæ zmiennej!";
		}
		this.varMap.put(varName, varValue);
		return "Dodano/Zmieniono zmienn¹.";
	}
	
	public ObservableList<String> getListViewItems(){
		ObservableList<String> items = FXCollections.observableArrayList();
		for(Map.Entry<String, String> entry : varMap.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    items.add(key + " = " + value);
		}
		return items;
	}
}
