import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.URL;

public class Constants {
	
	public static final int MAXSHIPS = 10;
	public static final int NUMBEROFFIELDSINSECTOR = 100;
	public static final String PROTOCOL = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
	public static String SERVER_IP = "192.168.0.5";
	public static String SERVER_PORT = "8080";
	public static String CLIENT_IP = "192.168.0.6";
	public static String CLIENT_PORT = "8181";
	public static String joinOrCreate = "create"; // "join" and "create"
															// are valid
	public static final int DELAY = 200; //Global delay in ms
	public static final BigInteger MAXVALUE = new BigInteger("2").pow(160).subtract(BigInteger.ONE);

}
