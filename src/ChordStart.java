import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class ChordStart {
	
	public ChordStart(){
		
	}

	public static ChordImpl createLocalNetwork() {

		PropertiesLoader.loadPropertyFile();
		
		ArrayList<ChordImpl> chordList = new ArrayList<>();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.LOCAL_PROTOCOL);

		URL localURL = null;

		try {
			localURL = new URL(protocol + "://localhost:" + Constants.SERVER_PORT + "/");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		ChordImpl chordLeader = new ChordImpl();

		NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl(chordLeader);

		chordLeader.setCallback(notifyCallback);
		try {
			chordLeader.create(localURL);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		chordList.add(chordLeader);
		System.out.println("New game created. Bootstrap node with ID: " + chordLeader.getID().shortIDAsString());
		
		for (int i = 1; i < 5; i++) {
			
			delay(200);
			
			ChordImpl chordJoiner = new ChordImpl();
			NotifyCallbackImpl notifyCallbackJoiner = new NotifyCallbackImpl(chordJoiner);
			chordJoiner.setCallback(notifyCallbackJoiner);
			URL joinURL = null;
			try {
				joinURL = new URL(protocol + "://localhost:808" + i + "/");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			try {
				chordJoiner.join(joinURL, localURL);
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			chordList.add(chordJoiner);
			System.out.println("New node joined with ID: " + chordJoiner.getID().shortIDAsString());
			
	
		}

		Random r = new Random();
		BigInteger ranID = new BigInteger(160, r);
		delay(2000);
		
		for (ChordImpl tmpChord : chordList) {
			System.out.println("===============");
			System.out.println("Fingertable of: " + tmpChord.getID().shortIDAsString());
			for (Node finger : tmpChord.getFingerTable()) {
				System.out.println(finger.getNodeID().shortIDAsString());
			}
			System.out.println("===============");
		}
		chordLeader.broadcast(ID.valueOf(ranID), false);
		
		delay(3000);
		BroadcastLogger.getInstance().printBroadcastHistory();
		
		return chordLeader;
		
	}

	public static ChordImpl createNetwork() {
		
		PropertiesLoader.loadPropertyFile();
		
		URL localURL = null;
		try {
			localURL = new URL(Constants.PROTOCOL + "://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT + "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		ChordImpl chordImpl = new ChordImpl();

		NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl(chordImpl);

		chordImpl.setCallback(notifyCallback);

		if (Constants.joinOrCreate.equals("create")) {
			try {
				chordImpl.create(localURL);
			} catch (ServiceException e) {
				throw new RuntimeException("Could not create DHT!", e);
			}
		} else if (Constants.joinOrCreate.equals("join")) {
			URL clientURL = null;
			try {
				clientURL = new URL(Constants.PROTOCOL + "://" + Constants.CLIENT_IP + ":" + Constants.CLIENT_PORT + "/");
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
		
		return chordImpl;

	}
	
	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}