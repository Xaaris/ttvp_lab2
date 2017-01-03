import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class Main {

	public static void main(String[] args) {
	ChordImpl chord = ChordStart.createNetwork(args);
	GameLogic gameLogic = GameLogic.getInstance();
	gameLogic.setChord(chord);
	gameLogic.initializeGame();
	}

}
