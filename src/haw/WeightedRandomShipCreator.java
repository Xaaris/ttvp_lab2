package haw;

public class WeightedRandomShipCreator implements ShipCreator{

	
	@Override
	public void createShips(Field[] playerFields) {
		
		FieldState[] fieldstates = WeightedgeneratRandomShipFormation();
		
		for(int i = 0; i < playerFields.length; i++){
			playerFields[i].setState(fieldstates[i]);
		}
		
	}
	
    public static FieldState[] WeightedgeneratRandomShipFormation(){

        FieldState[] sea = new FieldState[Constants.NUMBEROFFIELDSINSECTOR];

        for(int i = 0; i < sea.length; i++){
        	sea[i] = FieldState.WATER;
        }

        for(int i = 0; i < Constants.MAXSHIPS; i++){
            int randomFieldIndex = 0;
            do{
                randomFieldIndex = getRecursivRandomField(sea, 0, sea.length);
            }while(sea[randomFieldIndex] == FieldState.SHIP);
        }

        return sea;
    }

    //startIndex ist includiv; endIndex ist exclusiv
    private static int getRecursivRandomField(FieldState[] flotte, int startIndex, int endIndex) {

        if (endIndex - startIndex <= 1){

            return startIndex;

        }else {

            int middle = startIndex + (endIndex - startIndex / 2);
            int gewichtLinks = calculateWeight(flotte, startIndex, middle);
            int gewichtRechts = calculateWeight(flotte, middle, endIndex);
            int summe = gewichtLinks + gewichtRechts;
            int entscheidung = (int) (Math.random() * summe);

            if (entscheidung < gewichtLinks) {
                return getRecursivRandomField(flotte, startIndex, middle);
            } else {
                return getRecursivRandomField(flotte, middle, endIndex);
            }

        }


    }

    private static int calculateWeight(FieldState[] flotte, int startIndex, int endIndex){
        int gewicht = 0;
        for(int i = startIndex; i < endIndex; i++){
            if(flotte[i] == FieldState.WATER){ gewicht++; }
        }
        return gewicht;
    }



}