package re2dfa.scanner;

import re2dfa.main.Main;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class RegexReader {
    private final Scanner scanner;
    public static final String operators = "|.*";
    public static final String OPEN_PARA = "({[";
    public static final String CLOSE_PARA = ")}]";
    public static final String operand = "abcdefghijklmnopqrstuvwxyz";

    public RegexReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String postfixes() {
        String re = addConcat(scanner.next());
        fillSymbolTable(re);
        Stack<Character> stack = new Stack<>();
        re += ')';
        stack.push('(');
        StringBuilder stringBuilder = new StringBuilder();
        int currentPos = 0;
        while (currentPos < re.length()) {
            char currentChar = re.charAt(currentPos);
            if (isOperand(currentChar)) {
               stringBuilder.append(currentChar);
            } else if (isOpenPara(currentChar)) {
                stack.push(currentChar);
            } else if (isOperator(currentChar)) {
                while (!stack.isEmpty()
                        && isOperator(stack.peek())
                        && isLowerPrecedence(currentChar, stack.peek())
                        && !isOpenPara(stack.peek())) {
                    stringBuilder.append(stack.pop());
                }
                stack.push(currentChar);
            } else if (isClosePara(currentChar)) {
                while (!isOpenPara(stack.peek())) {
                    stringBuilder.append(stack.pop());
                }
                stack.pop(); // Pops open parenthesis
            }
            ++currentPos;
        }

        return stringBuilder.toString();
    }

    private void fillSymbolTable(String regex) {
        int currentPos = 0;

        while (currentPos < regex.length()) {
            char currentChar = regex.charAt(currentPos);
            if (isOperand(currentChar)) {
                if (!Main.symbolTable.containsKey(String.valueOf(currentChar))) {
                    Main.symbolTable.put(String.valueOf(currentChar), String.valueOf(currentChar));
                }
            }
            ++currentPos;
        }
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

    public static boolean isOperator(char character) {
        return operators.indexOf(character) >= 0;
    }

    public static boolean isOpenPara(char character) {
        return OPEN_PARA.indexOf(character) >= 0;
    }

    public static boolean isClosePara(char character) {
        return CLOSE_PARA.indexOf(character) >= 0;
    }

    public static boolean isOperand(char character) {
        return operand.indexOf(character) >= 0;
    }

    public static boolean isLowerPrecedence(char op, char otherOp) {
        return operators.indexOf(op) < operators.indexOf(otherOp);
    }
}
