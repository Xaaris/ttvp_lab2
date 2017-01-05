package haw;
import java.util.ArrayList;
import java.util.Random;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * 
 * @author Johannes & Erik
 * A targeter that selects a target at random
 */
public class RandomTargeter implements Targeter{
	
	static Random rnd = new Random();
	
	@Override
	public ID getTarget() {
		return randomSelection();
	}

	public static ID randomSelection() {
		Player rndPlayer = randomPlayerSelection(GameState.getInstance().getOtherPlayers());
		Field rndField = randomFieldSelection(rndPlayer.getPlayerFields());
//		System.out.println();
		System.out.println("Target selection:");
//		System.out.println("Self: " + GameState.getInstance().getSelf());
		
//		System.out.println("Selected " + rndField);
//		System.out.println("Fields of rnd Player:");
//		for (Field f : rndPlayer.getPlayerFields()) {
//			System.out.println(f);
//		}
		boolean isInSelf = GameState.getInstance().getSelf().isIDInPlayerSector(rndField.toID());
		System.out.println("is field " + rndField.toID().shortIDAsString() + " in self? " + isInSelf);
		if (isInSelf){
			System.err.println("ERROR: FRIENDLY FIRE!!");
			System.out.println("rndPlayer: " + rndPlayer);
			System.out.println("Selected " + rndField);
			System.out.println("Fields of rnd Player:");
			for (Field f : rndPlayer.getPlayerFields()) {
				System.out.println(f);
			}
		}
		
		return rndField.toID();
	}

	public static Player randomPlayerSelection(ArrayList<Player> otherPlayerList) {
		int randomIndex = rnd.nextInt(otherPlayerList.size());
		return otherPlayerList.get(randomIndex);
	}

	public static Field randomFieldSelection(Field[] fieldArr) {
		System.out.println("in randomFieldSelection");
		System.out.println("fieldArr.length "+ fieldArr.length);
		int randomIndex; 
		do{
			randomIndex = rnd.nextInt(fieldArr.length);
			System.out.println("Index: " + randomIndex + " " + fieldArr[randomIndex]);
		}while (fieldArr[randomIndex].getState() != FieldState.UNKNOWN);
		return fieldArr[randomIndex];
	}

}
