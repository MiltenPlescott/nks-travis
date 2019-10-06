package main;

import java.util.Arrays;
import offline.PhaseOneOnline;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Main {

	// gradle run --args="str1 str2 str3 str4 etc" --> args is gonna have 5 strings in it
	// first arg - int: number of (UP TO) chars in a job (static in .yaml)
	// second arg - int: index (0-51) of the starting char (in the 52 char WHOLE_ASCII array) (variable in env matrix)
	// both github actions: 3 chars at a time in 17 jobs, 1 last char in 18th job
	// travis: 5 chars at a time in 10 jobs, 2 last chars in 11th job
	// locally/on school pc: don't do the whole thing at once (in case run time would be longer than an hour, but split it into 10-ish chars)
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int charsInJob = Integer.parseInt(args[0], 10);
		int from = Integer.parseInt(args[1], 10);

		if ((from + charsInJob) > PhaseOneOnline.WHOLE_ASCII.length) {
			charsInJob = PhaseOneOnline.WHOLE_ASCII.length - from;
		}

		PhaseOneOnline.CURRENT_JOB_ASCII = Arrays.copyOfRange(PhaseOneOnline.WHOLE_ASCII, from, from + charsInJob);

		System.out.println("Testing passwords beginning with: " + Arrays.toString(PhaseOneOnline.CURRENT_JOB_ASCII));

		setLeakedDB();
		PhaseOneOnline.run();

		long end = System.currentTimeMillis();
		System.out.println("Total running time (between first and last line in main) in ms: " + (end - start));
		System.out.println("Total running time (between first and last line in main) in s: " + ((end - start) / 1000));
		System.out.println("Total running time (between first and last line in main) in min: " + ((end - start) / 1000 / 60));
	}

	// replace:  ([a-zA-Z0-9:]+)
	// with:     "\1",
	private static void setLeakedDB() {
		String[] saltAuthPairs = {
			"d8b1d588520f8825:e50229dd67c98647",
			"cab52a0e8aa88ecb:06f0d8219a475bef",
			"4881f50b652b4b0a:6c0d742c5c0637bc",
			"8df3d8b6ffdcd294:bdc75027c0816772",
			"757feb64ee62793f:6059fcb48cd40535",
			"42f0ec856def414f:7d8589ba3971c1d2",
			"09431283d4989c21:238436460a6851f6",
			"2fdc45c3e577d51f:afa3ca4411045de8",
			"f1d44a83184b5cc0:440809796d3d66e7",
			"f221203d22287fba:568298368daef294",
			"fcea57cd40d64dc8:bd47511a4e7c2c2c",
			"ebdb583979437fce:abd0a40e0fc48dae",
			"818578c651e40d4b:c368c4e1524890b1",
			"80bc5362c884d415:0d4dac0e971f5d0e",
			"597740a30459d2a5:a8aee506302873b7",
			"4e08685a72aa5c39:cfe747e271f8e504",
			"9bb981ab2c64fbe4:04aaa69b009590ee",
			"39591619f886417a:07f49d41d2c6d1c4",
			"6b2dab195d58f6b5:1cac0123f5d5b414",
			"e52fb077b646c525:f5ce160a2685a3fd",
			"bb11d9d8203f3651:bfcbfcaf2169b8b6",
			"13baca19a5f679e0:80d90016ea594d24",
			"91455cc9cecbf496:571c26773fd6c11a",
			"5f7a237077f9d2d6:3de6da96e4fe5e74",
			"8538700f07cb26b1:2e2fef09e9b5fcba",
			"518d7c9db37e79f8:87f64375d6da1484",
			"d0c7a99c3f9986ce:c5519e2127a4d663",
			"8e2afda3dfdce58f:023c8e2a0c86545f",
			"ba8bcfda7379547a:446a0a1ff8d9c793",
			"3f9acfed3a276df7:b4b2f33bc8625935",
			"2dd2d5cadd7c9959:948818942176e09d",
			"cfef388b75a5a760:4fd7b1ea8bdc6248",
			"ad519449aaf23ca4:8d0358eb445ee478",
			"f625b1854c789ff5:e32cf3e58075b307",
			"30de11b30456737b:fa7a6dbde643d3f7",
			"a87898d62d97b313:c534f009c18bfc62",
			"33e27b3502cff1b3:0b864c7d5487cadb",
			"424dbb938134349c:0d0ae8a827ba83c8",
			"9299225380a10909:fdbe3814c4b3651b",
			"cd27d0e92b6c8273:9ffada59c6419719",
			"d87169685df602c7:e2e7bc3a5634d78e",
			"1df9f9ffe9e7b897:b570c40706f74e53",
			"b0a0480d4df5968b:dd011c459b621c23",
			"1055f9ca19221217:375e56f87db33dbb",
			"8c1999aea956daf6:a233b68791bf0765",
			"aba1f567bd97d6db:16c477817103f7df",
			"c02609c2da09a5de:81e6388334c0dcd4",
			"4e9673355379538c:c014c8369cf6c33d",
			"bef8a6651a14faab:6b79944fa226e7b8",
			"d1ffa627d36467fb:afbc683c4d3f6bde",
			"dfb981ba01c440d0:4f9ac9bbbf22f18a",
			"014f71132c0dde96:7e0a88d70fa20c5e",
			"e6265e6b8b95f164:f88b4d9d3864ee8a",
			"e0b75e58b9258886:f2a9f1c5093d44c4",
			"4e0baf390384136a:df77866f146cd718",
			"b0b7f6428f7962a4:0f33dc7484e8b741",
			"ea006c7d0f4ddbab:9b01ca0a60693fa5",
			"bee49c2ed45e79ec:bd51c7a358e91451",
			"dcb9f3db0139902a:b151004ef25d5b07",
			"620e5c54a9918413:5e1e9b43fd8cd1d4",
			"4e00c10fc4f3b3f9:65ff3386b0fc7aa5",
			"28bcd9c4df7c6ff1:54c02d9d44a45373",
			"90cd2fb9fa0aa235:090d7657cd7786e6",
			"b1989a666fe7a451:49b56d519aafc39b",
			"609f1130f8e1cfc4:021ff9282bfda651",
			"c74fd9068c93a19d:61e3830435f0b2d2",
			"cdb6e8f1cc4fd82b:bae051404c3ae4b0",
			"40c87f3cae7a0b9b:8ee8be2b81eb8bb5",
			"4c2f431deb3645d5:cb6eaa3d54a9e53b",
			"c6f9ce5950325f00:2652274fb854dfa3",
			"ccd7c724eff39726:9f38a76ab1e7c5f6",
			"ab680ca2d3fca606:8eaf377a102f07c5",
			"a6456b3f23e563d4:9fb454fec6657457",
			"63adc5f0ad88b876:75403f8712588e5b",
			"e7ffd7b0c339de06:6ebaba6ec37b6663",
			"008bd440c3311033:d0506d79da3d2224",
			"0b92152bb35a45a9:4b6cf27a186d707f",
			"394e97659260abf4:caef7c598ceb7174",
			"30147bde4caf55e4:39d5f144a5794f08",
			"277bf11fa705c049:528b28835d2c7c8c",
			"35ded2be87ce86bb:c5c0de2abebd5b4b",
			"f9f398d114cabf36:8f79be5420e18c22",
			"8a6741b93eca8f0c:b8d7d7f20dfbd792",
			"0e74b596fbab1300:42f63a8d8bea4700",
			"bb827739ae301113:e71c4e30f1c7a24d",
			"83313be5e1747948:e9605e5683b3c80e",
			"27386762bd736479:d8d7f7592b78c024",
			"1381dd166aeb4979:d8ec8cfd2d499a4b",
			"6534b02e83e5ed1a:06b3ff53d32d97dd",
			"d9519c0ea3b55e14:5e1f611071a34fbe",
			"7c73e9e331b886b4:45aff836a305ecbb",
			"4395615a72986a9f:530b149f0ad1e79d",
			"005326a31e38be59:218e643b9033b604",
			"2e90e66235b18ea9:3073e99626b04b37",
			"09e6a54740cd95f5:7bd77c97119da24c",
			"384e9a2ce8166e8a:b70024e4e12def00",
			"710153441cc7fc70:6a7132734f0318c8",
			"da81c9b599044646:22b5715dec5f475f",
			"52441e32b9f67aae:8cad504b9bef8275",
			"6122857e96f44d1b:f74fef20d3fc294d"
		};
		String[] split;
		for (int i = 0; i < saltAuthPairs.length; i++) {
			split = saltAuthPairs[i].split(":");
			try {
				PhaseOneOnline.realSaltsBin[i] = Hex.decodeHex(split[0]);
			}
			catch (DecoderException e) {
				e.printStackTrace();
			}
			PhaseOneOnline.realSaltsHex[i] = split[0];
			PhaseOneOnline.realAuthsHex[i] = split[1];
		}
	}

}
