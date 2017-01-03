import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;

public class RandomTargeter implements Targeter{
	
	@Override
	public ID getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ID randomSelection(ArrayList<Player> otherPlayerList) {
		return randomFieldSelection(randomPlayerSelection(otherPlayerList).getPlayerFields()).toID();
	}

	public static Player randomPlayerSelection(ArrayList<Player> playerList) {
		Player randomPlayer = null;
		int randomPlayerIndex;
		randomPlayerIndex = (int) (Math.random() * playerList.size());
		randomPlayer = playerList.get(randomPlayerIndex);
		return randomPlayer;
	}

	public static Field randomFieldSelection(ArrayList<Field> fieldList) {
		Field randomField = null;
		int randomFieldIndex;

		ArrayList<Field> onlyUnknown = new ArrayList<>();
		for (Field field : fieldList) {
			if (field.getState() == FieldState.UNKNOWN) {
				onlyUnknown.add(field);
			}
		}

		randomFieldIndex = (int) (Math.random() * fieldList.size());
		randomField = fieldList.get(randomFieldIndex);

		return randomField;
	}

	

}