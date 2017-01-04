import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Field {
	
	private FieldState state = FieldState.UNKNOWN;
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
		BigInteger middle = startID.toBigInteger().add(startID.distanceTo(endID).divide(new BigInteger("2"))).mod(Constants.MAXVALUE);
		middle = Util.shortenTo20Bytes(middle);
		return ID.valueOf(middle);
	}
	
	public boolean isIDInField(ID id) {
		return id.isInInterval(ID.valueOf(startID.toBigInteger().subtract(BigInteger.ONE)), ID.valueOf(endID.toBigInteger().add(BigInteger.ONE)));
//		
//		if (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0) {
//			return false;
//		}
//		return true;
	}

	public FieldState getState() {
		return state;
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
	
	
	

}
