package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12B2 {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

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

        double x = 0;
        double y = 0;
        double dx = 10;
        double dy = 1;

        for (final Direction direction : directions) {
            switch (direction.getType()) {
                case "L":
                case "R":
                    double angle = Math.atan2(dy, dx);
                    double hypo = Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));
//                    System.out.println("angle " + angle + " hypo " + hypo);
//
//                    System.out.println("using angle " + direction.getValue() * Math.PI / 180.);
                    if (direction.getType().equals("L")) {
                        angle += direction.getValue() * Math.PI / 180.;
                    } else if (direction.getType().equals("R")) {
                        angle -= direction.getValue() * Math.PI / 180.;
                    }

//                    System.out.println("new angle " + angle + " hypo " + hypo);

                    dx = Math.cos(angle) * hypo;
                    dy = Math.sin(angle) * hypo;
                    break;
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

//            x = Math.round(x);
//            y = Math.round(y);
//            dx = Math.round(dx);
//            dy = Math.round(dy);
//            System.out.println(direction.toString() + ": x " + x + " y " + y + " dx " + dx + " dy " + dy);
//            System.out.println(direction.toString() + ": x " + Math.round(x) + " y " + Math.round(y) + " dx " + Math.round(dx) + " dy " + Math.round(dy));
        }


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

//        System.out.println("x " + x + " y " + y + " manhattan distance " + (Math.abs(x) + Math.abs(y)) + " in " + timeElapsed + "ms");
        System.out.println("x " + Math.round(x) + " y " + Math.round(y) + " manhattan distance " + (Math.abs(Math.round(x)) + Math.abs(Math.round(y))) + " in " + timeElapsed + "ms");
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
