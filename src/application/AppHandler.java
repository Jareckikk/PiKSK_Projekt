package application;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AppHandler {
	private Label statusNote = null;
	public VariableList varList = null;
	public ThreadHandler threadHandler = new ThreadHandler();
	public static MessageHandler message = null;
	public static Receiver receiver = null;;
	
	AppHandler(Label label, ListView listView){
		this.statusNote = label;
		varList = new VariableList(listView);
		message = new MessageHandler(this.statusNote, varList);
		receiver = new Receiver(message);
	}
	
}
