import java.math.BigInteger;

public class Util {
	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static BigInteger shortenTo20Bytes(BigInteger bigInt) {
		if (bigInt.toByteArray().length > 20) {
			byte[] toLong = bigInt.toByteArray();
			byte[] shortenedArray = new byte[20];
			for (int i = 1; i <= shortenedArray.length; i++) {
				byte tmp = toLong[toLong.length - i];
				shortenedArray[shortenedArray.length - i] = tmp;
			}
			return new BigInteger(shortenedArray);
		} else {
			return bigInt;
		}
	}

}
