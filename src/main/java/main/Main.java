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
		System.out.println("Main args: " + Arrays.toString(args));
		if (args.length != 2) {
			System.err.println("Main received " + args.length + " args, instead of required 2.");
			System.exit(0);
		}
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
			"f328cfd4c22d8a67:97709acef4d83127",
			"2f8fa9b7f8965515:2ddf236915c9406d",
			"b59d280fae049b6e:42e5c585127bfaaa",
			"27a7fb2620e48e35:f7cc59a2b3346262",
			"6d6304620510ace5:8abd30496c109d22",
			"174e15b387154b4d:cd7497cd4f660b31",
			"118451fe212aad88:272e42276b30d71b",
			"e8b907087db32447:9719cdb5420431b3",
			"42cd5464ffe8380f:62a51fa18c57444e",
			"5a6928dff3ee327d:a36f6ed22bda7634",
			"aaa2dacec2d065a3:425701d8b67a849c",
			"c1a742a2459b4e74:bc5ebaa584c1749b",
			"0d4e226b9c2fbcb6:93ff8cf68121cf6a",
			"974972be938e8830:684f4eacaa5c0d4d",
			"4059d77b1a1a9c44:1cbf32ef05df6718",
			"a87990ce50f35957:985e78dfbafefc10",
			"cb98696a242d4a97:33d55dec526a37eb",
			"26a20938052db0d6:048673912014bef4",
			"a1fd4ba99a0d90cf:dd086e5e9cba94f3",
			"dc692f90bee4464d:25f87cb82ba1be29",
			"42c98654f6b490c9:c4b215571dfc8f3b",
			"8c0ce17ad3c77d56:773e0e289cb21bea",
			"94909a53601f4c1a:558cf759f726a567",
			"169d88a670f7cfc7:9166adb5534894a8",
			"23b7c6548344e1b5:7bb30898cd708c8e",
			"49084b3f2245baf4:2ec0295700c78e53",
			"7437a4d172003241:2be3a87663137bae",
			"fa5a69051483adaf:a4918127945527fb",
			"5d7b48054e76057c:1334e83cc458b117",
			"f6bf281d5a0c25d6:54a0bdcb9b98b682",
			"83f2930aa0703c5b:825dfa0160b02a98",
			"874ab7dbf9321721:9ba0a325b5d5c67c",
			"d1ab81b5a3eaf315:73f6e05c7787ad9c",
			"fe5d36578b7aa233:1690a5c39a54b361",
			"1a4713410c7c169e:a1bb0478dc3273bb",
			"745f784a9d6a6ee1:d715d46757d6cfbf",
			"4b10d22f9022680d:ef70e3ec310a60af",
			"87aea670610feda4:e53e718721864e88",
			"51d4567821104f5b:69cb4ae1efe8cb25",
			"091fefd57cfe55af:8d48952aa31d4fc3",
			"09a057063676278b:b5fab219d7891745",
			"20bac3ecc3cfff03:4fda555f88e85078",
			"cf6d7c6a840aa97c:f7dd78c36b325978",
			"fcbe0c6b014a859a:d599fd8df10813bd",
			"0fcb72eb6de3810d:253ea9be21bf454b",
			"bab365add89aded5:49d88cd5ab3f7144",
			"21fbd05c23e516d6:a7f4dedd75496f36",
			"1b0ba97824fef994:9d8bcde1cf0051ec",
			"8c4576f0c4f6523d:a8363a84ed4a174a",
			"bd06370ccd9733ca:2f79766dad759db3",
			"c2cd2c0cefd6357c:b108eb277863fbdd",
			"572a835f8ff001c8:bd09002550ed7841",
			"e0bbab59bb6d0d58:9294a5780638c0c0",
			"59d0859cc1e1e75f:042c6e322174d761",
			"c60ba05060d8b265:3558f7dde32ed01e",
			"5463002cf7e169e8:d015b439df39ef03",
			"344c418f6e50e7c7:ecf552ec50043127",
			"10a492c6f34bdeae:45782d938e6f17fe",
			"c866e553e239a720:9b34c6094af1b288",
			"29b757c1033dad63:3f9049272922cd46",
			"f668bc8150d357de:fab09bde90569c25",
			"a7168526037ca983:959f80ce77f468c3",
			"b04c4fa47afd7d45:df8d987820be2f1a",
			"b925eb5c7d433a95:8c2f732c77364465",
			"131b0823d8c8516f:964326d77d7e6dca",
			"836df8579673efb7:3434455a721ca176",
			"af24a07f9abd6ffb:9acccbbabdc815f5",
			"2b079e7bb053dd6b:a2ef7481a8c055d7",
			"f2488cb3f6f16314:e79d103c3729ae2a",
			"fadd744023d4b825:14bee3bf905d2173",
			"c59f219907f75a53:d0f6e9d3a751b65b",
			"32eb460b358496fb:0b41ccd4f6e3645d",
			"85eafbd29ac41fe1:a7109a54d1895458",
			"dea7bb2f0a2a1fd4:fba3108429409b08",
			"a8df53a6f00af32b:7521927d7529718d",
			"ab51f192d9474293:31b52216be816088",
			"4d49438c0d21c54c:2d67486d7f5af03d",
			"3ab5f6d8f4480919:ed3d5926331c9831",
			"b3760210b3d136fb:6a33a63b84584c33",
			"072971c75fae8c29:fe1a902c53ef1c9b",
			"965a4d35243a0746:212eab3d5734e0c6",
			"30657448fac1f261:1554cac6947b0041",
			"5588c75e00f3ef28:392ea5f265525e29",
			"7a139b060cea6600:2abaf6f8f2c562b3",
			"bfb884f92be01a8e:a62dce7b0176acca",
			"e83de3ed56a710fe:0aaa816c4aab594f",
			"5f6b00ae29a93b84:b02fe04d28050c61",
			"94816215e5d08851:3d40f61b9a84e0ff",
			"b66ceb3bc1b38cce:12047ad59f51c73f",
			"23107fd977aac27a:739868c24bc97276",
			"ea679b4e034498b5:2a925e44d8c8ff10",
			"c889b064890e225e:3c1d0a55b666874d",
			"e86021ede6fb6c6f:3ee79a5d26077c41",
			"f5ee6a5047afaafc:b2006655a025dac9",
			"e28d4d2f902e9cb4:37f1c83ec43f66a0",
			"ccca2c828c57e4e1:eef1c8981bfb7abd",
			"080419ce04009b2e:5c5f616a741b1719",
			"a5594437d365486e:27a054d8ad2b55d7",
			"6cedad1f660cd74e:108276291a7a5445",
			"e9e5d238013d1a0c:5cbba2eaea8ce00a"
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
