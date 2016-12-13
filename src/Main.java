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
		
		Chord chord1 = new ChordImpl();
		Chord chord2 = new ChordImpl();
		Chord chord3 = new ChordImpl();
		
		NotifyCallback notifyCallback = new NotifyCallback() {

			@Override
			public void retrieved(ID target) {
			}

			@Override
			public void broadcast(ID source, ID target, Boolean hit) {
			}
		};
		
		chord1.setCallback(notifyCallback);
		chord1.create(localURL);

		chord2.setCallback(notifyCallback);
		chord2.join(secondURL, localURL);
		
		chord3.setCallback(notifyCallback);
		chord3.join(thirdURL, secondURL);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		chord1.broadcast(null, null);
//		chord2.broadcast(null, null);
//		chord3.broadcast(null, null);

	}

}
