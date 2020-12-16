package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final Pattern linePattern = Pattern.compile("^([a-z]+)([0-9]+)$", Pattern.CASE_INSENSITIVE);

        ArrayList<Direction> directions = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher lineMatcher = linePattern.matcher(readLine.trim());
                final boolean found = lineMatcher.find();

                if (!found) {
                    throw new IllegalArgumentException(readLine.trim());
                }

                directions.add(new Direction(lineMatcher.group(1), Long.parseLong(lineMatcher.group(2))));
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + directions.size() + " directions");

        final Instant start = Instant.now();

        long x = 0;
        long y = 0;
        long dx = 10;
        long dy = 1;

        for (final Direction direction : directions) {
            if (direction.getType().equals("L")) {
                long temp;
                switch ((int) (direction.getValue() / 90)) {
                    case 1:
                        temp = dy;
                        dy = dx;
                        dx = -temp;
                        break;
                    case 2://180
                        dx = -dx;
                        dy = -dy;
                        break;
                    case 3:
                        temp = dy;
                        dy = -dx;
                        dx = temp;
                        break;
                }
            } else if (direction.getType().equals("R")) {
                long temp;
                switch ((int) (direction.getValue() / 90)) {
                    case 1:
                        temp = dy;
                        dy = -dx;
                        dx = temp;
                        break;
                    case 2://180
                        dx = -dx;
                        dy = -dy;
                        break;
                    case 3:
                        temp = dy;
                        dy = dx;
                        dx = -temp;
                        break;
                }
            } else {
                switch (direction.getType()) {
                    case "F":
                        x += dx * direction.getValue();
                        y += dy * direction.getValue();
                        break;
                    case "E":
                        dx += direction.getValue();
                        break;
                    case "N":
                        dy += direction.getValue();
                        break;
                    case "W":
                        dx -= direction.getValue();
                        break;
                    case "S":
                        dy -= direction.getValue();
                        break;
                }
            }

//            System.out.println(direction.toString() + ": x " + x + " y " + y + " dx " + dx + " dy " + dy);
        }


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("x " + x + " y " + y + " manhattan distance " + (Math.abs(x) + Math.abs(y)) + " in " + timeElapsed + "ms");
    }

    private static class Direction {
        private final String type;
        private final Long value;

        private Direction(final String type, final Long value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public Long getValue() {
            return value;
        }

        @Override
        public String toString() {
            return type + " " + value;
        }
    }
}
