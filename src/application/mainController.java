package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class mainController {
	public static AppHandler appHandler = new AppHandler();
	
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

    public void initialize(){
    	try {
			this.ipLabel.setText("IP: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	
    	this.appHandler.receiver.start();    	
    	
    }
	
    public void addVariable(){
    	statusNote.setText(this.appHandler.varList.addVariable(this.varName.getText(), this.varValue.getText()));
    	this.refreshView();
    }
    
    public void send() throws IOException{
    	this.appHandler.message.send(this.inputCmd.getText());
    }
    
	private void refreshView(){
		this.varList.setItems(this.appHandler.varList.getListViewItems());
	}
}
