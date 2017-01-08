package haw;


public class AdvancedShipCreator implements ShipCreator{
	
	public void createShips(Field[] playerFields){
		
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR; i++) {
			playerFields[i].setState(FieldState.WATER);
	}
		
		placeShipInArea(playerFields, 0, 5);
		placeShipInArea(playerFields, 95, 100);
		placeShipInArea(playerFields, 48, 53);
		
		for(int i = 2; i < Constants.MAXSHIPS; i++){
			placeShipInArea(playerFields, 0, 100);
		}
	}
	
	private void placeShipInArea(Field[] playerFields, int startIndex, int endIndex){ // endIndex ist exclusive
		
		int range = endIndex - startIndex;
		int randomNumber;
		int randomIndex;
		do{
			randomNumber = (int) (Math.random() * range);
			randomIndex = startIndex + randomNumber;
		}while(!checkRandomIndex(playerFields, randomIndex));
		
		playerFields[randomIndex].setState(FieldState.SHIP);
			
	}
	
	private boolean checkRandomIndex(Field[] playerFields, int randomIndex){
		if( randomIndex < 0 || randomIndex >= playerFields.length){
			return false; // Out of Bound
		}
		
		if(playerFields[randomIndex].getState() == FieldState.SHIP){
			return false;
		}
		
		return true;
	}
	
}
