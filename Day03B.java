package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day03B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

        final ArrayList<String> treeLines = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                treeLines.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + treeLines.size() + " treeLines");

        int posY = 0;
        ArrayList<Tracker> trackers = new ArrayList<>();
        trackers.add(new Tracker(1, 1));
        trackers.add(new Tracker(3, 1));
        trackers.add(new Tracker(5, 1));
        trackers.add(new Tracker(7, 1));
        trackers.add(new Tracker(1, 2));
        for (final String treeLine : treeLines) {
            final int treeLineLength = treeLine.length();
            for (int linePos = 0; linePos < treeLineLength; linePos++) {
                if (treeLine.charAt(linePos) == '#') {
                    for (final Tracker tracker : trackers) {
                        tracker.incrementCheck(linePos, posY);
                    }
                }
            }
            for (final Tracker tracker : trackers) {
                tracker.nextLine(posY, treeLineLength);
            }
            posY++;
        }

        long mult = 1;
        for (final Tracker tracker : trackers) {
            System.out.println("Trees found: " + tracker.toString());
            mult = mult * tracker.getCount();
        }

        System.out.println("Multiplication is: " + mult);
    }

    private static class Tracker {
        private int x;
        private int y;
        private final int right;
        private final int down;
        private int count;

        Tracker(final int right, final int down) {
            this.x = 0;
            this.y = 0;
            this.right = right;
            this.down = down;
            this.count = 0;
        }

        public void nextLine(int currentY, int maxLength) {
            if (currentY < y) {
                return;
            }
            x = x + right;
            y = y + down;
            if (x >= maxLength) {
                x = x - maxLength;
            }
        }

        public void incrementCheck(final int x, final int y) {
            if (this.x == x && this.y == y) {
                System.out.println("x" + x + ",y" + y + " is: #");
                count++;
            } else {
                System.out.println("x" + x + ",y" + y + " vs this.x" + this.x + ",this.y" + this.y + " is: .");
            }
        }

        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return "Tracker{" +
                    "right=" + right +
                    ", down=" + down +
                    ", count=" + count +
                    '}';
        }
    }
}
