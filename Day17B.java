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

public class Day17B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");


        int lineCount = 0;
        Set<WXYZ> cubes = new HashSet<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (int i = 0; i < readLine.trim().length(); i++) {
                    if (readLine.charAt(i) == '#') {
                        cubes.add(new WXYZ(i, lineCount, 0, 0));
                    }
                }
                lineCount++;
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + lineCount + " lines");
        System.out.println("Got " + cubes.size() + " hypercubes");

        final Instant start = Instant.now();

        for (int i = 1; i <= 6; i++) {
            final Map<WXYZ, Integer> neighborCubeCounter = new HashMap<>();
            final Set<WXYZ> newCubes = new HashSet<>();
            for (final WXYZ cube : cubes) {
                addUpNeighbors(cube, neighborCubeCounter);

                final int neighborCount = countNeighbors(cubes, cube);
                if (neighborCount == 2 || neighborCount == 3) {
                    newCubes.add(cube);
                }
            }

            for (final Map.Entry<WXYZ, Integer> neighborCubeCount : neighborCubeCounter.entrySet()) {
                if (neighborCubeCount.getValue() == 3 && !cubes.contains(neighborCubeCount.getKey())) {
                    newCubes.add(neighborCubeCount.getKey());
                }
            }

            cubes = newCubes;
        }


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Hypercubes count on the 6th iteration is " + cubes.size() + " in " + timeElapsed + "ms");
    }

    private static int countNeighbors(final Set<WXYZ> cubes, final WXYZ cube) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (i == 0 && j == 0 && k == 0 && l == 0) {
                            continue;
                        }

                        final WXYZ neighbor = new WXYZ(cube.getW() + i, cube.getX() + j, cube.getY() + k, cube.getZ() + l);
                        if (cubes.contains(neighbor)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private static void addUpNeighbors(final WXYZ cube, final Map<WXYZ, Integer> neighborCubeCounter) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (i == 0 && j == 0 && k == 0 && l == 0) {
                            continue;
                        }

                        final WXYZ neighbor = new WXYZ(cube.getW() + i, cube.getX() + j, cube.getY() + k, cube.getZ() + l);
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
    }

    private static class WXYZ {
        private final Integer w;
        private final Integer x;
        private final Integer y;
        private final Integer z;

        private WXYZ(final Integer w, final Integer x, final Integer y, final Integer z) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Integer getW() {
            return w;
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
            final WXYZ WXYZ = (WXYZ) o;
            return Objects.equals(w, WXYZ.w) &&
                    Objects.equals(x, WXYZ.x) &&
                    Objects.equals(y, WXYZ.y) &&
                    Objects.equals(z, WXYZ.z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(w, x, y, z);
        }
    }
}
