package re2dfa.main;

import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Arguments less than 1");
        } else {
            Scanner scanner = new Scanner(args[0]);

            if (args.length == 2) {
                if (args[1].matches("-v")) {
                    Main.start(scanner, "", true);
                } else {
                    Main.start(scanner, args[1], false);
                }
            } else if (args.length > 2) {
                String option = args[1];
                String input = args[2];

                if (!option.matches("-v")) {
                    System.err.println("Invalid arg must be: regex [opt] [input]");
                } else {
                    Main.start(scanner, input, true);
                }
            } else {
                Main.start(scanner, "", false);
            }
        }
    }
}
