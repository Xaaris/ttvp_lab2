package haw;
import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;

public class WeigthedRandomTargeter implements Targeter {

   

	@Override
	public ID getTarget() {
		ArrayList<Player> otherPlayerList = GameState.getInstance().getOtherPlayers();
		
		Player randomPlayer = weightedRandomPlayerSelection(otherPlayerList);
		
		Field randomField = recursivFieldFinding(randomPlayer.getPlayerFields(), 0, 100);
		
		return randomField.toID();
	}
	
	private static Field recursivFieldFinding(Field[] fields, int startIndex, int endIndex){
		
		Field target = null;
		
		if (endIndex - startIndex <= 1){

			 if (fields[startIndex].getState() == FieldState.UNKNOWN) {
				 target = fields[startIndex];
			 }

        }else {

            int middle = startIndex + (endIndex - startIndex / 2);
            int gewichtLinks = fieldGewichten(fields, startIndex, middle);
            int gewichtRechts = fieldGewichten(fields, middle, endIndex);
            int summe = gewichtLinks + gewichtRechts;
            int entscheidung = (int) (Math.random() * summe);

            if (entscheidung < gewichtLinks) {
                target = recursivFieldFinding(fields, startIndex, middle);
            } else {
                target = recursivFieldFinding(fields, middle, endIndex);
            }
            
            if(target == null){
            	if (entscheidung < gewichtLinks) {
            		target = recursivFieldFinding(fields, middle, endIndex);
                } else {
                	target = recursivFieldFinding(fields, startIndex, middle);
                }
            }

        }
		
		return target;
	}
	
	private static int fieldGewichten(Field[] fields, int startIndex, int endIndex) {
        int resu = 0;

        for (int i = startIndex; i < endIndex; i++) {
            if (fields[i].getState() == FieldState.UNKNOWN) {
                resu += 1;
            }
        }

        return resu;
    }
	
	
	public static Player weightedRandomPlayerSelection(ArrayList<Player> otherPlayerList){
        int[] bewertung = new int[otherPlayerList.size()];
        int summeBewertungen = 0;
        int randomPlayerIndex = 0;
        Player randomPlayer = null;

        // Spieler bewerten
        for (int i = 0; i < otherPlayerList.size(); i++) {
            bewertung[i] = playerGewichten(otherPlayerList.get(i));
            summeBewertungen += bewertung[i];
        }

        // Spieler anhand der Bewertung "zufällig" auswählen
        randomPlayerIndex = (int) (Math.random() * summeBewertungen);

        for (int i = 0; i < bewertung.length; i++) {
        	randomPlayerIndex -= bewertung[i];
            if (randomPlayerIndex <= 0) {
                randomPlayer = otherPlayerList.get(i);
                break;
            }
        }
        return randomPlayer;
    }
	
	 private static int playerGewichten(Player player) {
        int resu = 0;
        resu = (int) (player.getHitChanceForPlayer() * 1000);
        if (player.getNumberOfShipsLeft() == 2) {
            resu = 10;
        }
        return resu;
    }

}