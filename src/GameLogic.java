import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class GameLogic {

	private static GameLogic gameLogic = null; // for singleton

	private static GameState gameState;
	private static ChordImpl chord;
	private HashSet<ID> playerIDs = new HashSet<>();

	private GameLogic() {
		gameState = GameState.getInstance();
	}

	public static GameLogic getInstance() {
		if (gameLogic == null) {
			gameLogic = new GameLogic();
		}
		return gameLogic;
	}

	public void initializeGame() {
		lookUpKnownNodeIDsAfterInitialization();
		System.out.println("Known Players:");
		for (ID id : playerIDs) {
			System.out.println(id.shortIDAsString());
		}
		for (ID id : playerIDs) {
			gameState.createPlayerFromID(id);
		}

		if (checkIfFirstPlayer()) {
			System.out.println("YAY, we are first!");

			// waiting for user confirmation to open the game.
			System.err.println("Press enter to start shooting.\n\n");
			new Scanner(System.in).nextLine();
			shoot();

		} else {
			System.out.println("\nWaiting for 'hightest player' to shoot...\n\n");
		}

	}

	/**
	 * looks up all nodes that are known after initialization. might not be all
	 * 
	 * @return list of node IDs
	 */
	private void lookUpKnownNodeIDsAfterInitialization() {

		playerIDs.add(chord.getID());
		if (chord.getPredecessorID() != null) {
			playerIDs.add(chord.getPredecessorID());
		}
		for (Node node : chord.getFingerTable()) {
			playerIDs.add(node.getNodeID());
		}
	}

	

	public Player getSelf() {
		for (Player player : gameState.getListOfPlayers()) {
			if (player.isIDInPlayerSector(chord.getID())) {
				return player;
			}
		}
		return null;
	}

	private boolean checkIfFirstPlayer() {

		BigInteger highestBigInt = Constants.MAXVALUE;
		ID highestID = ID.valueOf(highestBigInt);

		return getSelf().isIDInPlayerSector(highestID);
	}

	public void setChord(ChordImpl chord) {
		GameLogic.chord = chord;
	}
	
	public void handleHit(ID target){
		if (getSelf().isIDInPlayerSector(target)){
			Field targetField = getSelf().getFieldForID(target);
			if (targetField.getState() == FieldState.SHIP){
				targetField.setState(FieldState.SHIPWRECK);
				System.out.println("Our ship got hit! Ships left: " + getSelf().getNumberOfShipsLeft());
				gameState.updateGameState();
				chord.broadcast(target, true);
			}else{
				System.out.println("Shot missed!");
				chord.broadcast(target, false);
			}
			Util.delay(Constants.DELAY * 5);
			shoot();
		}
	}
	
	public void actOnReceivedBroadcast(BroadcastLogObject broadcast){
		BroadcastLogger.getInstance().addBroadcast(broadcast);
		gameState.updateGameState();
	}

	public void shoot() {
		ID target = getTarget();
		System.out.println("Shooting at: " + target.shortIDAsString());
		chord.retrieve(target);
	}

	public ID getTarget() {
		Random r = new Random();
		ID ranID = ID.valueOf(new BigInteger(160, r));
		System.out.println(ranID + " : " + ranID.getLength());
		// if its own fields or already shot at -> generate new random ID
		while (getSelf().isIDInPlayerSector(ranID) || gameState.getFieldForID(ranID).getState() != FieldState.UNKNOWN) {
			ranID = ID.valueOf(new BigInteger(160, r));
			System.out.println(ranID + " : " + ranID.getLength());
		}
		// get middle of field
		System.out.println(gameState.getFieldForID(ranID).toID() + " : " + gameState.getFieldForID(ranID).toID().getLength());
		return gameState.getFieldForID(ranID).toID();
	}

}
