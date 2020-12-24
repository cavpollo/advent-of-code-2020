package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24B {
    private static int DAYS = 100;
    private static boolean TILE_TEST = false;
    private static boolean INDIVIDUAL_TILE_TEST = false;
    private static boolean TILE_TEST_PT2 = false;
    private static boolean INDIVIDUAL_TILE_TEST_PT2 = false;

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

        Set<Pair<Integer, Integer>> tileSet = new HashSet<>();
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

        for (int i = 1; i <= DAYS; i++) {
            if (INDIVIDUAL_TILE_TEST_PT2) System.out.println("Day " + i);
            final Map<Pair<Integer, Integer>, Integer> whiteTileSet = new HashMap<>();
            final Set<Pair<Integer, Integer>> blackTileSet = new HashSet<>();
            for (final Pair<Integer, Integer> tile : tileSet) {
                final Set<Pair<Integer, Integer>> adjacentTiles = getAdjacentTiles(tile);
                if (INDIVIDUAL_TILE_TEST_PT2) System.out.println(tile + " adjacent: " + adjacentTiles);

                final Set<Pair<Integer, Integer>> adjacentWhiteTiles = new HashSet<>();
                for (final Pair<Integer, Integer> adjacentTile : adjacentTiles) {
                    if (!tileSet.contains(adjacentTile)) {
                        adjacentWhiteTiles.add(adjacentTile);
                    }
                }
                if (INDIVIDUAL_TILE_TEST_PT2) System.out.println(tile + " whites: " + adjacentWhiteTiles);

                final int adjacentWhiteTileCount = adjacentWhiteTiles.size();
                if (adjacentWhiteTileCount == 4 || adjacentWhiteTileCount == 5) {
                    blackTileSet.add(tile); // Stay black
                    if (INDIVIDUAL_TILE_TEST_PT2) System.out.println(tile + " black");
                }

                for (final Pair<Integer, Integer> whiteTile : adjacentWhiteTiles) {
                    whiteTileSet.merge(whiteTile, 1, Integer::sum);
                }
            }

            final Set<Pair<Integer, Integer>> flipToBlackTiles = whiteTileSet.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            blackTileSet.addAll(flipToBlackTiles);

            tileSet = blackTileSet;

            if (TILE_TEST_PT2) System.out.println("Day " + i + ": " + tileSet.size());
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Black tile count is " + tileSet.size() + " in " + timeElapsed + "ms");
    }

    private static Set<Pair<Integer, Integer>> getAdjacentTiles(final Pair<Integer, Integer> tile) {
        final Set<Pair<Integer, Integer>> adjacentTiles = new HashSet<>();

        for (int i = -2; i <= 2; i++) {
            if (i == 0) continue;
            adjacentTiles.add(Pair.of(tile.fst + i, tile.snd));
        }

        final int newY = (tile.fst % 2 == 0) ? tile.snd - 1 : tile.snd + 1;
        adjacentTiles.add(Pair.of(tile.fst - 1, newY));
        adjacentTiles.add(Pair.of(tile.fst + 1, newY));

        return adjacentTiles;
    }
}
