package re2dfa.main;

import java.util.BitSet;
import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) {
        Scanner scanner;
        if (args.length == 2) {
            scanner = new Scanner(args[0]);
            Main.start(scanner, args[1], false, false);
        } else if (args.length == 3) {
            BitSet flags = new BitSet(2);

            if (args[0].contains("v")) {
                flags.set(0);
            } else if (args[0].contains("d")) {
                flags.set(1);
            }

            scanner = new Scanner(args[1]);
            Main.start(scanner, args[2], flags.get(0), flags.get(1));
        }
    }
}
