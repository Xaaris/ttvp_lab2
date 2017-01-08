package haw;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

/**
 * 
 * @author Johannes & Erik
 * Class that represents the current state of the game 
 */
public class GameState {

	private ArrayList<Player> listOfPlayers = new ArrayList<>();
	private static GameState gameState = null;
	private BroadcastLogger broadcastLogger = null;
	private ChordImpl chord;
	private COAPConnection coapConnection;
	private HashSet<ID> playerIDs = new HashSet<>();
	
	private GameState() {
		listOfPlayers = new ArrayList<>();
		broadcastLogger = BroadcastLogger.getInstance();
		coapConnection = COAPConnection.getCOAPConnection();
	}

	/**
	 * returns singleton instance
	 * @return
	 */
	public static GameState getInstance() {
		if (gameState == null) {
			gameState = new GameState();
		}
		return gameState;
	}
	
	/**
	 * looks up all nodes that are known after initialization. might not be all
	 * 
	 * @return list of node IDs
	 */
	public void lookUpKnownNodeIDsAfterInitialization() {

		playerIDs.add(chord.getID());
		if (chord.getPredecessorID() != null) {
			playerIDs.add(chord.getPredecessorID());
		}
		for (Node node : chord.getFingerTable()) {
			playerIDs.add(node.getNodeID());
		}
	}
	
	/**
	 * creates known players from playersIDs list
	 */
	public void createKnownPlayers(){
		System.out.println("Known Players:");
		for (ID id : playerIDs) {
			System.out.println(id.shortIDAsString());
		}
		for (ID id : playerIDs) {
			createPlayerFromID(id);
		}
	}

	/**
	 * updates the gamestate according to the (last) passed broadcast
	 * also calls updateLED
	 * @param broadcast
	 */
	public void updateGameState(BroadcastLogObject broadcast) {
//		System.out.println("In updateGameState");
//		System.out.println("Broadcast: source: " + broadcast.getSource() +  " target: " + broadcast.getTarget() + " hit: " + broadcast.isHit());
		if (!playerIDs.contains(broadcast.getSource())){
			playerIDs.add(broadcast.getSource());
			System.err.println("NEW PLAYER JOINED WITH ID: " + broadcast.getSource());
			createPlayerFromID(broadcast.getSource());
			rebuildGameState();
		}
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
		updateLED();
		System.out.println(gameState);
	}
	
	/**
	 * rebuilds the gamestate from all logged broadcasts
	 * should be called after a new player joined
	 */
	private void rebuildGameState(){
		//reset other players
		for (Player otherPlayer : getOtherPlayers()) {
			otherPlayer.resetPlayer();
		}
		for (BroadcastLogObject broadcast : broadcastLogger.getBroadcastList()) {
			Player sourcePlayer = getPlayerForID(broadcast.getSource());
			if (!sourcePlayer.equals(getSelf())){
				Field targetedField = getFieldForID(broadcast.getTarget());
				if (broadcast.isHit()){
					targetedField.setState(FieldState.SHIPWRECK);
				}else{
					targetedField.setState(FieldState.WATER_SHOT_AT);
				}
			}
		}
	}

	/**
	 * updates LED with current state of the game
	 */
	private void updateLED() {
		
		Player self = getSelf();
		int shipsLeft = self.getNumberOfShipsLeft();
		
		if(shipsLeft == 10){
			// 100% Flottenst�rke 0=> LED green
			coapConnection.setLEDto(COAPConnection.GREEN);
		}else if(shipsLeft > 5){
			// >50% Flottenst�rke 0=> LED blue
			coapConnection.setLEDto(COAPConnection.BLUE);
		}else if(shipsLeft > 0){
			// > 0% Flottenst�rke 0=> LED purple
			coapConnection.setLEDto(COAPConnection.PURPLE);
		}else{
			// 0% Flottenst�rke   0=> LED red
			coapConnection.setLEDto(COAPConnection.RED);
		}
		
	}

	public void addPlayer(Player player) {

		if (!listOfPlayers.contains(player)) {
			listOfPlayers.add(player);
		}
	}
	
	/**
	 * creates a new player from nodeID
	 * if it is not the first player it calls the changesectorsize method on the player that owned 
	 * the sector where this nodeID lays
	 * @param nodeID
	 */
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
	
	/**
	 * returns the own player 
	 * @return own player
	 */
	public Player getSelf() {
		for (Player player : listOfPlayers) {
			if (player.isIDInPlayerSector(chord.getID())) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * returns a player for a given ID
	 * @param id
	 * @return
	 */
	public Player getPlayerForID(ID id){
		for (Player player : listOfPlayers) {
			if (player.isIDInPlayerSector(id)) {
				return player;
			}
		}
		System.out.println("ERROR: PLAYER FOR ID NOT FOUND. ID: " + id);
		return null;
	}

	/**
	 * returns all players but self
	 * @return
	 */
	public ArrayList<Player> getOtherPlayers() {
		ArrayList<Player> retList = new ArrayList<>();
		for (Player player : listOfPlayers) {
			retList.add(player);
		}
		retList.remove(getSelf());
		return retList;
	}

	/**
	 * returns a field for a given ID
	 * @param id
	 * @return
	 */
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
	
	/**
	 * returns true if any of the other players has no ships left
	 * @return
	 */
	public boolean gameOver(){
		for (Player otherPlayer : getOtherPlayers()) {
			if (otherPlayer.getNumberOfShipsLeft() < 1){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns true if last shot was from us and target has no ships left
	 * @param broadcast
	 * @param lastTarget
	 * @return
	 */
	public boolean weWon(BroadcastLogObject broadcast, Player lastTarget){
		if (lastTarget.isIDInPlayerSector(broadcast.getSource())) {
			Player targetPlayer = getPlayerForID(broadcast.getTarget());
			if (targetPlayer.getNumberOfShipsLeft() < 1) {
				return true;
			}
		}
		return false;
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
