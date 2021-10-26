package re2dfa.scanner;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.util.Stack;

public class RegexReader {
    private final Scanner scanner;
    private final String operators = "|.*";

    private final String operand = "abcdefghijklmnopqrstuvwxyz";

    public RegexReader(String filePath) throws FileNotFoundException {
        FileInputStream stream = new FileInputStream(filePath);
        scanner = new Scanner(stream);
    }

    public String postfixify() {
        String re = addConcat(scanner.next());
        Stack<String> stack = new Stack<>();

        StringBuilder stringBuilder = new StringBuilder();
        int currentPos = 0;
        while (currentPos < re.length()) {
            char currentChar = re.charAt(currentPos);
            if (operand.indexOf(currentChar) >= 0) {
               stringBuilder.append(currentChar);
            }
        }

        return re;
    }

    private String addConcat(String re) {
        StringBuilder stringBuilder = new StringBuilder();
        int currentPos = 0;
        while (currentPos < re.length() - 1) {
            char currentChar = re.charAt(currentPos);
            char nextChar = re.charAt(currentPos + 1);
            if (currentChar == '(' || currentChar == '|') {
                stringBuilder.append(currentChar);
            } else if (nextChar == '(' || Character.isLetterOrDigit(nextChar)) {
                stringBuilder.append(currentChar);
                stringBuilder.append('.');
            } else {
                stringBuilder.append(currentChar);
            }
            ++currentPos;
        }
        stringBuilder.append(re.charAt(re.length() - 1));
        return stringBuilder.toString();
    }
}
