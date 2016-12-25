import java.math.BigInteger;
import java.util.HashSet;
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
		for (ID id : playerIDs) {
			createPlayerFromID(id);
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
		playerIDs.add(chord.getPredecessorID());
		for (Node node : chord.getFingerTable()) {
			playerIDs.add(node.getNodeID());
		}
	}

	private void createPlayerFromID(ID nodeID) {
		if (gameState.getListOfPlayers().isEmpty()){
			ID startID = ID.valueOf(nodeID.toBigInteger().add(BigInteger.ONE)); //TODO: fails on wrap around point
			Player tmpPlayer = new Player(startID, nodeID);
			gameState.addPlayer(tmpPlayer);
		}else{
			for (Player player : gameState.getListOfPlayers()) {
				if (player.isIDInPlayerSector(nodeID)){
					ID startIDForNewPlayer = player.getStartID();
					Player newPlayer = new Player(startIDForNewPlayer, nodeID);
					gameState.addPlayer(newPlayer);
					player.changeSectorSize(ID.valueOf(nodeID.toBigInteger().add(BigInteger.ONE)), player.getEndID());
				}
			}
		}
		
	}
	
	public Player getSelf(){
		for (Player player : gameState.getListOfPlayers()) {
			if (player.isIDInPlayerSector(chord.getID())){
				return player;
			}
		}
		return null;
	}

	private boolean checkIfFirstPlayer() {

		BigInteger highestBigInt = new BigInteger("2").pow(160).subtract(BigInteger.ONE);
		ID highestID = ID.valueOf(highestBigInt);

		return getSelf().isIDInPlayerSector(highestID);
	}

	public void setChord(ChordImpl chord) {
		GameLogic.chord = chord;
	}
	
	public void shoot(){
		ID target = getTarget();
		//TODO: implement
	}
	
	public ID getTarget(){
		//TODO: implement
		return null;
	}

}
