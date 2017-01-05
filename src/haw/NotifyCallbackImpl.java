package haw;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;

/**
 * 
 * @author Johannes & Erik
 * Implementation of the NotifyCallback that notifies the gamelogic
 */
public class NotifyCallbackImpl implements NotifyCallback {
	
	private GameLogic gameLogic = null;
	
	public NotifyCallbackImpl() {
		this.gameLogic = GameLogic.getInstance();
	}

	/**
	 * Calls the gameLogic to handle the potential hit
	 */
	@Override
	public void retrieved(ID target) {
		System.out.println("Received a shot at: " + target.shortIDAsString());
		gameLogic.handleHit(target);
	}
	
	/**
	 * Calls the gameLogic to act on the received broadcast
	 */
	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		System.out.println("Broadcast was seen from source: " + source.shortIDAsString() + " target: " + target.shortIDAsString() + " hit: " + hit);
		BroadcastLogObject broadcastLogObj = new BroadcastLogObject(source, target, hit);
		gameLogic.actOnReceivedBroadcast(broadcastLogObj);
		
	}

}
