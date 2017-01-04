package haw;
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
		if (bigInt.toByteArray().length > 21) {
			
			byte[] tooLong = bigInt.toByteArray();
			byte[] shortenedArray = new byte[21];
			for (int i = 1; i <= shortenedArray.length; i++) {
				byte tmp = tooLong[tooLong.length - i];
				shortenedArray[shortenedArray.length - i] = tmp;
			}
//			System.out.println();
//			for (byte b : tooLong) {
//				System.out.print(b + ", ");
//			}
//			System.out.println();
//			for (byte b : shortenedArray) {
//				System.out.print(b + ", ");
//			}
//			System.out.println();
//			
//			for (byte b : Constants.MAXVALUE.toByteArray()) {
//				System.out.print(b + ", ");
//			}
//			System.out.println();
			return new BigInteger(shortenedArray);
		} else {
			return bigInt;
		}
	}

	public static BigInteger sanitizeBigInt(BigInteger bigInt) {
//		System.out.println("Signum of : " + bigInt + " was " + bigInt.signum());
		if (bigInt.signum() < 0) {
//			System.out.println("Found negativ big int: " + bigInt);
			return shortenTo20Bytes(Constants.MAXVALUE.add(bigInt));
		} else {
			BigInteger retInt = bigInt.mod(Constants.MAXVALUE);
//			System.out.println(bigInt);
//			System.out.println(retInt);
			retInt = shortenTo20Bytes(retInt);
//			System.out.println(retInt);
			return retInt;
		}
	}

}
