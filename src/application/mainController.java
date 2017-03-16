package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class mainController {
	AppHandler appHandler = new AppHandler();
	
	@FXML
    private Label filePathLabel;
	
	@FXML
	private Label ipLabel;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Thread socketServer = new Thread(new SocketServer());
    	socketServer.start();
    	
    }
	
    public void addVariable(){

    }
    
    public void send() throws IOException{
    	appHandler.sendMsg(this.inputCmd.getText());
    }
    
	
}
