package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day17A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");


        int lineCount = 0;
        Set<XYZ> cubes = new HashSet<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (int i = 0; i < readLine.trim().length(); i++) {
                    if (readLine.charAt(i) == '#') {
                        cubes.add(new XYZ(i, lineCount, 0));
                    }
                }
                lineCount++;
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + lineCount + " lines");
        System.out.println("Got " + cubes.size() + " cubes");

        final Instant start = Instant.now();

        for (int i = 1; i <= 6; i++) {
            final Map<XYZ, Integer> neighborCubeCounter = new HashMap<>();
            final Set<XYZ> newCubes = new HashSet<>();
            for (final XYZ cube : cubes) {
                addUpNeighbors(cube, neighborCubeCounter);

                final int neighborCount = countNeighbors(cubes, cube);
                if (neighborCount == 2 || neighborCount == 3) {
                    newCubes.add(cube);
                }
            }

            for (final Map.Entry<XYZ, Integer> neighborCubeCount : neighborCubeCounter.entrySet()) {
                if (neighborCubeCount.getValue() == 3 && !cubes.contains(neighborCubeCount.getKey())) {
                    newCubes.add(neighborCubeCount.getKey());
                }
            }

            cubes = newCubes;
        }


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Cube count on the 6th iteration is " + cubes.size() + " in " + timeElapsed + "ms");
    }

    private static int countNeighbors(final Set<XYZ> cubes, final XYZ cube) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue;
                    }

                    final XYZ neighbor = new XYZ(cube.getX() + i, cube.getY() + j, cube.getZ() + k);
                    if (cubes.contains(neighbor)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static void addUpNeighbors(final XYZ cube, final Map<XYZ, Integer> neighborCubeCounter) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue;
                    }

                    final XYZ neighbor = new XYZ(cube.getX() + i, cube.getY() + j, cube.getZ() + k);
                    final Integer count = neighborCubeCounter.get(neighbor);
                    if (count == null) {
                        neighborCubeCounter.put(neighbor, 1);
                    } else {
                        neighborCubeCounter.put(neighbor, count + 1);
                    }
                }
            }
        }
    }

    private static class XYZ {
        private final Integer x;
        private final Integer y;
        private final Integer z;

        private XYZ(final Integer x, final Integer y, final Integer z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Integer getX() {
            return x;
        }

        public Integer getY() {
            return y;
        }

        public Integer getZ() {
            return z;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final XYZ xyz = (XYZ) o;
            return Objects.equals(x, xyz.x) &&
                    Objects.equals(y, xyz.y) &&
                    Objects.equals(z, xyz.z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
