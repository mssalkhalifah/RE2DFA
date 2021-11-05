package re2dfa.scanner;

import re2dfa.main.Main;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexReader {
    public static final String operators = "|.*";
    public static final Pattern operands = Pattern.compile("[a-zA-Z]|[0-9]|\\\\.|\\\\e|[+-]");
    public static final Pattern operator = Pattern.compile("[|]|[.]|[*]");

    private static final Pattern regularExpressionPattern = Pattern.compile("\\\\e|\\\\.|[*]|[|]|[()]|[a-zA-Z.]|[0-9]|[+-]");
    private static final Pattern openParenthesis = Pattern.compile("[(]");
    private static final Pattern closeParenthesis = Pattern.compile("[)]");
    private static final Pattern epsilon = Pattern.compile("\\\\e");
    private static final Pattern dot = Pattern.compile("\\\\.");

    public static List<String> getTokenList(String regex) {
        LinkedList<String> tokenLinkedList = postfix(insertConcat(tokenizer(regex)));
        fillSymbolTable(tokenLinkedList);

        return tokenLinkedList;
    }

    private static void fillSymbolTable(List<String> tokens) {
        for (String token : tokens) {
            if (epsilon.matcher(token).matches()) {
                Main.symbolTable.put("\\e", null);
            } else if (dot.matcher(token).matches()) {
                Main.symbolTable.put("\\.", ".");
            } else if (operands.matcher(token).matches()) {
                Main.symbolTable.put(token, token);
            }
        }
    }

    private static LinkedList<String> insertConcat(LinkedList<String> tokens) {
        LinkedList<String> concatTokens = new LinkedList<>();
        ArrayList<String> tokenArray = new ArrayList<>(tokens);

        Pattern skip = Pattern.compile("[(|]");

        int currentPos = 0;
        while (currentPos < tokenArray.size() - 1) {
            String currentToken = tokenArray.get(currentPos);
            String nextToken = tokenArray.get(currentPos + 1);

            if (skip.matcher(currentToken).matches()) {
                concatTokens.add(currentToken);
            } else if (openParenthesis.matcher(nextToken).matches()
                    || operands.matcher(nextToken).matches()) {
                concatTokens.add(currentToken);
                concatTokens.add(".");
            } else {
                concatTokens.add(currentToken);
            }

            ++currentPos;
        }

        concatTokens.add(tokenArray.get(tokenArray.size() - 1));

        return concatTokens;
    }

    private static LinkedList<String> tokenizer(String regex) {
        LinkedList<String> tokens = new LinkedList<>();

        Matcher reMatcher = regularExpressionPattern.matcher(regex);
        while (reMatcher.find()) {
            tokens.add(reMatcher.group());
        }

        return tokens;
    }

    private static LinkedList<String> postfix(List<String> tokens) {
        LinkedList<String> postfixTokenList = new LinkedList<>();
        Stack<String> stringStack = new Stack<>();

        stringStack.push("(");
        tokens.add(")");

        for (String token : tokens) {
            if (operands.matcher(token).matches()) {
                postfixTokenList.add(token);
            } else if (operator.matcher(token).matches()) {
                while (!stringStack.isEmpty()
                        && operator.matcher(stringStack.peek()).matches()
                        && isLowerPrecedence(token.charAt(0), stringStack.peek().charAt(0))) {
                    postfixTokenList.add(stringStack.pop());
                }

                stringStack.push(token);
            } else if (openParenthesis.matcher(token).matches()) {
                stringStack.push(token);
            } else if (closeParenthesis.matcher(token).matches()) {
                while (!openParenthesis.matcher(stringStack.peek()).matches()) {
                    postfixTokenList.add(stringStack.pop());
                }

                stringStack.pop();
            }
        }
        return postfixTokenList;
    }

    private static boolean isLowerPrecedence(char op, char otherOp) {
        return operators.indexOf(op) < operators.indexOf(otherOp);
    }
}
