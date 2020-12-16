package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16B {
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

                ticketFields.add(new TicketField(fieldMatcher.group(1),
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

        final String ownReadLine = bufferedReader.readLine();// Own ticket
        final Matcher ownTicketMatcher = ticketPattern.matcher(ownReadLine.trim());
        final boolean foundOwnTicket = ownTicketMatcher.find();

        if (!foundOwnTicket) {
            throw new RuntimeException("doesn't fit regex: '" + ownReadLine + "'");
        }
        final List<Integer> ownTicket = Arrays.stream(ownReadLine.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

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

        // Filter out invalid tickets
        final List<List<Integer>> validTickets = tickets.stream()
                .filter(ticket -> isValidTicket(ticketFields, ticket))
                .collect(Collectors.toList());

        // Among the valid tickets, keep track of which column can fit each ticket field
        for (final TicketField ticketField : ticketFields) {
//            System.out.println(ticketField.getName());
            for (final List<Integer> ticket : validTickets) {
                final Set<Integer> validColumns = new HashSet<>();
                for (int colNum = 0; colNum < ticket.size(); colNum++) {
                    final Integer ticketValue = ticket.get(colNum);
                    if (ticketField.isValid(ticketValue)) {
                        validColumns.add(colNum);
                    }
                }
//                System.out.print(validColumns);
                ticketField.saveSimilar(validColumns);
//                System.out.println(" -> " + ticketField.getFittingColumns());
            }
        }

        // Narrow down which ticket field can only match a column
        do {
            for (final TicketField ticketField : ticketFields) {
                if (ticketField.getFittingColumns().size() == 1) {
                    final Integer columnNumber = ticketField.getFittingColumns().iterator().next();
                    ticketField.setColumnNumber(columnNumber);
//                    System.out.println("Column " + columnNumber + " matches " + ticketField.getName());
                    remove(ticketFields, columnNumber);
                }
            }
        } while (!isEmpty(ticketFields));

        // Calculate the oriduct of all departures
        long product = 1;
        for (final TicketField ticketField : ticketFields) {
            if (ticketField.getName().startsWith("departure")) {
                product *= ownTicket.get(ticketField.getColumnNumber());
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Product of departure fields is " + product + " in " + timeElapsed + "ms");
    }

    private static void remove(final List<TicketField> ticketFields, final Integer columnNumber) {
        for (final TicketField ticketField : ticketFields) {
            ticketField.removeFromFittingColumns(columnNumber);
        }
    }

    private static boolean isEmpty(final List<TicketField> ticketFields) {
        for (final TicketField ticketField : ticketFields) {
            if (!ticketField.getFittingColumns().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidTicket(final List<TicketField> ticketFields, final List<Integer> ticket) {
        for (final Integer value : ticket) {
            boolean valid = false;
            for (TicketField ticketField : ticketFields) {
                if (ticketField.isValid(value)) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                return false;
            }
        }
        return true;
    }

    private static class TicketField {
        private final String name;
        private final Integer lowerBound1;
        private final Integer upperBound1;
        private final Integer lowerBound2;
        private final Integer upperBound2;
        private Set<Integer> fittingColumns;
        private Integer columnNumber;

        public TicketField(final String name,
                           final Integer lowerBound1,
                           final Integer upperBound1,
                           final Integer lowerBound2,
                           final Integer upperBound2) {
            this.name = name;
            this.lowerBound1 = lowerBound1;
            this.upperBound1 = upperBound1;
            this.lowerBound2 = lowerBound2;
            this.upperBound2 = upperBound2;
            this.fittingColumns = null;
            this.columnNumber = null;
        }

        public String getName() {
            return name;
        }

        public Set<Integer> getFittingColumns() {
            return fittingColumns;
        }

        public Integer getColumnNumber() {
            return columnNumber;
        }

        public void setColumnNumber(final Integer columnNumber) {
            this.columnNumber = columnNumber;
        }

        public boolean isValid(final int value) {
            return (value >= lowerBound1 && value <= upperBound1) || (value >= lowerBound2 && value <= upperBound2);
        }

        public void saveSimilar(final Set<Integer> colNumber) {
            if (fittingColumns == null) {
                fittingColumns = colNumber;
            } else {
                fittingColumns = fittingColumns.stream()
                        .filter(colNumber::contains)
                        .collect(Collectors.toSet());
            }
        }

        public void removeFromFittingColumns(final Integer columnNumber) {
            fittingColumns = fittingColumns.stream()
                    .filter(it -> !it.equals(columnNumber))
                    .collect(Collectors.toSet());
        }
    }
}
