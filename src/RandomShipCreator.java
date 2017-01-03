
public class RandomShipCreator{

    public static FieldState[] generatRandomShipFormation(){
        FieldState[] flotte = new FieldState[Constants.NUMBEROFFIELDSINSECTOR];

        for(int i = 0; i < Constants.MAXSHIPS; i++){
            if(flotte[i] != FieldState.SHIP){ flotte[i] = FieldState.WATER; }
        }

        for(int i = 0; i < Constants.MAXSHIPS; i++){
            int randomFieldIndex = 0;

            do  {
                randomFieldIndex = (int)(Math.random() * flotte.length);
            }while(flotte[randomFieldIndex] == FieldState.SHIP);

            flotte[randomFieldIndex] = FieldState.SHIP;
        }

        return flotte;
    }
}