package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class mainController {
	
	@FXML
    private Label filePathLabel;
	
	@FXML
	private Label ipLabel;
	
	@FXML
	private Label statusNote;
	
	@FXML
	private TextField varName;
	
	@FXML
	private TextField varValue;
	
	@FXML
	private TextField inputCmd;
	
	@FXML
	private ListView varList;
	
	public AppHandler appHandler = null;
	
    public void initialize(){
    	try {
			this.ipLabel.setText("IP: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	this.appHandler = new AppHandler(this.statusNote, this.varList);
    	this.appHandler.receiver.start();    	   	
    }
	
    public void addVariable(){
    	statusNote.setText(this.appHandler.varList.addVariable(this.varName.getText(), this.varValue.getText()));
    	this.appHandler.varList.refresh();
    }
    
    public void send() throws IOException{
    	statusNote.setText("");
    	this.appHandler.message.send(this.inputCmd.getText());
    }

}
