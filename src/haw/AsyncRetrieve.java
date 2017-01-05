package haw;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;

/**
 * 
 * @author Johannes & Erik
 * Asynchronously shoots after small delay
 */
public class AsyncRetrieve implements Runnable{
	
	Chord chord = null;
	ID target = null;
	
	public AsyncRetrieve(Chord chord, ID target) {
		this.chord = chord;
		this.target = target;
	}
	
	public void run() {
        
		Util.delay(Constants.DELAY * 5);
		try {
			System.out.println("Shooting at: " + target.shortIDAsString());
			chord.retrieve(target);
		} catch (ServiceException e) {
			System.err.println("Shooting failed!");
			e.printStackTrace();
		}
	}

}
