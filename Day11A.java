package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Day11A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        ArrayList<ArrayList<Seat>> seats = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final ArrayList<Seat> seatLineList = new ArrayList<>();
                for (String s : readLine.trim().split("")) {
                    seatLineList.add(new Seat(s));
                }

                seats.add(seatLineList);
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + seats.size() + " seat lines");

        final Instant start = Instant.now();

        int round = 0;
        boolean changed;
        do {
            final ArrayList<ArrayList<Seat>> nextSeatList = new ArrayList<>();

            changed = false;
            for (int i = 0; i < seats.size(); i++) {
                final ArrayList<Seat> nextSeatLineList = new ArrayList<>();

                final ArrayList<Seat> seatLine = seats.get(i);
                for (int j = 0; j < seatLine.size(); j++) {
                    final Seat currentSeat = seatLine.get(j);
                    if (currentSeat.isLocked()) {
                        nextSeatLineList.add(new Seat(false, true));
                        continue;
                    }

                    int occupiedCount = getOccupiedCount(i, j, seats);

                    final Boolean isCurrentSeatOccupied = currentSeat.isOccupied();
                    if (!isCurrentSeatOccupied) {
                        if (occupiedCount == 0) {
                            nextSeatLineList.add(new Seat(true));
                            changed = true;
                            continue;
                        }
                    } else {
                        if (occupiedCount >= 4) {
                            nextSeatLineList.add(new Seat(false));
                            changed = true;
                            continue;
                        }
                    }

                    // Keep it the same then
                    nextSeatLineList.add(new Seat(isCurrentSeatOccupied));
                }

                nextSeatList.add(nextSeatLineList);
            }

            seats = nextSeatList;
            round++;
//            System.out.println("Round " + round + " done:");
//            for (final ArrayList<Seat> seatLine : seats) {
//                for (final Seat seat : seatLine) {
//                    System.out.print(seat.isLocked() ? "." : seat.isOccupied() ? "#" : "L");
//                }
//                System.out.println();
//            }
//            System.out.println();
        } while (changed);

        int finalOccupiedCount = 0;
        for (final ArrayList<Seat> seatLine : seats) {
            for (final Seat seat : seatLine) {
                if (seat.isOccupied()) {
                    finalOccupiedCount++;
                }
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();  //in millis

        System.out.println("After " + round + "th round the final OccupiedSeatCount is " + finalOccupiedCount + " in " + timeElapsed + "ms");
    }

    private static int getOccupiedCount(final int i, final int j, final ArrayList<ArrayList<Seat>> seats) {
        int count = 0;
        if (i - 1 >= 0) {
            final ArrayList<Seat> prevSeatLine = seats.get(i - 1);
            count += (j - 1 >= 0 && prevSeatLine.get(j - 1).isOccupied() ? 1 : 0) +
                    (prevSeatLine.get(j).isOccupied() ? 1 : 0) +
                    (j + 1 < prevSeatLine.size() && prevSeatLine.get(j + 1).isOccupied() ? 1 : 0);
        }

        if (i + 1 < seats.size()) {
            final ArrayList<Seat> nextSeatLine = seats.get(i + 1);

            count += (j - 1 >= 0 && nextSeatLine.get(j - 1).isOccupied() ? 1 : 0) +
                    (nextSeatLine.get(j).isOccupied() ? 1 : 0) +
                    (j + 1 < nextSeatLine.size() && nextSeatLine.get(j + 1).isOccupied() ? 1 : 0);
        }

        final ArrayList<Seat> seatLine = seats.get(i);
        return count +
                ((j - 1 >= 0 && seatLine.get(j - 1).isOccupied()) ? 1 : 0) + //Left
                ((j + 1 < seatLine.size() && seatLine.get(j + 1).isOccupied()) ? 1 : 0); // Right
    }

    private static class Seat {
        private final Boolean locked;
        private final Boolean occupied;

        private Seat(final String value) {
            this.locked = value.equals(".");
            this.occupied = false;
        }

        public Seat(final boolean occupied) {
            this.locked = false;
            this.occupied = occupied;
        }

        public Seat(final boolean occupied, final boolean locked) {
            this.locked = locked;
            this.occupied = occupied;
        }

        public Boolean isLocked() {
            return locked;
        }

        public Boolean isOccupied() {
            return occupied;
        }

        public Boolean isEmpty() {
            return !occupied;
        }
    }
}
