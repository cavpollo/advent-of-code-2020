package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Day15A {
    private static final int GOAL = 2020;

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final ArrayList<Integer> values = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (String s : readLine.trim().split(",")) {
                    values.add(Integer.parseInt(s));
                }
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + values.size() + " values");

        final Instant start = Instant.now();

        final Map<Integer, Integer> lastMap = new HashMap<>();
        for (int i = 0; i < values.size() - 1; i++) {
            lastMap.put(values.get(i), i + 1);
//            System.out.println("Turn " + (i + 1) + " : " + values.get(i));
        }

        int lastValue = values.get(values.size() - 1);
        for (int turn = values.size() + 1; turn <= GOAL; turn++) {
            if (lastMap.get(lastValue) == null) {
                lastMap.put(lastValue, turn - 1); // Always store prev
//                System.out.println("Turn " + (turn - 1) + " : " + lastValue);

                lastValue = 0;
            } else {
                final Integer lastTurnSeen = lastMap.get(lastValue);

                lastMap.put(lastValue, turn - 1); // Always store prev
//                System.out.println("Turn " + (turn - 1) + " : " + lastValue);

                lastValue = (turn - 1) - lastTurnSeen;
            }
        }

        System.out.println("Turn " + GOAL + " : " + lastValue);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("The 2020th number is " + lastValue + " in " + timeElapsed + "ms");
    }
}
