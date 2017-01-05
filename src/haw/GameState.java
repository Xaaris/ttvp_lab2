package haw;
import java.math.BigInteger;
import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class GameState {

	private ArrayList<Player> listOfPlayers = new ArrayList<>();
	private static GameState gameState = null;
	private BroadcastLogger broadcastLogger = null;
	private ChordImpl chord;
	private COAPConnection coapConnection;
	
	private GameState() {
		listOfPlayers = new ArrayList<>();
		broadcastLogger = BroadcastLogger.getInstance();
		coapConnection = COAPConnection.getCOAPConnection();
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
			if (targetedField.getState() == FieldState.UNKNOWN) {
				targetedField.setState(FieldState.WATER_SHOT_AT);
			}else if (targetedField.getState() == FieldState.WATER) {
				targetedField.setState(FieldState.WATER_SHOT_AT);
			}
			
		}
//		updateLED();
		System.out.println(gameState);
	}

	private void updateLED() {
		
		Player self = getSelf();
		int shipsLeft = self.getNumberOfShipsLeft();
		
		if(shipsLeft == 10){
			// 100% Flottenst�rke 0=> LED green
			coapConnection.setLEDto(coapConnection.GREEN);
		}else if(shipsLeft > 5){
			// >50% Flottenst�rke 0=> LED blue
			coapConnection.setLEDto(coapConnection.BLUE);
		}else if(shipsLeft > 0){
			// > 0% Flottenst�rke 0=> LED purple
			coapConnection.setLEDto(coapConnection.PURPLE);
		}else{
			// 0% Flottenst�rke   0=> LED red
			coapConnection.setLEDto(coapConnection.RED);
		}
		
	}

	public void addPlayer(Player player) {

		if (!listOfPlayers.contains(player)) {
			listOfPlayers.add(player);
		}
	}
	
	public void createPlayerFromID(ID nodeID) {
		if (listOfPlayers.isEmpty()) {
			BigInteger startIDAsBigInt = Util.sanitizeBigInt(nodeID.toBigInteger().add(BigInteger.ONE));
			ID startID = ID.valueOf(startIDAsBigInt);
			Player tmpPlayer = new Player(startID, nodeID);
			addPlayer(tmpPlayer);
		} else {
			ID startIDForNewPlayer = null;
			for (Player player : listOfPlayers) {
				if (player.isIDInPlayerSector(nodeID)) {
					startIDForNewPlayer = player.getStartID();
					BigInteger newStartIDAsBigInt = Util.sanitizeBigInt(nodeID.toBigInteger().add(BigInteger.ONE));
					player.changeSectorSize(ID.valueOf(newStartIDAsBigInt), player.getEndID());
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
//		System.out.println();
//		System.out.println("Self: " + getSelf());
//		for (Player player : retList) {
//			System.out.println("Other player: " + player);
//		}
		
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
	
	public String toString(){
		int width = 102;
		String retStr = "";
		for (int i = 0; i < (width/2)-4; i++) {
			retStr += "=";
		}
		retStr += "GAMESTATE";
		for (int i = 0; i < (width/2)-5; i++) {
			retStr += "=";
		}
		retStr += "\n";
		for (Player player : listOfPlayers) {
			retStr += player.toString();
		}
		for (int i = 0; i < width; i++) {
			retStr += "=";
		}
		return retStr;
	}

}
