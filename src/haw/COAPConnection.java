package haw;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * 
 * @author Johannes & Erik
 * Class that interfaces with the LED over Coap
 *  
 */
public class COAPConnection {

	public static final int LED = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int PURPLE = 4;	
	private static COAPConnection coapConnection = null;
	
	private boolean[] ledStates;
	
	private CoapClient client;
	
	
	private COAPConnection(){
		
		ledStates = new boolean[]{false, false, false, false};
		client = new CoapClient(Constants.COAP_URI);
		
	}

	public static COAPConnection getCOAPConnection(){
		if( coapConnection == null ){
			coapConnection =  new COAPConnection();
		}
		
		return coapConnection;
	}
		
	
	public void setLEDto(int color){
		clearLED();
		
		if(color == RED || color == PURPLE){
			sendMEssage("r");
			ledStates[RED] = true;
		}
		if(color == GREEN){
			sendMEssage("g");
			ledStates[GREEN] = true;
		}
		if(color == BLUE || color == PURPLE){
			sendMEssage("b");
			ledStates[BLUE] = true;
		}
		
//		sendMEssage("1");
//		ledStates[LED] = true;
	}
	
	private void clearLED(){
//		if(ledStates[LED] == true){
			sendMEssage("0");
//			ledStates[LED] = false;
//		}
//		if(ledStates[RED] == true){
//			sendMEssage("r");
//			ledStates[RED] = false;	
//		}
//		if(ledStates[GREEN] == true){
//			sendMEssage("g");
//			ledStates[GREEN] = false;
//		}
//		if(ledStates[BLUE] == true){
//			sendMEssage("b");
//			ledStates[BLUE] = false;
//		}
	}
	
	private void sendMEssage(String message){
		client.put(message, MediaTypeRegistry.TEXT_PLAIN);
	}
}
