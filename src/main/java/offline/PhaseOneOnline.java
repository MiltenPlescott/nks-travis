package offline;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

public class PhaseOneOnline {

	private static final char[] DIGITS = "0123456789".toCharArray();
	private static final char[] LOWER_ASCII = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//	private static final char[] UPPER_ASCII = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final char[] WHOLE_ASCII = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final String MSK = "80016";

	private static long start = System.currentTimeMillis();
	private static long startNano = System.nanoTime();

	private static long end = System.currentTimeMillis();
	private static long endNano = System.nanoTime();

	private static final int LEAK_SIZE = 100;
	private static byte[][] leakedSalts;  // salts from leaked DB as bin (they leaked as hex but I'll convert them to bin anyway)
	private static List<String> candidateSalts = new ArrayList<>(100);  // hex
	private static List<String> candidatePwds = new ArrayList<>(100);

	public static void run() {
		generateLeakedDB();
		start = System.currentTimeMillis();
		startNano = System.nanoTime();
		recursive(new StringBuilder(8));
		System.out.println("Candidates (" + candidateSalts.size() + "): " + candidateSalts);
		System.out.println("Candidates: (" + candidatePwds.size() + ")" + candidatePwds);

		end = System.currentTimeMillis();
		endNano = System.nanoTime();
		System.out.println("Time (ms): " + (end - start));
		System.out.println("Time (ns): " + (endNano - startNano));
	}

	private static void generateLeakedDB() {
		Random rand = new Random();
		leakedSalts = new byte[LEAK_SIZE][];
		for (int i = 0; i < LEAK_SIZE; i++) {
			StringBuilder builder = new StringBuilder(8);
			builder.append(WHOLE_ASCII[rand.nextInt(WHOLE_ASCII.length)]);

			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);
			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);
			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);

			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);

			System.out.println("pwd_" + (i + 1) + ": " + builder.toString());

			String input = MSK + builder.toString();

			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
				byte[] saltBin = Arrays.copyOfRange(digest, 0, 8);
				leakedSalts[i] = saltBin;
				String saltHex = Hex.encodeHexString(saltBin);
				System.out.println("salt_" + (i + 1) + ": " + saltHex);
			}
			catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	// just generating passwords without any hashing/serialization takes 5.7 minutes
	private static StringBuilder recursive(StringBuilder currentStr) {
		if (currentStr.length() == 8) {
			hash(currentStr.toString());
//			if (currentStr.charAt(0) == 'b') {
//				System.out.println("Time (ms) at b: " + (System.currentTimeMillis() - start));
//				System.out.println("Time (ns) at b: " + (System.nanoTime() - startNano));
//				System.exit(0);
//			}
		}
		else if (currentStr.length() == 0) {
			for (char c : WHOLE_ASCII) {
				recursive(new StringBuilder(currentStr).append(c));
			}
		}
		else if (currentStr.length() >= 4) {
			for (char c : DIGITS) {
				recursive(new StringBuilder(currentStr).append(c));
			}
		}
		else if (currentStr.length() >= 1) {
			for (char c : LOWER_ASCII) {
				recursive(new StringBuilder(currentStr).append(c));
			}
		}
		return null;
	}

	private static void hash(String pwd) {
		String input = MSK + pwd;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
			byte[] saltBin = Arrays.copyOfRange(digest, 0, 8);
			for (int i = 0; i < LEAK_SIZE; i++) {
				if (Arrays.equals(saltBin, leakedSalts[i])) {
					String saltHex = Hex.encodeHexString(saltBin);
					candidateSalts.add(saltHex);
					candidatePwds.add(pwd);
					if (pwd.charAt(0) == 'b') {
						System.out.println("Time (ms) at b: " + (System.currentTimeMillis() - start));
						System.out.println("Time (ns) at b: " + (System.nanoTime() - startNano));
						System.exit(0);
					}
				}
			}
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
