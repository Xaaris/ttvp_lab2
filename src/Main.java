import java.net.MalformedURLException;
import java.util.Scanner;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;




public class Main {
	
    // constants for config
    private static final String PROTOCOL = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
    private static final String SERVER_IP = "192.168.0.5";
    private static final String SERVER_PORT = "8080";
    private static final String CLIENT_IP = "192.168.0.13";
    private static final String CLIENT_PORT = "8181";
    private static final String joinOrCreate = "create"; // "join" and "create" are valid
    
    

	public static void main(String[] args) throws MalformedURLException, ServiceException {
		PropertiesLoader.loadPropertyFile();
		
//		localTest();
		runGame();
		
		

	}
	
	private static void runGame(){

		URL localURL = null;
        try {
            localURL = new URL(PROTOCOL + "://" + SERVER_IP + ":" + SERVER_PORT + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
		
		ChordImpl chordImpl = new ChordImpl();
		
		NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl(chordImpl);
		
		chordImpl.setCallback(notifyCallback);
		
		if (joinOrCreate.equals("create")) {
			try {
	            chordImpl.create(localURL);
	        } catch (ServiceException e) {
	            throw new RuntimeException("Could not create DHT!", e);
	        }
        } else if (joinOrCreate.equals("join")) {
        	URL clientURL = null;
        	try {
                clientURL = new URL(PROTOCOL + "://" + CLIENT_IP + ":" + CLIENT_PORT + "/");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        	
        	try {
                chordImpl.join(clientURL, localURL);
            } catch (ServiceException e) {
                throw new RuntimeException("Could not join DHT!", e);
            }
        } else {
            System.out.println("ERROR: choose if you want to be server or client!");
        }
		
		
        System.out.println("Chord listens on: " + localURL);
		

        System.out.println("\n\n\n\nPress enter to start\n");
		new Scanner(System.in).nextLine();
	}
	
	private static void localTest() throws MalformedURLException, ServiceException{
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
