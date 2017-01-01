import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;

public class NotifyCallbackImpl implements NotifyCallback {
	
	private GameLogic gameLogic = null;
	
	public NotifyCallbackImpl() {
		this.gameLogic = GameLogic.getInstance();
	}

	@Override
	public void retrieved(ID target) {
		System.out.println("Received a shot at: " + target.shortIDAsString());
		gameLogic.handleHit(target);
	}

	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		System.out.println("Broadcast was seen from source: " + source.shortIDAsString() + " target: " + target.shortIDAsString() + " hit: " + hit);
		// TODO Log broadcast and act accordingly
		BroadcastLogObject broadcastLogObj = new BroadcastLogObject(source, target, hit);
		gameLogic.actOnReceivedBroadcast(broadcastLogObj);
		
	}

}
