package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final Pattern fieldPattern = Pattern.compile("^([a-z\\s]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)$", Pattern.CASE_INSENSITIVE);
        final Pattern ticketPattern = Pattern.compile("^\\d+(,\\d+)+$", Pattern.CASE_INSENSITIVE);

        final List<TicketField> ticketFields = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher fieldMatcher = fieldPattern.matcher(readLine.trim());
                final boolean foundField = fieldMatcher.find();

                if (!foundField) {
                    throw new RuntimeException("doesn't fit regex: '" + readLine + "'");
                }

                ticketFields.add(new TicketField(
                        Integer.parseInt(fieldMatcher.group(2)),
                        Integer.parseInt(fieldMatcher.group(3)),
                        Integer.parseInt(fieldMatcher.group(4)),
                        Integer.parseInt(fieldMatcher.group(5))));
            } else {
                break;
            }
        } while (true);

        final String readOwnLine = bufferedReader.readLine();
        if (!readOwnLine.trim().equals("your ticket:")) {
            throw new RuntimeException("doesn't fit \"your ticket:\" message '" + readOwnLine + "'");
        }

        bufferedReader.readLine(); // Own ticket
        bufferedReader.readLine(); // Empty line
        bufferedReader.readLine(); // "Nearby tickets" line

        final List<List<Integer>> tickets = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher ticketMatcher = ticketPattern.matcher(readLine.trim());
                final boolean foundTicket = ticketMatcher.find();

                if (!foundTicket) {
                    throw new RuntimeException("doesn't fit regex: '" + readLine + "'");
                }

                final List<Integer> list = Arrays.stream(readLine.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                tickets.add(list);
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + ticketFields.size() + " ticket fields");
        System.out.println("Got " + tickets.size() + " tickets");

        final Instant start = Instant.now();

        int invalidTicketValues = 0;
        for (final List<Integer> ticket : tickets) {
            Integer invalidTicketValue = null;
            int invalidTicketFields = 0;
            for (final Integer value : ticket) {

                boolean valid = false;
                for (TicketField ticketField : ticketFields) {
                    if (ticketField.isValid(value)) {
                        valid = true;
                        break;
                    }
                }

                if (!valid) {
//                    System.out.println("Invalid value " + value);
                    invalidTicketFields++;
                    invalidTicketValue = value;
                    break;
                }
            }
            if (invalidTicketFields == 1) {
//                System.out.println("1 invalid value for ticket: " + ticket);
                invalidTicketValues += invalidTicketValue;
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Sum of invalid ticket fields is " + invalidTicketValues + " in " + timeElapsed + "ms");
    }

    private static class TicketField {
        private final Integer lowerBound1;
        private final Integer upperBound1;
        private final Integer lowerBound2;
        private final Integer upperBound2;

        public TicketField(final Integer lowerBound1,
                           final Integer upperBound1,
                           final Integer lowerBound2,
                           final Integer upperBound2) {
            this.lowerBound1 = lowerBound1;
            this.upperBound1 = upperBound1;
            this.lowerBound2 = lowerBound2;
            this.upperBound2 = upperBound2;
        }

        public boolean isValid(final int value) {
            return (value >= lowerBound1 && value <= upperBound1) || (value >= lowerBound2 && value <= upperBound2);
        }
    }
}
