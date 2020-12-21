package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// WONT WORK FOR LARGER SETS OF TILES
// IT WILL RUN FOREVER... =(
public class Day20A2 {

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final Pattern titlePattern = Pattern.compile("^Tile (\\d+):$", Pattern.CASE_INSENSITIVE);

        final List<Tile> tiles = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher titleMatcher = titlePattern.matcher(readLine.trim());
                final boolean foundTitle = titleMatcher.find();

                if (!foundTitle) {
                    throw new RuntimeException("=(");
                }

                final List<String> tileContent = new ArrayList<>();
                do {
                    final String readTileLine = bufferedReader.readLine();
                    if (readTileLine != null && !readTileLine.isEmpty()) {
                        tileContent.add(readTileLine.trim());
                    } else {
                        break;
                    }
                } while (true);

                tiles.add(new Tile(Integer.parseInt(titleMatcher.group(1)), tileContent));
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + tiles.size() + " rules");

        final Instant start = Instant.now();

        BigInteger flipCombinations = BigInteger.ZERO;
        for (int i = 0; i < tiles.size(); i++) {
            flipCombinations = flipCombinations.shiftLeft(1);
            flipCombinations = flipCombinations.add(BigInteger.ONE);
        }

        System.out.println("Flip combinations: " + flipCombinations);

        for (BigInteger flipIds = BigInteger.ZERO; flipIds.compareTo(flipCombinations) <= 0; flipIds = flipIds.add(BigInteger.ONE)) {
//            System.out.println("\nUsing flipIds: " + flipIds);

            for (final Tile tile : tiles) {
                tile.resetLinks();
            }

            boolean badLinkCount = false;
            for (int i = 0; i < tiles.size(); i++) {
                final Tile tileA = tiles.get(i);
                final boolean flipA = flipIds.and(BigInteger.ONE.shiftLeft(i)).compareTo(BigInteger.ZERO) > 0;

                for (int j = i + 1; j < tiles.size(); j++) {
                    final Tile tileB = tiles.get(j);
                    final boolean flipB = flipIds.and(BigInteger.ONE.shiftLeft(j)).compareTo(BigInteger.ZERO) > 0;

//                    System.out.println("FlipA=" + flipA + "(" + i + " & " + flipIds + " = " + (i & flipIds) + ") FlipB=" + flipB + "(" + j + " & " + flipIds + " = " + (j & flipIds) + ") ");

//                    System.out.println(tileA.getTileId() + "(" + flipA + ") " + tileA.getPatterns(flipA) + " compared against " + tileB.getTileId() + "(" + flipB + ") " + tileB.getPatterns(flipB));
                    if (!Collections.disjoint(tileA.getPatterns(flipA), tileB.getPatterns(flipB))) {
//                        System.out.println(tileA.getTileId() + "(" + flipA + ") matches " + tileB.getTileId() + "(" + flipB + ") ");
                        tileA.linkTile(tileB);
                        tileB.linkTile(tileA);
                    }
                }

                if (tileA.getLinkCount() < 2) {
                    badLinkCount = true;
                    break;
                }
            }

            if (!badLinkCount) {
                int twoLinkCount = 0;
                System.out.print("Candidates: ");
                for (final Tile tile : tiles) {
                    System.out.print("[" + tile.getTileId() + ":" + tile.getLinkCount() + "]");
                    if (tile.getLinkCount() == 2) {
                        twoLinkCount++;
                    }
                }
                System.out.print("\n");
                if (twoLinkCount == 4) {
                    System.out.println("Found!");
                    break;
                }
            }
        }

        long total = 1;
        for (final Tile tile : tiles) {
            if (tile.getLinkCount() == 2) {
                total *= tile.getTileId();
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("The product of all corners multiplied is " + total + " in " + timeElapsed + "ms");
    }

    private static class Tile {
        private final int tileId;
        private final List<String> patterns;
        private final List<String> reversedPatterns;
        private final List<Tile> linkedTiles;

        public Tile(final int tileId, final List<String> tileContent) {
            this.tileId = tileId;
            this.patterns = new ArrayList<>();
            this.reversedPatterns = new ArrayList<>();
            extractPatterns(tileContent);
            this.linkedTiles = new ArrayList<>();
        }

        public int getTileId() {
            return tileId;
        }

        public List<String> getPatterns(boolean isFlipped) {
            if (isFlipped) {
                return reversedPatterns;
            } else {
                return patterns;
            }
        }

        public void linkTile(final Tile tile) {
            linkedTiles.add(tile);
        }

        public int getLinkCount() {
            return linkedTiles.size();
        }

        public void resetLinks() {
            this.linkedTiles.clear();
        }

        private void extractPatterns(final List<String> tileContent) {
            final int sideLength = tileContent.size();

            final String firstRow = tileContent.get(0);
            patterns.add(firstRow);
            reversedPatterns.add(reverse(firstRow));

            final StringBuilder rightPattern = new StringBuilder();
            for (final String row : tileContent) {
                rightPattern.append(row.charAt(sideLength - 1));
            }
            patterns.add(rightPattern.toString());
            reversedPatterns.add(rightPattern.reverse().toString());

            final String lastRow = tileContent.get(sideLength - 1);
            patterns.add(reverse(lastRow));
            reversedPatterns.add(lastRow);

            final StringBuilder leftPattern = new StringBuilder();
            for (final String row : tileContent) {
                leftPattern.append(row.charAt(0));
            }
            reversedPatterns.add(leftPattern.toString());
            patterns.add(leftPattern.reverse().toString()); // It's important it is reversed after the previous line is executed
        }

        private String reverse(final String firstRow) {
            final StringBuilder stringBuilder = new StringBuilder(firstRow);
            return stringBuilder.reverse().toString();
        }
    }
}
