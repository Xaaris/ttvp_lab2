package haw;
import java.util.ArrayList;

/**
 * 
 * @author Johannes & Erik
 * Class that logs all unique received broadcasts
 */
public class BroadcastLogger {

	private ArrayList<BroadcastLogObject> broadcastList;
	private static BroadcastLogger logger = null;

	private BroadcastLogger() {
		broadcastList = new ArrayList<>();
	}

	public static BroadcastLogger getInstance() {
		if (logger == null) {
			logger = new BroadcastLogger();
		}
		return logger;
	}

	public ArrayList<BroadcastLogObject> getBroadcastList() {
		return broadcastList;
	}
	
	public void addBroadcast(BroadcastLogObject broadcast){
		broadcastList.add(broadcast);
	}
	
	/**
	 * prints out all logged broadcasts
	 */
	public void printBroadcastHistory(){
		System.out.println("=================Broadcast History=================");
		for (BroadcastLogObject broadcastLogObject : broadcastList) {
			System.out.println(broadcastLogObject);
		}
		System.out.println("===================================================");
	}
}
