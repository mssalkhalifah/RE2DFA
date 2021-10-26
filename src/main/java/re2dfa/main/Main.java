package re2dfa.main;

import re2dfa.scanner.RegexReader;

import java.io.FileNotFoundException;

public class Main {
    public Main(String fileLocation) {
        try {
            RegexReader reader = new RegexReader(fileLocation);
            System.out.println(reader.postfixify());
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
