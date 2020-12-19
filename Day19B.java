package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final Pattern rulePattern = Pattern.compile("^(\\d+): (.+)$", Pattern.CASE_INSENSITIVE);

        final Map<Integer, String> rules = new HashMap<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher ruleMatcher = rulePattern.matcher(readLine.trim());
                final boolean foundRule = ruleMatcher.find();

                if (!foundRule) {
                    throw new RuntimeException("=(");
                }
                rules.put(Integer.parseInt(ruleMatcher.group(1)), ruleMatcher.group(2));
            } else {
                break;
            }
        } while (true);

        final List<String> messages = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                messages.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        // Replace input as the problem says
        rules.put(8, "42 | 42 8");
        rules.put(11, "42 31 | 42 11 31");

        System.out.println("Got " + rules.size() + " rules");
        System.out.println("Got " + messages.size() + " messages");

        final Instant start = Instant.now();

        int validCount = 0;
        for (final String message : messages) {
//            System.out.println("\nmessage " + message);
            final List<Integer> nextValue = nextValue(message, rules, 0, Collections.singletonList(0));
            if (!nextValue.isEmpty() && nextValue.contains(message.length())) {
//                System.out.println("message " + message + " is valid");
                validCount++;
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println(validCount + " messages match rule 0 in " + timeElapsed + "ms");
    }

    private static List<Integer> nextValue(final String message, final Map<Integer, String> rules, final int ruleId, final List<Integer> positions) {
        final List<Integer> possibilities = new ArrayList<>();
        for (final Integer pos : positions) {
            if (pos >= message.length()) {
//                System.out.println("OVERFLOW");
                continue;
            }

            final String ruleString = rules.get(ruleId);
//            System.out.println("RULE " + ruleId + " - " + ruleString);
            if (ruleString.equals("\"a\"")) {
//                System.out.println("a at pos " + pos + " = " + message.charAt(pos));
                if (message.charAt(pos) == 'a') {
                    possibilities.add(pos + 1);
                }
            } else if (ruleString.equals("\"b\"")) {
//                System.out.println("b at pos " + pos + " = " + message.charAt(pos));
                if (message.charAt(pos) == 'b') {
                    possibilities.add(pos + 1);
                }
            } else {
                for (final String subRule : ruleString.split(" \\| ")) {
//                    System.out.println("subrule " + subRule);
                    List<Integer> andPositions = Collections.singletonList(pos);
                    boolean valid = true;
                    for (final String subRuleId : subRule.split(" ")) {
                        final List<Integer> nextValues = nextValue(message, rules, Integer.parseInt(subRuleId), andPositions);
                        if (nextValues.isEmpty()) {
//                            System.out.println("-> " + subRuleId + " INVALID");
                            valid = false;
                            break;
                        }
                        andPositions = nextValues;
                    }
                    if (valid) {
                        possibilities.addAll(andPositions);
                    }
                }
            }

        }

        return possibilities;
    }
}
