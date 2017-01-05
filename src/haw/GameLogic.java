package haw;
import java.util.Scanner;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

/**
 * 
 * @author Johannes & Erik
 * Methods for the Game logic
 */
public class GameLogic {

	private static GameLogic gameLogic = null; // for singleton

	private static GameState gameState;
	private ChordImpl chord;
	

	private Player lastTarget = null;
	
	private GameLogic() {
		gameState = GameState.getInstance();
	}

	/**
	 * singleton creation
	 * @return
	 */
	public static GameLogic getInstance() {
		if (gameLogic == null) {
			gameLogic = new GameLogic();
		}
		return gameLogic;
	}

	/**
	 * initializes the game by looking up  and creating known players
	 * afterwards checks if self is first player and in that case starts shooting
	 */
	public void initializeGame() {
		gameState.setChord(chord);
		// look up and create players
		gameState.lookUpKnownNodeIDsAfterInitialization();
		
		gameState.createKnownPlayers();
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
	 * returns a bool that says if biggest ID is in this players field
	 * @return
	 */
	private boolean checkIfFirstPlayer() {
		ID highestID = ID.valueOf(Constants.MAXVALUE);
		return getSelf().isIDInPlayerSector(highestID);
	}
	
	public Player getSelf(){
		return gameState.getSelf();
	}

	public void setChord(ChordImpl chord) {
		this.chord = chord;
	}

	/**
	 * updates the gamestate accordingly. 
	 * sends a broadcast back with the result of the shot
	 * shoots as long as game is not over yet
	 * @param target
	 */
	public void handleHit(ID target) {
		if (getSelf().isIDInPlayerSector(target)) {
			Field targetField = getSelf().getFieldForID(target);
			if (targetField.getState() == FieldState.SHIP) {
				targetField.setState(FieldState.SHIPWRECK);
				System.err.println("Our ship got hit! Ships left: " + getSelf().getNumberOfShipsLeft());
				chord.broadcast(target, true);
			} else {
				if (targetField.getState() == FieldState.WATER) {
					targetField.setState(FieldState.WATER_SHOT_AT);
				}
				System.out.println("Shot missed!");
				chord.broadcast(target, false);
			}
			if (!Constants.GAME_OVER) {
				shoot();
			}
		}else{
			System.err.println("RETRIEVE ON WRONG PLAYER!");
		}
	}

	/**
	 * logs received broadcast and updates gamestate. also checks if game is over
	 * @param broadcast
	 */
	public void actOnReceivedBroadcast(BroadcastLogObject broadcast) {
		BroadcastLogger.getInstance().addBroadcast(broadcast);
		gameState.updateGameState(broadcast);
		if(lastTarget != null){
			if (gameState.weWon(broadcast, lastTarget)){
				Constants.GAME_OVER = true;
				winning();
			}
		}
	}
	
	/**
	 * prints winning message -> endless loop
	 */
	public void winning(){
		for (int i = 0; i < 100; i++) {
			System.out.println();
			System.out.print("*");
		}
		System.err.println("\n\nWE WON!\n\nThe following player is dead:\n");
		for (Player otherPlayer : gameState.getOtherPlayers()) {
			if (otherPlayer.getNumberOfShipsLeft() < 1){
				System.out.println(otherPlayer + "\n");
			}
		}
		for (int i = 0; i < 100; i++) {
			System.out.print("*");
		}
		System.out.println();
		System.out.println();
		BroadcastLogger.getInstance().printBroadcastHistory();
		while (true){Util.delay(1000);} //Endless loop, game ended
	}

	/**
	 * searches for a target and shoots at it asynchronously
	 */
	public void shoot() {
		Targeter targeter = Constants.targeter;
		ID target = targeter.getTarget();
		
		lastTarget = gameState.getPlayerForID(target);
//		chord.retrieve(target); // sync retrive
		
		//async retrieve
		new Thread(new AsyncRetrieve(chord, target)).start();
	}
	
}
