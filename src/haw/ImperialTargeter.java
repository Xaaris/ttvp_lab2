package haw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import de.uniba.wiai.lspi.chord.data.ID;

public class ImperialTargeter implements Targeter {

	@Override
	public ID getTarget() {
		
		ArrayList<Player> otherPlayerList = GameState.getInstance().getOtherPlayers();
		Field targetField = null;
		
		// check for Player with 0 Ships
		Player zeroShipedPlayer = checkForPlayersWithZeroShips(otherPlayerList); // sollte eigentlich nicht vorkommen, aber wenn doch... Hehehehee
		
		if( zeroShipedPlayer == null){
		
			// check for Player with 2 Ships
			otherPlayerList = removeTwoShipPlayers(otherPlayerList);
			
			// check for Player with 1 Ship
			otherPlayerList = checkForPlayersWithOneShip(otherPlayerList);
			
			// make something up => find a target
			Player player = searchForTargetPlayer(otherPlayerList);
			
			 targetField = getTargetFieldFromPlayer(player);
			
		}else{
			targetField = zeroShipedPlayer.getPlayerFields()[2];
		}
		
		System.out.println("Target selected. Fire at will!");
		return targetField.toID();
	}
	
	private Player checkForPlayersWithZeroShips(ArrayList<Player> otherPlayerList){
		for (Player player : otherPlayerList) {
			if(player.getNumberOfShipsLeft() == 0){
				return player;
			}
		}
		return null;
	}
	
	
	private Field getTargetFieldFromPlayer(Player player) {
		Field targetField = null;
		Field[] playerFields = player.getPlayerFields();
		Random rnd = new Random();
		do {
			targetField = playerFields[rnd.nextInt(Constants.NUMBEROFFIELDSINSECTOR)];
		} while (targetField.getState() != FieldState.UNKNOWN);
		
		return targetField;
	}
	
	private Player searchForTargetPlayer(ArrayList<Player> otherPlayerList) {
		double hitchance = -1000;
		Player targetPlayer = null;
		Collections.shuffle(otherPlayerList);
		
		for (Player player : otherPlayerList) { // Player mit der besten Hitchance finden (vermutlich der schwächste?)
			if(player.getHitChanceForPlayer() > hitchance){
				hitchance = player.getHitChanceForPlayer();
				targetPlayer = player;
			}
		}
		
		return targetPlayer;
	}
		
	private ArrayList<Player> checkForPlayersWithOneShip(ArrayList<Player> otherPlayerList){
		ArrayList<Player> oneShipedPlayerList =  new ArrayList<>();
		for (Player player : otherPlayerList) {
			if(player.getNumberOfShipsLeft() == 1){
				oneShipedPlayerList.add(player);
			}
		}
		if(oneShipedPlayerList.isEmpty()){
			return otherPlayerList;
		}else{
			return oneShipedPlayerList;
		}
	}
	
	private ArrayList<Player> removeTwoShipPlayers(ArrayList<Player> otherPlayerList){
		ArrayList<Player> playersLeft = new ArrayList<>();
		for (Player player : otherPlayerList) {
			if(player.getNumberOfShipsLeft() != 2){
				playersLeft.add(player);
			}
		}
		
		if(playersLeft.isEmpty()){ 
			return otherPlayerList;
		}else{
			return playersLeft;
		}
	}

	
}
