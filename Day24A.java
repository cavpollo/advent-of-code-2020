package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24A {
    private static boolean TILE_TEST = false;
    private static boolean INDIVIDUAL_TILE_TEST = false;

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final List<String> tiles = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                tiles.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + tiles.size() + " tiles");

        final Instant start = Instant.now();

        final Set<Pair<Integer, Integer>> tileSet = new HashSet<>();
        for (final String tile : tiles) {
            int x = 0;
            int y = 0;
            if (TILE_TEST) System.out.println("tile: " + tile);
            for (int i = 0; i < tile.length(); i++) {
                final char c = tile.charAt(i);
                switch (c) {
                    case 'w':
                        if (INDIVIDUAL_TILE_TEST) System.out.println("d: " + c);
                        x -= 2;
                        break;
                    case 'e':
                        if (INDIVIDUAL_TILE_TEST) System.out.println("d: " + c);
                        x += 2;
                        break;
                    case 'n':
                        if (Math.abs(x % 2) == 1) y++;
                    case 's':
                        if (INDIVIDUAL_TILE_TEST) System.out.print("d: " + c);
                        if (c == 's' && Math.abs(x % 2) == 0) y--;
                        final char c2 = tile.charAt(i + 1);
                        if (INDIVIDUAL_TILE_TEST) System.out.println(c2);
                        if (c2 == 'w') {
                            x--;
                        } else if (c2 == 'e') {
                            x++;
                        }
                        i++;
                        break;
                }
                if (INDIVIDUAL_TILE_TEST) System.out.println("Now at x=" + x + " y=" + y);
            }
            final Pair<Integer, Integer> pair = Pair.of(x, y);
            if (tileSet.contains(pair)) {
                if (TILE_TEST) System.out.println("Removing x=" + x + " y=" + y);
                tileSet.remove(pair);
            } else {
                if (TILE_TEST) System.out.println("Adding x=" + x + " y=" + y);
                tileSet.add(pair);
            }
        }

        if (TILE_TEST) {
            for (final Pair<Integer, Integer> pair : tileSet) {
                System.out.print("[" + pair.fst + "," + pair.snd + "]");
            }
            System.out.println();
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Black tile count is " + tileSet.size() + " in " + timeElapsed + "ms");
    }
}
