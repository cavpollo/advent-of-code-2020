package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Day23A {
    private static int MOVES = 100;

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final List<Integer> cups = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (final String cupNumber : readLine.trim().split("")) {
                    cups.add(Integer.parseInt(cupNumber));
                }
            } else {
                break;
            }
        } while (true);

        final int totalCups = cups.size();
        System.out.println("Got " + totalCups + " cups");

        final Instant start = Instant.now();

//        final Integer highestValue = cups.stream().max(Comparator.naturalOrder()).get();

        int position = 0;
        for (int i = 0; i < MOVES; i++) {
//            System.out.println((i + 1) + ": " + cups);
//            System.out.println("position: " + position);

            final int fixedCupNumber = cups.get(position);

            //Remove cups
            final List<Integer> nextThreeCups = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (position + 1 >= cups.size()) {
                    nextThreeCups.add(cups.remove(0));
                } else {
                    nextThreeCups.add(cups.remove(position + 1));
                }
            }
//            System.out.println("removedCups: " + nextThreeCups);

            int destinationCupNumber = fixedCupNumber;
            do {
                destinationCupNumber--;
                if (destinationCupNumber == 0) destinationCupNumber = totalCups;
            } while (nextThreeCups.contains(destinationCupNumber));
//            System.out.println("destinationCupNumber: " + destinationCupNumber);


            final int destinationCupPosition = cups.indexOf(destinationCupNumber);
//            System.out.println("destinationCupPosition: " + destinationCupPosition);
            cups.addAll(destinationCupPosition + 1, nextThreeCups);

//            System.out.println("--- " + cups);

            for (int j = cups.indexOf(fixedCupNumber) - position; j > 0; j--) {
//                System.out.println("Fixing<");
                cups.add(cups.size() - 1, cups.remove(0));
            }

            position++;
            if (position >= totalCups) position = 0;
        }
//        System.out.println("END: " + cups);

        final StringBuilder cupOrder = new StringBuilder();
        int i = cups.indexOf(1);
        do {
            i++;
            if (i == totalCups) i = 0;
            final Integer integer = cups.get(i);
            if (integer == 1) break;
            cupOrder.append(cups.get(i));
        } while (true);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Cup order is " + cupOrder + " in " + timeElapsed + "ms");
    }
}
