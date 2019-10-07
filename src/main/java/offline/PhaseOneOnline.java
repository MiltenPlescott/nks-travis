package offline;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

public class PhaseOneOnline {

	private static final char[] DIGITS = "0123456789".toCharArray();
	private static final char[] LOWER_ASCII = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//	private static final char[] UPPER_ASCII = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static final char[] WHOLE_ASCII = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static char[] CURRENT_JOB_ASCII;
	private static final String MSK = "80016";

	private static final int LEAK_SIZE = 100;
//	private static byte[][] leakedSaltsBin;
//	private static String[] leakedSaltsHex;
//	private static String[] leakedAuths;
//	private static String[] notSoLeakedPasswords;
	private static List<byte[]> candidateSaltsBin = new ArrayList<>(LEAK_SIZE);
	private static List<String> candidatePwds = new ArrayList<>(LEAK_SIZE);

	public static byte[][] realSaltsBin = new byte[LEAK_SIZE][];
	public static String[] realSaltsHex = new String[LEAK_SIZE];
	public static String[] realAuthsHex = new String[LEAK_SIZE];

	public static void run() {
//		generateLeakedDB();  // COMMENT OUT
		recursive(new StringBuilder(8));
		System.out.println("Number of candidates: " + candidateSaltsBin.size());
		verify();
	}

	private static void verify() {
		System.out.println("-------- CRACKED PASSWORDS -------\n\n");
		for (int leak = 0; leak < LEAK_SIZE; leak++) {
			for (int cand = 0; cand < candidateSaltsBin.size(); cand++) {
				if (Arrays.equals(realSaltsBin[leak], candidateSaltsBin.get(cand))) {
					String input = realSaltsHex[leak] + candidatePwds.get(cand);
					try {
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
						byte[] authBin = Arrays.copyOfRange(digest, 0, 8);
						String authHex = Hex.encodeHexString(authBin);
						if (authHex.equals(realAuthsHex[leak])) {
							System.out.println("" + candidatePwds.get(cand));
						}
					}
					catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}

				}
			}
		}
		System.out.println("\n\n-------------- END ---------------");
	}

//	private static void generateLeakedDB() {
//		Random rand = new Random(1);
//		leakedSaltsBin = new byte[LEAK_SIZE][];
//		leakedSaltsHex = new String[LEAK_SIZE];
//		leakedAuths = new String[LEAK_SIZE];
//		notSoLeakedPasswords = new String[LEAK_SIZE];
//		for (int i = 0; i < LEAK_SIZE; i++) {
//			StringBuilder builder = new StringBuilder(8);
//			builder.append(WHOLE_ASCII[rand.nextInt(WHOLE_ASCII.length)]);
//
//			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);
//			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);
//			builder.append(LOWER_ASCII[rand.nextInt(LOWER_ASCII.length)]);
//
//			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
//			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
//			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
//			builder.append(DIGITS[rand.nextInt(DIGITS.length)]);
//
//			String pwd = builder.toString();
//			System.out.println("pwd_" + (i + 1) + ": " + pwd);
//			notSoLeakedPasswords[i] = pwd;
//
//			try {
//				String input1 = MSK + pwd;
//				MessageDigest md1 = MessageDigest.getInstance("SHA-256");
//				byte[] digest1 = md1.digest(input1.getBytes(StandardCharsets.UTF_8));
//				byte[] saltBin = Arrays.copyOfRange(digest1, 0, 8);
//				leakedSaltsBin[i] = saltBin;
//				String saltHex = Hex.encodeHexString(saltBin);
//				leakedSaltsHex[i] = saltHex;
//				System.out.println("salt_" + (i + 1) + ": " + saltHex);
//				//
//				String input2 = saltHex + pwd;
//				MessageDigest md2 = MessageDigest.getInstance("SHA-256");
//				byte[] digest2 = md2.digest(input2.getBytes(StandardCharsets.UTF_8));
//				byte[] authBin = Arrays.copyOfRange(digest2, 0, 8);
//				String authHex = Hex.encodeHexString(authBin);
//				leakedAuths[i] = authHex;
//				System.out.println("auth_" + (i + 1) + ": " + authHex);
//			}
//			catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("----- GENERATED LEAKED DB -----");
//		System.out.println(" p w d  :     s a l t    :     a u t h    ");
//		for (int i = 0; i < LEAK_SIZE; i++) {
//			System.out.println("" + (i + 1));
//			System.out.println("" + notSoLeakedPasswords[i] + ":" + leakedSaltsHex[i] + ":" + leakedAuths[i]);
//		}
//		System.out.println("----- GENERATED LEAKED DB -----");
//	}

	// just generating passwords without any hashing/serialization takes 5.7 minutes
	private static StringBuilder recursive(StringBuilder currentStr) {
		if (currentStr.length() == 8) {
			hash(currentStr.toString());
		}
		else if (currentStr.length() == 0) {
			for (char c : CURRENT_JOB_ASCII) {
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
				if (Arrays.equals(saltBin, realSaltsBin[i])) {
					candidateSaltsBin.add(saltBin);
					candidatePwds.add(pwd);
				}
			}
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
