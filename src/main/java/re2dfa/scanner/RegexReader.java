package re2dfa.scanner;

import re2dfa.main.Main;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public final class RegexReader {
    public static final String operators = "|.*";
    public static final String OPEN_PARA = "({[";
    public static final String CLOSE_PARA = ")}]";
    public static final String operand = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Scanner scanner;

    public RegexReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public static List<String> getTokenList(String regex) {
        LinkedList<String> tokenLinkedList = tokenizer(regex);


        return tokenLinkedList;
    }

    private static void fillSymbolTable(List<String> tokens) {

    }

    private static LinkedList<String> tokenizer(String regex) {
        LinkedList<String> tokens = new LinkedList<>();

        int currentPos = 0;
        while (currentPos < regex.length()) {
            char currentChar = regex.charAt(currentPos);
            if (isOperand(currentChar)
                    || isOperator(currentChar)
                    || isOpenPara(currentChar)
                    || isClosePara(currentChar)) {
                tokens.add(String.valueOf(currentChar));
            }
            if (currentChar == '\\') {
                char nextChar = regex.charAt(currentPos + 1);
                if (nextChar == 'e') {
                    tokens.add("epsilon");
                    ++currentPos;
                }
            }
            ++currentPos;
        }

        return tokens;
    }

    private static LinkedList<String> postfix(List<String> tokens) {
        LinkedList<String> postfixTokenList = new LinkedList<>();

        for (String token : tokens) {

        }

        return null;
    }

    public String postfixes() {
        String regex = scanner.next();
        regex = fillSymbolTable(regex);
        String re = addConcat(regex);
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

    private String fillSymbolTable(String regex) {
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

        return regex;
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
