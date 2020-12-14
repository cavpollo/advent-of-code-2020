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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14B {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        Pattern maskPattern = Pattern.compile("^mask = ([01X]+)$", Pattern.CASE_INSENSITIVE);
        Pattern memAssignPattern = Pattern.compile("^mem\\[(\\d+)\\] = (\\d+)$", Pattern.CASE_INSENSITIVE);

        final ArrayList<MaskMem> maskMems = new ArrayList<>();
        MaskMem newMaskMem = null;
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher maskMatcher = maskPattern.matcher(readLine.trim());
                final boolean foundMask = maskMatcher.find();

                if (foundMask) {
                    newMaskMem = new MaskMem(maskMatcher.group(1));
                    maskMems.add(newMaskMem);
                } else {
                    final Matcher memAssignMatcher = memAssignPattern.matcher(readLine.trim());
                    final boolean foundMemAssign = memAssignMatcher.find();

                    final Mem mem = new Mem(BigInteger.valueOf(Integer.parseInt(memAssignMatcher.group(1))),
                            BigInteger.valueOf(Long.parseUnsignedLong(memAssignMatcher.group(2))));
                    newMaskMem.addMem(mem);
                }
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + maskMems.size() + " masks with memory assignations");

        final Instant start = Instant.now();

        final Map<BigInteger, BigInteger> memory = new HashMap<>();

        for (final MaskMem maskMem : maskMems) {
            int xCount = 0;
            final String mask = maskMem.getMask();
            for (int i = 0; i < mask.length(); i++) {
                if (mask.charAt(i) == 'X') {
                    xCount++;
                }
            }

            final long combinations = (long) Math.pow(2, xCount) - 1;
            final List<Pair<BigInteger, BigInteger>> locations = new ArrayList<>();
            for (long i = 0; i <= combinations; i++) {
                BigInteger andMask = BigInteger.ZERO;
                BigInteger orMask = BigInteger.ZERO;
//                String orMaskString = "";
//                String andMaskString = "";

                int xPosition = 0;
                for (int j = 0; j < mask.length(); j++) {
                    orMask = orMask.shiftLeft(1);
                    andMask = andMask.shiftLeft(1);
                    if (mask.charAt(j) == 'X') {
//                        System.out.println(i + " vs" + (1 << xPosition));
                        if ((i & (1 << xPosition)) > 0) {
                            orMask = orMask.add(BigInteger.ONE);
//                            andMaskString += "0";
//                            orMaskString += "1";
                        } else {
//                            andMaskString += "0";
//                            orMaskString += "0";
                        }
                        xPosition++;
                    } else if (mask.charAt(j) == '1') {
                        andMask = andMask.add(BigInteger.ONE);
                        orMask = orMask.add(BigInteger.ONE);
//                        orMaskString += "1";
//                        andMaskString += "1";
                    } else {
                        andMask = andMask.add(BigInteger.ONE);
//                        andMaskString += "1";
//                        orMaskString += "0";
                    }
                }

//                System.out.println(i + " [" + mask + "]mask");
//                System.out.println(i + " [" + orMaskString + "]OR -> " + orMask);
//                System.out.println(i + " [" + andMaskString + "]AND -> " + andMask);
                locations.add(Pair.of(andMask, orMask));
            }

//            System.out.println("Combinations " + combinations);

            for (final Mem mem : maskMem.getMems()) {
                for (final Pair<BigInteger, BigInteger> location : locations) {
                    final BigInteger memoryPosition = mem.getPosition().and(location.fst).or(location.snd);
//                    System.out.println("MemoryPosition " + memoryPosition + " with " + mem.getValue());
                    memory.put(memoryPosition, mem.getValue());
                }
            }
        }

        System.out.println("Memory " + memory.toString());

        final BigInteger sum = memory.values().stream().reduce(BigInteger.ZERO, BigInteger::add);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();  //in millis

        System.out.println("Sum of memory is " + sum + " in " + timeElapsed + "ms");
    }

    private static class MaskMem {
        private final String mask;
        private final ArrayList<Mem> mems;

        private MaskMem(final String mask) {
            this.mask = mask;
            mems = new ArrayList<>();
        }

        public void addMem(final Mem mem) {
            mems.add(mem);
        }

        public String getMask() {
            return mask;
        }

        public ArrayList<Mem> getMems() {
            return mems;
        }
    }

    private static class Mem {
        private final BigInteger position;
        private final BigInteger value;

        private Mem(final BigInteger position, final BigInteger value) {
            this.position = position;
            this.value = value;
        }

        public BigInteger getPosition() {
            return position;
        }

        public BigInteger getValue() {
            return value;
        }
    }
}
