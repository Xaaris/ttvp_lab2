import java.net.MalformedURLException;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Main {

	public static void main(String[] args) throws MalformedURLException, ServiceException {
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.LOCAL_PROTOCOL);

		URL localURL = new URL(protocol + "://localhost:8080/");
		URL secondURL = new URL(protocol + "://localhost:8081/");
		URL thirdURL = new URL(protocol + "://localhost:8082/");
		
		ChordImpl chord1 = new ChordImpl();
		ChordImpl chord2 = new ChordImpl();
		ChordImpl chord3 = new ChordImpl();
		
		NotifyCallbackImpl notifyCallback1 = new NotifyCallbackImpl(chord1);
		NotifyCallbackImpl notifyCallback2 = new NotifyCallbackImpl(chord2);
		NotifyCallbackImpl notifyCallback3 = new NotifyCallbackImpl(chord3);
		
		chord1.setCallback(notifyCallback1);
		chord1.create(localURL);

		chord2.setCallback(notifyCallback2);
		chord2.join(secondURL, localURL);
		
		chord3.setCallback(notifyCallback3);
		chord3.join(thirdURL, secondURL);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		chord1.broadcast(null, null);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		chord2.broadcast(null, null);
//		chord3.broadcast(null, null);

	}

}
