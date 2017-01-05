package haw;
import de.uniba.wiai.lspi.chord.data.ID;

/**
 * 
 * @author Johannes & Erik
 * Object that represents one broadcast which can be logged by the broadcastlogger
 */
public class BroadcastLogObject {
	
	private ID source;
	private ID target;
	private boolean hit;
	
	public BroadcastLogObject(ID source, ID target, Boolean hit){
		this.source = source;
		this.target = target;
		this.hit = hit;
	}

	public ID getSource() {
		return source;
	}

	public ID getTarget() {
		return target;
	}

	public boolean isHit() {
		return hit;
	}
	
	public String toString(){
		return "[Broadcast: Source: " + source.shortIDAsString() + " target: " + target.shortIDAsString() + " hit: " + hit + "]";
	}

}
