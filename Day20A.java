package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20A {
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

        final Map<Pair<Integer, Integer>, PlacedTile> grid = new HashMap<>();
        final Set<Pair<Integer, Integer>> emptyGridPositions = new HashSet<>();
        emptyGridPositions.add(Pair.of(0, 0));

        final int maxTileLength = (int) Math.sqrt(tiles.size());

        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        do {
            final Pair<Integer, Integer> emptyGridPosition = emptyGridPositions.iterator().next();

//            System.out.println("Considering empty position: " + emptyGridPosition);

            //Check if width won't be higher than max square width
            //Check if height won't be higher than max square height
            if ((maxX - minX + 1 < maxTileLength || (minX <= emptyGridPosition.fst && emptyGridPosition.fst <= maxX)) &&
                    (maxY - minY + 1 < maxTileLength || (minY <= emptyGridPosition.snd && emptyGridPosition.snd <= maxY))) {

//                System.out.println("Considering empty position: " + emptyGridPosition);

                for (int l = 0; l < tiles.size(); l++) {
                    final Tile tile = tiles.get(l);

                    final PlacedTile fittingTile = fits(tile, emptyGridPosition, grid);
                    if (fittingTile != null) {
                        grid.put(emptyGridPosition, fittingTile);

                        if (emptyGridPosition.fst < minX) minX = emptyGridPosition.fst;
                        if (emptyGridPosition.fst > maxX) maxX = emptyGridPosition.fst;
                        if (emptyGridPosition.snd < minY) minY = emptyGridPosition.snd;
                        if (emptyGridPosition.snd > maxY) maxY = emptyGridPosition.snd;

//                        System.out.println("minX:" + minX + " maxX:" + maxX + "minY:" + minY + " maxY:" + maxY);

                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                if ((i == 0 && j == 0) || (i != 0 && j != 0)) continue;

                                final Pair<Integer, Integer> newPosition = Pair.of(emptyGridPosition.fst + i, emptyGridPosition.snd + j);
                                if (!grid.containsKey(newPosition)) {
//                                    System.out.println("Storing empty position: " + newPosition);
                                    emptyGridPositions.add(newPosition);
                                }
                            }
                        }

//                        System.out.println("Tile " + tile.getTileId() + " fits at " + emptyGridPosition + " with rotate:" + fittingTile.rotate + " flip:" + fittingTile.isFlipped);

                        tiles.remove(tile);
                        break;
                    }
                }
            }

            emptyGridPositions.remove(emptyGridPosition);
        } while (!emptyGridPositions.isEmpty() && !tiles.isEmpty());

        if (tiles.size() > 0) {
            throw new RuntimeException("Couldn't fit " + tiles.size() + " pieces: " + tiles);
        }

//        final Pair<Integer, Integer> rightTopCorner = getTopRightCorner(grid.keySet());
//        final Pair<Integer, Integer> leftBottomCorner = getBottomLeftCorner(grid.keySet());
//
//        final Pair<Integer, Integer> leftTopCorner = Pair.of(leftBottomCorner.fst, rightTopCorner.snd);
//        final Pair<Integer, Integer> rightBottomCorner = Pair.of(rightTopCorner.fst, leftBottomCorner.snd);

        final Pair<Integer, Integer> rightTopCorner = Pair.of(maxX, maxY);
        final Pair<Integer, Integer> leftBottomCorner = Pair.of(minX, minY);
        final Pair<Integer, Integer> leftTopCorner = Pair.of(minX, maxY);
        final Pair<Integer, Integer> rightBottomCorner = Pair.of(maxX, minY);

//        System.out.println("Corners: ");
//        System.out.println(leftTopCorner + " " + rightTopCorner);
//        System.out.println(leftBottomCorner + " " + rightBottomCorner);
//        System.out.println(leftTopCorner + ":" + grid.get(leftTopCorner).getTile().getTileId() + " " + rightTopCorner + ":" + grid.get(rightTopCorner).getTile().getTileId());
//        System.out.println(leftBottomCorner + ":" + grid.get(leftBottomCorner).getTile().getTileId() + " " + rightBottomCorner + ":" + grid.get(rightBottomCorner).getTile().getTileId());

        final BigInteger rightTopCornerValue = BigInteger.valueOf(grid.get(rightTopCorner).getTile().getTileId());
        final BigInteger rightBottomCornerValue = BigInteger.valueOf(grid.get(rightBottomCorner).getTile().getTileId());
        final BigInteger leftBottomCornerValue = BigInteger.valueOf(grid.get(leftBottomCorner).getTile().getTileId());
        final BigInteger leftTopCornerValue = BigInteger.valueOf(grid.get(leftTopCorner).getTile().getTileId());

        final BigInteger total = rightTopCornerValue.multiply(rightBottomCornerValue).multiply(leftBottomCornerValue).multiply(leftTopCornerValue);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("The product of all corners multiplied is " + total + " in " + timeElapsed + "ms");
    }

    private static PlacedTile fits(final Tile testTile, final Pair<Integer, Integer> emptyGridPosition, final Map<Pair<Integer, Integer>, PlacedTile> grid) {
        for (int shouldFlipNum = 0; shouldFlipNum <= 1; shouldFlipNum++) {
            final boolean shouldFlip = shouldFlipNum == 1;

            for (int rotation = 0; rotation <= 3; rotation++) {

                boolean fitsAgainstAdjacentTiles = true;
                // Compare all 4 directions of the tile (N, S, E, W)
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((i == 0 && j == 0) || (i != 0 && j != 0)) continue;

                        final Pair<Integer, Integer> position = Pair.of(emptyGridPosition.fst + i, emptyGridPosition.snd + j);
                        final PlacedTile placedTile = grid.get(position);

                        if (placedTile != null) {
                            final int emptyTileSide = (j == 0 ? 2 - i : 1 - j);
                            final int placedTileSide = (emptyTileSide + 2) % 4;

                            final String testTilePattern = reverseString(testTile.getPattern(emptyTileSide, rotation, shouldFlip));
                            final String placedTilePattern = placedTile.getPattern(placedTileSide);

//                            System.out.println(tile.getTileId() + "[" + emptyTileSide + "," + rotation + "," + shouldFlip + "] " + tilePattern + " compared against " +
//                                    placedTile.getTile().getTileId() + "[" + (emptyGridPosition.fst + i) + "," + (emptyGridPosition.snd + j) + "]" + "[" + placedTileSide + "] " + placedTilePattern);
                            if (!testTilePattern.equals(placedTilePattern)) {
                                fitsAgainstAdjacentTiles = false;
                                break;
                            }
//                            System.out.println(testTile.getTileId() + "[" + emptyTileSide + "," + rotation + "," + shouldFlip + "] " + testTilePattern + " fits against " +
//                                    placedTile.getTile().getTileId() + "[" + (emptyGridPosition.fst + i) + "," + (emptyGridPosition.snd + j) + "]" + "[" + placedTileSide + "] " + placedTilePattern);
                        }
                    }

                    if (!fitsAgainstAdjacentTiles) {
                        break;
                    }
                }

                if (!fitsAgainstAdjacentTiles) {
                    continue;
                }

                return new PlacedTile(testTile, rotation, shouldFlip);
            }
        }

        return null; // Didn't fit any
    }

    private static String reverseString(final String string) {
        final StringBuilder stringBuilder = new StringBuilder(string);
        return stringBuilder.reverse().toString();
    }

    private static class Tile {
        private final int tileId;
        private final List<String> patterns;

        public Tile(final int tileId, final List<String> tileContent) {
            this.tileId = tileId;
            this.patterns = new ArrayList<>();

            final int sideLength = tileContent.size();

            final String firstRow = tileContent.get(0);
            patterns.add(firstRow); // 0 = N

            final StringBuilder rightPattern = new StringBuilder();
            final StringBuilder leftPattern = new StringBuilder();
            for (final String row : tileContent) {
                leftPattern.append(row.charAt(0));
                rightPattern.append(row.charAt(sideLength - 1));
            }
            patterns.add(rightPattern.toString()); // 1 = E

            final String lastRow = tileContent.get(sideLength - 1);
            patterns.add(reverseString(lastRow)); // 2 = S

            // INSERTION ORDER IS IMPORTANT!!!
            patterns.add(leftPattern.reverse().toString()); // 3 = W
        }

        public int getTileId() {
            return tileId;
        }

        //SIDE
        // 0 = top
        // 1 = right
        // 2 = bottom
        // 3 = left
        //ROTATION
        // 0 = 0
        // 1 = 90
        // 2 = 180
        // 3 = 270
        // flipped (vertical flip: 0 -> 2 and 2 -> 0)
        public String getPattern(final int side, final int rotation, final boolean flipped) {
            if (flipped) {
                final int newPos;
                if (side % 2 == 0) {
                    newPos = (side - rotation + 6) % 4;
                } else {
                    newPos = (side - rotation + 4) % 4;
                }
                return reverseString(patterns.get(newPos));
            } else {
                return patterns.get((side + rotation) % 4);
            }
        }
    }

    private static class PlacedTile {
        private final Tile tile;
        private final int rotate;
        private final boolean isFlipped;

        public PlacedTile(final Tile tile, final int rotate, final boolean isFlipped) {
            this.tile = tile;
            this.rotate = rotate;
            this.isFlipped = isFlipped;
        }

        public Tile getTile() {
            return tile;
        }

        public String getPattern(final int side) {
            return tile.getPattern(side, rotate, isFlipped);
        }
    }
}
