package application;

import javafx.scene.control.Label;

public class AppHandler {
	private Label statusNote = null;
	public VariableList varList = new VariableList();
	public ThreadHandler threadHandler = new ThreadHandler();
	public static MessageHandler message = null;
	public static Receiver receiver = null;;
	
	AppHandler(Label label){
		this.statusNote = label;
		message = new MessageHandler(this.statusNote, varList);
		receiver = new Receiver(message);
	}
	
}
