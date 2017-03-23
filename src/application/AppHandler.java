package application;

import javafx.scene.control.Label;

public class AppHandler {
	private Label statusNote = null;
	public VariableList varList = new VariableList();
	public ThreadHandler threadHandler = new ThreadHandler();
	public MessageHandler message = new MessageHandler(statusNote);
	public Receiver receiver = new Receiver(message);
	
	AppHandler(Label label){
		this.statusNote = label;
	}
}
