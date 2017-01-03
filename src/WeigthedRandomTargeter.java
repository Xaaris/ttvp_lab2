import java.util.ArrayList;

import de.uniba.wiai.lspi.chord.data.ID;

public class WeigthedRandomTargeter {

    public static ID weightedRandomSelection(ArrayList<Player> otherPlayerList) {

        Player choosenPlayer = weightedRandomSelection(otherPlayerList);
        // Feld des Spielers bestimmen
        Field choosenField = rekursivFieldFinding(weightetSelectedPlayer.getPlayerFieldsStates());
        if (target != null) {
            return target.toID();
        }

        // sollte nie vorkommen, ist aber wenigstens failsave O.o
        return RandomTargeter.randomSelection(playerList, self);
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

    public static Field rekursivFieldFinding(ArrayList<Field> fields) {
        Field target = null;

        if (fields.size() >= 2) {
            // Blatt noch nicht erreicht
            int middle = fields.size() / 2;
            ArrayList<Field>[] subListArray = new (ArrayList<Field>)[2]; // 0 = links; 1 = rechts
            subListArray[0] = fields.subList(0, middle - 1);
            subListArray[1] = rightSubList = fields.subList(middle, fields.size() - 1);
            int gewichtLinks = fieldListGewichtung01(subListArray[0]);
            int gewichtRechts = fieldListGewichtung01((subListArray[1]));
            int lOderR = (Integer) Math.random() * (gewichtLinks + gewichtRechts);
            int first = 0;

            if (lOderR > gewichtLinks) {
                first = 1;
            }
            target = rekursivFieldFinding(subListArray[first]);
            if (target == null) {
                target = rekursivFieldFinding(subListArray[1 - first]);
            }
            return target;

        } else {
            // Ende des Baums (ein Blatt)
            if (fields.get(0).getState == FieldState.UNKNOWN) {
                target = fields.get(0);
            }
        }

        return target;

    }

    private static int fieldListGewichtung01(ArrayList<Field> fields) {
        int resu = 0;

        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getState() = FieldState.UNKNOWN) {
                value = 1;
            }
        }

        return resu;
    }
}