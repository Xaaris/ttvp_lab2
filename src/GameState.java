import java.math.BigInteger;
import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;

public class GameState {

	private ArrayList<Player> listOfPlayers;
	private static GameState gameState = null;
	private BroadcastLogger broadcastLogger = null;

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

	public void updateGameState() {
		// TODO: implement, use broadcastlogger
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
			for (Player player : listOfPlayers) {
				if (player.isIDInPlayerSector(nodeID)) {
					ID startIDForNewPlayer = player.getStartID();
					Player newPlayer = new Player(startIDForNewPlayer, nodeID);
					addPlayer(newPlayer);
					player.changeSectorSize(ID.valueOf(nodeID.toBigInteger().add(BigInteger.ONE)), player.getEndID());
				}
			}
		}
	}

	public ArrayList<Player> getListOfPlayers() {
		return listOfPlayers;
	}

	public ArrayList<Player> getOtherPlayers() {
		ArrayList<Player> retList = new ArrayList<>();
		for (int i = 1; i < listOfPlayers.size(); i++) {
			retList.add(listOfPlayers.get(i));
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

}
