package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Day18B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final List<String> expressions = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                expressions.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + expressions.size() + " expressions");

        final Instant start = Instant.now();

        long total = 0;
        for (final String expression : expressions) {
//            System.out.println("calculating: " + expression);
            total += calculate(expression);
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Total of all expressions is " + total + " in " + timeElapsed + "ms");
    }


    private static long calculate(final String expression) {
//        System.out.println(expression + " (EVAL)");
        Character operator = null;
        Long result = null;
        int parserPosition = 0;
        do {
            final long parsedValue;
            int startParserPosition = parserPosition;
            if (operator != null && operator.equals('*')) {
                int parenthesisCount = 0;
                do {
                    if (expression.charAt(parserPosition) == '(') {
                        parenthesisCount++;
                    } else if (expression.charAt(parserPosition) == ')') {
                        parenthesisCount--;
                    }
                    parserPosition++;
                } while (parserPosition < expression.length() &&
                        (expression.charAt(parserPosition) != ')' || parenthesisCount >= 0) &&
                        (expression.charAt(parserPosition) != '*' || parenthesisCount > 0));

                parsedValue = calculate(expression.substring(startParserPosition, parserPosition < expression.length() ? parserPosition - 1 : parserPosition));

                if (parserPosition < expression.length() && expression.charAt(parserPosition) == '*') {
                    parserPosition -= 1; // Hack to continue the operation chain
                }
            } else if (expression.charAt(parserPosition) == '(') {
                int parenthesisCount = 0;
                do {
                    if (expression.charAt(parserPosition) == '(') {
                        parenthesisCount++;
                    } else if (expression.charAt(parserPosition) == ')') {
                        parenthesisCount--;
                    }
                    parserPosition++;
                } while (parenthesisCount > 0 && parserPosition < expression.length());

                parsedValue = calculate(expression.substring(startParserPosition + 1, parserPosition - 1));
            } else {
                do {
                    parserPosition++;
                } while (parserPosition < expression.length() && expression.charAt(parserPosition) != ' ');

                parsedValue = Long.parseLong(expression.substring(startParserPosition, parserPosition));
            }

            if (result == null) {
                result = parsedValue;
            } else {
                if (operator.equals('*')) {
//                    System.out.println((result * parsedValue) + " = " + result + " * " + parsedValue + " (OP)");
                    result = result * parsedValue;
                } else if (operator.equals('+')) {
//                    System.out.println((result + parsedValue) + " = " + result + " + " + parsedValue + " (OP)");
                    result = result + parsedValue;
                } else {
                    throw new RuntimeException("=( weird operator: '" + operator + "'");
                }
            }

            if (parserPosition >= expression.length()) {
//                System.out.println(result + " = " + expression + " (END)");
                return result;
            }

            operator = expression.charAt(parserPosition + 1);
            parserPosition = parserPosition + 3;
        } while (true);
    }
}
