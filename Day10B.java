package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Day10B {
    public static HashMap<Integer, Long> positionCombinationsCache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

        final ArrayList<Long> adapterList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                adapterList.add(Long.parseLong(readLine.trim()));
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + adapterList.size() + " adapters");
//        System.out.println(adapterList.toString());

        final Instant start = Instant.now();

        adapterList.add(0L);

        adapterList.sort(Comparator.naturalOrder());

        final long combinations = getCombinations(0, adapterList);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Combinations: " + combinations + " in " + timeElapsed + "ms");
    }

    private static long getCombinations(int currentPosition, ArrayList<Long> adapterList) {
        if (currentPosition >= adapterList.size() - 1) {
//            System.out.println("END");
            return 1;
        }

        if (positionCombinationsCache.containsKey(currentPosition)) {
            final Long combinations = positionCombinationsCache.get(currentPosition);
//            System.out.println("== " + combinations + " for " + adapterList.get(currentPosition));
            return combinations;
        }

        long combinations = 0;
        for (int j = 1; j <= 3; j++) {
            final int nextPosition = currentPosition + j;
            if (nextPosition < adapterList.size() && adapterList.get(nextPosition) - adapterList.get(currentPosition) <= 3) {
//                System.out.println(adapterList.get(currentPosition) + "(" + currentPosition + ") to " + adapterList.get(nextPosition) + "(" + (nextPosition) + ")");
                combinations += getCombinations(nextPosition, adapterList);
            }
        }

        positionCombinationsCache.put(currentPosition, combinations);

//        System.out.println("== " + combinations + " for " + adapterList.get(currentPosition));

        return combinations;
    }
}
