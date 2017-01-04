package haw;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Scanner;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class GameLogic {

	private static GameLogic gameLogic = null; // for singleton

	private static GameState gameState;
	private ChordImpl chord;
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
		gameState.setChord(chord);
		// look up and create players
		lookUpKnownNodeIDsAfterInitialization();
		System.out.println("Known Players:");
		for (ID id : playerIDs) {
			System.out.println(id.shortIDAsString());
		}
		for (ID id : playerIDs) {
			gameState.createPlayerFromID(id);
		}
		// populate self with ships
		ShipCreator shipCreator = Constants.shipCreator;
		shipCreator.createShips(getSelf().getPlayerFields());

		if (checkIfFirstPlayer()) {
			Util.delay(Constants.DELAY * 5);
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

	private boolean checkIfFirstPlayer() {

		BigInteger highestBigInt = Constants.MAXVALUE;
		ID highestID = ID.valueOf(highestBigInt);

		return getSelf().isIDInPlayerSector(highestID);
	}
	
	public Player getSelf(){
		return gameState.getSelf();
	}

	public void setChord(ChordImpl chord) {
		this.chord = chord;
	}

	public void handleHit(ID target) {
		if (getSelf().isIDInPlayerSector(target)) {
			Field targetField = getSelf().getFieldForID(target);
			if (targetField.getState() == FieldState.SHIP) {
				targetField.setState(FieldState.SHIPWRECK);
				System.out.println("Our ship got hit! Ships left: " + getSelf().getNumberOfShipsLeft());
				chord.broadcast(target, true);
			} else {
				if (targetField.getState() == FieldState.WATER) {
					targetField.setState(FieldState.WATER_SHOT_AT);
				}
				System.out.println("Shot missed!");
				chord.broadcast(target, false);
			}
			shoot();
		}else{
			System.err.println("RETRIEVE ON WRONG PLAYER!");
		}
	}

	public void actOnReceivedBroadcast(BroadcastLogObject broadcast) {
		BroadcastLogger.getInstance().addBroadcast(broadcast);
		gameState.updateGameState(broadcast);
	}

	public void shoot() {
		Targeter targeter = Constants.targeter;
		ID target = targeter.getTarget();
		
		
//		chord.retrieve(target);
		
		new Thread(new AsyncRetrieve(chord, target)).start();
	}
	
}
