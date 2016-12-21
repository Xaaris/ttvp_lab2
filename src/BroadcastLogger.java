import java.util.ArrayList;

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
}
