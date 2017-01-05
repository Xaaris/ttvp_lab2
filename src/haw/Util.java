package haw;

import java.math.BigInteger;

/**
 * 
 * @author Johannes & Erik 
 * Utility Methods used throughout the project
 */
public class Util {

	/**
	 * pauses the Thread for ms milliseconds
	 * @param ms milliseconds 
	 */
	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * shortens a BigInteger to 21 Bytes
	 * @param bigInt
	 * @return converted BigInteger
	 */
	public static BigInteger shortenTo21Bytes(BigInteger bigInt) {
		if (bigInt.toByteArray().length > 21) {

			byte[] tooLong = bigInt.toByteArray();
			byte[] shortenedArray = new byte[21];
			for (int i = 1; i <= shortenedArray.length; i++) {
				byte tmp = tooLong[tooLong.length - i];
				shortenedArray[shortenedArray.length - i] = tmp;
			}
			// System.out.println();
			// for (byte b : tooLong) {
			// System.out.print(b + ", ");
			// }
			// System.out.println();
			// for (byte b : shortenedArray) {
			// System.out.print(b + ", ");
			// }
			// System.out.println();
			//
			// for (byte b : Constants.MAXVALUE.toByteArray()) {
			// System.out.print(b + ", ");
			// }
			// System.out.println();
			return new BigInteger(shortenedArray);
		} else {
			return bigInt;
		}
	}

	/**
	 * Makes sure BigInt is in bounds of 2^160
	 * @param bigInt
	 * @return cleaned BigInt
	 */
	public static BigInteger sanitizeBigInt(BigInteger bigInt) {
		// System.out.println("Signum of : " + bigInt + " was " +
		// bigInt.signum());
		if (bigInt.signum() < 0) {
			System.out.println("Found negativ big int: " + bigInt);
			return shortenTo21Bytes(Constants.MAXVALUE.add(bigInt));
		} else {
			BigInteger retInt = bigInt.mod(Constants.MAXVALUE);
			// System.out.println(bigInt);
			// System.out.println(retInt);
			retInt = shortenTo21Bytes(retInt);
			// System.out.println(retInt);
			return retInt;
		}
	}

}
