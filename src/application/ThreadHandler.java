package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadHandler {
	private List<Thread> threadList = new ArrayList<Thread>();
	
	public void registerThread(Thread thread){
		this.threadList.add(thread);
	}
	
	public void killThreads() throws IOException{
		//killServer();
	}
	

	

}
