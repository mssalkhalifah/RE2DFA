package re2dfa.main;

import java.io.InputStream;
import java.util.Scanner;

public class EntryPoint {
    public static void main(String[] args) {
        InputStream stream =  Main.class.getClassLoader().getResourceAsStream("testregex.regex");
        Scanner scanner = new Scanner(stream);
        new Main(scanner);
    }
}
