import java.math.BigInteger;
import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class GameState {

	private ArrayList<Player> listOfPlayers = new ArrayList<>();
	private static GameState gameState = null;
	private BroadcastLogger broadcastLogger = null;
	private ChordImpl chord;

	private GameState() {
		listOfPlayers = new ArrayList<>();
		broadcastLogger = BroadcastLogger.getInstance();
	}

	public static GameState getInstance() {
		if (gameState == null) {
			gameState = new GameState();
		}
		return gameState;
	}

	public void updateGameState(BroadcastLogObject broadcast) {
		
		// if source not in list of ids add it
		// if target not in list of ids add it
		Field targetedField = getFieldForID(broadcast.getTarget());
		if (broadcast.isHit()){
			targetedField.setState(FieldState.SHIPWRECK);
		}else{
			targetedField.setState(FieldState.WATER);
		}
		updateLED();
	}

	private void updateLED() {
		// TODO: implement (coap)
	}

	public void addPlayer(Player player) {

		if (!listOfPlayers.contains(player)) {
			listOfPlayers.add(player);
		}
	}
	
	public void createPlayerFromID(ID nodeID) {
		if (listOfPlayers.isEmpty()) {
			// TODO: fails on wrap around point, very unlikely
			ID startID = ID.valueOf(nodeID.toBigInteger().add(BigInteger.ONE));
			Player tmpPlayer = new Player(startID, nodeID);
			addPlayer(tmpPlayer);
		} else {
			ID startIDForNewPlayer = null;
			for (Player player : listOfPlayers) {
				if (player.isIDInPlayerSector(nodeID)) {
					startIDForNewPlayer = player.getStartID();
					player.changeSectorSize(ID.valueOf(nodeID.toBigInteger().add(BigInteger.ONE)), player.getEndID());
				}
			}
			Player newPlayer = new Player(startIDForNewPlayer, nodeID);
			addPlayer(newPlayer);
		}
	}

	public ArrayList<Player> getListOfPlayers() {
		return listOfPlayers;
	}
	
	public Player getSelf() {
		for (Player player : listOfPlayers) {
			if (player.isIDInPlayerSector(chord.getID())) {
				return player;
			}
		}
		return null;
	}

	public ArrayList<Player> getOtherPlayers() {
		ArrayList<Player> retList = new ArrayList<>();
		for (Player player : listOfPlayers) {
			retList.add(player);
		}
		retList.remove(getSelf());
		System.out.println();
		System.out.println("Self: " + getSelf());
		for (Player player : retList) {
			System.out.println("Other player: " + player);
		}
		
		return retList;
	}

	public Field getFieldForID(ID id) {
		for (Player player : listOfPlayers) {
			if (player.isIDInPlayerSector(id)) {
				return player.getFieldForID(id);
			}
		}
		return null;
	}
	
	public void setChord(ChordImpl chord) {
		this.chord = chord;
	}

}
