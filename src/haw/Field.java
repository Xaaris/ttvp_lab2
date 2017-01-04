package haw;
import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Field {
	
	private FieldState state = FieldState.NOTSET;
	private ID startID;
	private ID endID; 
	
	public Field(ID startID, ID endID){
		this.startID = startID;
		this.endID = endID;
	}
	
	public Field(ID startID, ID endID, FieldState state){
		this.startID = startID;
		this.endID = endID;
		this.state = state;
	}
	
	public ID toID(){
		
		
		
		BigInteger middle = startID.toBigInteger().add(startID.distanceTo(endID).divide(new BigInteger("2")));
		middle = Util.sanitizeBigInt(middle);
		
//		System.out.println("In toID");
//		System.out.println("StartID: " + startID.toBigInteger());
//		System.out.println("endID: " + endID.toBigInteger());
//		System.out.println("distance: " + startID.distanceTo(endID));
//		System.out.println("distance/2: " + startID.distanceTo(endID).divide(new BigInteger("2")));
//		System.out.println("middle: " + middle);
		
		return ID.valueOf(middle);
	}
	
	public boolean isIDInField(ID id) {
		BigInteger lowerBound = Util.sanitizeBigInt(startID.toBigInteger().subtract(BigInteger.ONE));
		BigInteger upperBound = Util.sanitizeBigInt(endID.toBigInteger().add(BigInteger.ONE));
		return id.isInInterval(ID.valueOf(lowerBound), ID.valueOf(upperBound));
//		
//		if (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0) {
//			return false;
//		}
//		return true;
	}

	public FieldState getState() {
		if (state != null){
			return state;
		}else{
			return FieldState.NOTSET;
		}
		
	}

	public void setState(FieldState state) {
		this.state = state;
	}

	public ID getStartID() {
		return startID;
	}

	public ID getEndID() {
		return endID;
	}
	
	public void setStartID(ID newStartID){
		this.startID = newStartID;
	}
	
	public void setEndID(ID newEndID){
		this.endID = newEndID;
	}
	
	public String toString(){
		return "Field [" + startID.shortIDAsString() + " - " + endID.shortIDAsString() + "] State: " + getState();
	}
	
	public String shortRepresentation(){
		switch (getState()) {
		case SHIP:
			return "o";
		case SHIPWRECK:
			return "X";
		case WATER:
			return "~";
		case WATER_SHOT_AT:
			return "_";
		case UNKNOWN:
			return "?";
		case NOTSET:
			return "N";
		default:
			return "ERROR";
		}
	}
	
	
	

}
