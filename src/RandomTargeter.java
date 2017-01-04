import java.util.ArrayList;
import java.util.Random;

import de.uniba.wiai.lspi.chord.data.ID;

public class RandomTargeter implements Targeter{
	
	static Random rnd = new Random();
	
	@Override
	public ID getTarget() {
		return randomSelection();
	}

	public static ID randomSelection() {
		Player rndPlayer = randomPlayerSelection(GameState.getInstance().getOtherPlayers());
		Field rndField = randomFieldSelection(rndPlayer.getPlayerFields());
		System.out.println();
		System.out.println("Target selection:");
		System.out.println("Self: " + GameState.getInstance().getSelf());
		System.out.println("rndPlayer: " + rndPlayer);
		System.out.println("Selected " + rndField);
		System.out.println("Fields of rnd Player:");
		for (Field f : rndPlayer.getPlayerFields()) {
			System.out.println(f);
		}
		boolean isInSelf = GameState.getInstance().getSelf().isIDInPlayerSector(rndField.toID());
		System.out.println("is field in self? " + isInSelf);
		
		return rndField.toID();
	}

	public static Player randomPlayerSelection(ArrayList<Player> otherPlayerList) {
		int randomIndex = rnd.nextInt(otherPlayerList.size());
		return otherPlayerList.get(randomIndex);
	}

	public static Field randomFieldSelection(Field[] fieldArr) {
		int randomIndex = rnd.nextInt(fieldArr.length);
		while (fieldArr[randomIndex].getState() != FieldState.UNKNOWN){
			randomIndex = rnd.nextInt(fieldArr.length);
		}
		return fieldArr[randomIndex];
	}

}
