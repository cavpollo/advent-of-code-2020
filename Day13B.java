package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Day13B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        bufferedReader.readLine(); // Throw away the value

        final ArrayList<Pair<Integer, Integer>> buses = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {

                final String[] split = readLine.trim().split(",");
                for (int i = 0; i < split.length; i++) {
                    if (!split[i].equals("x")) {
                        buses.add(Pair.of(i, Integer.parseInt(split[i])));
                    }
                }
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + buses.size() + " buses");

        final Instant start = Instant.now();

        Collections.sort(buses, Collections.reverseOrder(Comparator.comparingInt(pair -> pair.snd)));
//        System.out.println("Sorted buses " + buses);

        final Pair<Integer, Integer> biggestBus = buses.get(0);
        long counter = 100000000000000L / biggestBus.snd;
        long step = 1;

//        System.out.println("Starting with counter " + counter + " and step " + step);

        Long checkpointCounter = null;
        int startingPos = 1;
        boolean allFound;
        long base;
        do {
            base = (biggestBus.snd * counter) - biggestBus.fst;

            allFound = true;
            for (int i = startingPos; i < buses.size(); i++) {
                final Pair<Integer, Integer> bus = buses.get(i);
                if ((base + bus.fst) % bus.snd != 0) {
                    allFound = false;
                    break;
                } else {
                    if (checkpointCounter == null) {
                        checkpointCounter = counter;
                    } else {
                        step = counter - checkpointCounter;
                        checkpointCounter = null;
                        startingPos++;
//                        System.out.println("New checkpoint at " + counter + " with step " + step + " startingPos: " + startingPos);
                    }
                }
            }

            counter += step;
        } while (!allFound);

        if (!allFound) {
            throw new RuntimeException("=(");
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Using counter " + counter + ", at " + base + " all buses depart in order. Took " + timeElapsed + "ms");
    }
}
