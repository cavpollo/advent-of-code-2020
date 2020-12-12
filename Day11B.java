package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Day11B {
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
                        if (occupiedCount >= 5) {
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
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("After " + round + "th round the final OccupiedSeatCount is " + finalOccupiedCount + " in " + timeElapsed + "ms");
    }

    private static int getOccupiedCount(final int i, final int j, final ArrayList<ArrayList<Seat>> seats) {
        int count = 0;
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }

                int mult = 1;
                do {
                    if (i + (k * mult) < 0 || i + (k * mult) >= seats.size()) {
                        break;
                    }
                    if (j + (l * mult) < 0 || j + (l * mult) >= seats.get(0).size()) {
                        break;
                    }

                    final Seat seat = seats.get(i + (k * mult)).get(j + (l * mult));
                    if (!seat.isLocked()) {
                        if (seat.isOccupied()) {
                            count++;
                        }
                        break;
                    }
                    mult++;
                } while (true);
            }
        }
        return count;
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
