import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class NotifyCallbackImpl implements NotifyCallback {
	
	private ChordImpl chordImpl = null;
	
	public NotifyCallbackImpl(ChordImpl chordImpl) {
		this.chordImpl = chordImpl;
	}

	@Override
	public void retrieved(ID target) {
		// TODO implement our logic
		chordImpl.broadcast(target, false);
		
	}

	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		System.out.println("Broadcast was seen by: " + chordImpl.getURL() +  " source: " + source + " target: " + target + " hit: " + hit);
		// TODO Log broadcast and act accordingly
		
	}

}
