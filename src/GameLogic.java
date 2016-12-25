import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class GameLogic {

	private static GameLogic gameLogic = null; // for singleton

	private static GameState gameState;
	private static ChordImpl chord;

	private GameLogic() {
		gameState = GameState.getInstance();
	}

	public static GameLogic getInstance() {
		if (gameLogic == null) {
			gameLogic = new GameLogic();
		}
		return gameLogic;
	}
	
	public void initializeGame(){
		Player self = createOwnPlayer();
		gameState.addPlayer(self);
		boolean firstPlayer = checkIfFirstPlayer();
		
		//TODO: implement
	}
	
	private Player createOwnPlayer(){
		ID predecessorID = chord.getPredecessorID();
		ID startID = ID.valueOf(predecessorID.toBigInteger().add(BigInteger.ONE));
		return new Player(startID, chord.getID());
	}

	private boolean checkIfFirstPlayer() {

		Player self = gameState.getSelf();
		BigInteger highestBigInt = new BigInteger("2").pow(160).subtract(BigInteger.ONE);
		ID highestID = ID.valueOf(highestBigInt);

		return self.isIDInPlayerSector(highestID);
	}
	
	public void setChord(ChordImpl chord){
		this.chord = chord;
	}
	
	

}
