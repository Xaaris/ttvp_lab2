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

		NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl();

		chordLeader.setCallback(notifyCallback);
		try {
			chordLeader.create(localURL);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		chordList.add(chordLeader);
		System.out.println("New game created. Bootstrap node with ID: " + chordLeader.getID().shortIDAsString());
		
		for (int i = 1; i < 5; i++) {
			
			Util.delay(Constants.DELAY);
			
			ChordImpl chordJoiner = new ChordImpl();
			NotifyCallbackImpl notifyCallbackJoiner = new NotifyCallbackImpl();
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
		Util.delay(Constants.DELAY * 20);
		
		for (ChordImpl tmpChord : chordList) {
			System.out.println("===============");
			System.out.println("Fingertable of: " + tmpChord.getID().shortIDAsString());
			for (Node finger : tmpChord.getFingerTable()) {
				System.out.println(finger.getNodeID().shortIDAsString());
			}
			System.out.println("===============");
		}
		chordLeader.broadcast(ID.valueOf(ranID), false);
		
		Util.delay(Constants.DELAY * 20);
		BroadcastLogger.getInstance().printBroadcastHistory();
		
		return chordLeader;
		
	}

	public static ChordImpl createNetwork(String[] args) {
		
		if (args.length == 1){
		Constants.joinOrCreate = args[0];
		}else if (args.length == 2){
			Constants.joinOrCreate = args[0];
			Constants.SERVER_IP = args[1];
		}else if (args.length == 3){
			Constants.joinOrCreate = args[0];
			Constants.SERVER_IP = args[1];
			Constants.SERVER_PORT = args[2];
		}else if (args.length == 4){
			Constants.joinOrCreate = args[0];
			Constants.SERVER_IP = args[1];
			Constants.SERVER_PORT = args[2];
			Constants.CLIENT_IP = args[3];
		}else if (args.length == 5){
			Constants.joinOrCreate = args[0];
			Constants.SERVER_IP = args[1];
			Constants.SERVER_PORT = args[2];
			Constants.CLIENT_IP = args[3];
			Constants.CLIENT_PORT = args[4];
		}
		
		PropertiesLoader.loadPropertyFile();
		
		URL localURL = null;
		try {
			localURL = new URL(Constants.PROTOCOL + "://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT + "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		ChordImpl chordImpl = new ChordImpl();

		NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl();

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
				System.out.println("Local URL: " + clientURL);
				System.out.println("Bootstrap URL: " + localURL);
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
	
	

}
