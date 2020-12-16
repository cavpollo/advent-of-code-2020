package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12A {
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

        int x = 0;
        int y = 0;
        int d = 0;

        for (final Direction direction : directions) {
            if (direction.getType().equals("L")) {
                d += direction.getValue();
                if (d > 360) d -= 360;
                continue;
            }
            if (direction.getType().equals("R")) {
                d -= direction.getValue();
                if (d < 0) d += 360;
                continue;
            }

            if (direction.getType().equals("F")) {
                x += Math.round(Math.cos(d * Math.PI / 180.) * direction.getValue());
                y += Math.round(Math.sin(d * Math.PI / 180.) * direction.getValue());
            } else {

                switch (direction.getType()) {
                    case "E":
                        x += direction.getValue();
                        break;
                    case "N":
                        y += direction.getValue();
                        break;
                    case "W":
                        x -= direction.getValue();
                        break;
                    case "S":
                        y -= direction.getValue();
                        break;
                }
            }

            System.out.println("x " + x + " y " + y + " d " + d);
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
    }
}
