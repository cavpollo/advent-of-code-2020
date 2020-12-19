package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19A {
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

        System.out.println("Got " + rules.size() + " rules");
        System.out.println("Got " + messages.size() + " messages");

        final Instant start = Instant.now();

        int validCount = 0;
        for (final String message : messages) {
//            System.out.println("\nmessage " + message);
            final Integer nextValue = nextValue(message, rules, 0, 0);
            if (nextValue != null && nextValue == message.length()) {
                validCount++;
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println(validCount + " messages match rule 0 in " + timeElapsed + "ms");
    }

    private static Integer nextValue(final String message, final Map<Integer, String> rules, final int ruleId, final int pos) {
        final String ruleString = rules.get(ruleId);
//        System.out.println("RULE " + ruleId + " - " + ruleString);
        if (ruleString.equals("\"a\"")) {
//            System.out.println("a at pos " + pos + " = " + message.charAt(pos));
            return message.charAt(pos) == 'a' ? pos + 1 : null;
        } else if (ruleString.equals("\"b\"")) {
//            System.out.println("b at pos " + pos + " = " + message.charAt(pos));
            return message.charAt(pos) == 'b' ? pos + 1 : null;
        } else {
            for (final String subRule : ruleString.split(" \\| ")) {
//                System.out.println("subrule " + subRule);
                int andPos = pos;
                boolean valid = true;
                for (final String subRuleId : subRule.split(" ")) {
                    final Integer nextValue = nextValue(message, rules, Integer.parseInt(subRuleId), andPos);
                    if (nextValue == null) {
//                        System.out.println("-> " + subRuleId + " INVALID");
                        valid = false;
                        break;
                    }
                    andPos = nextValue;
                }
                if (valid) {
                    return andPos;
                }
            }

            return null;
        }
    }
}
