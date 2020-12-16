package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final Pattern maskPattern = Pattern.compile("^mask = ([01X]+)$", Pattern.CASE_INSENSITIVE);
        final Pattern memAssignPattern = Pattern.compile("^mem\\[(\\d+)\\] = (\\d+)$", Pattern.CASE_INSENSITIVE);

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

                    final Mem mem = new Mem(Integer.parseInt(memAssignMatcher.group(1)), BigInteger.valueOf(Long.parseUnsignedLong(memAssignMatcher.group(2))));
                    newMaskMem.addMem(mem);
                }
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + maskMems.size() + " masks with memory assignations");

        final Instant start = Instant.now();

        final Map<Integer, BigInteger> memory = new HashMap<>();

        for (final MaskMem maskMem : maskMems) {
            BigInteger orMask = BigInteger.ZERO;
//            String orMaskString = "";
            BigInteger andMask = BigInteger.ZERO;
//            String andMaskString = "";
            final String mask = maskMem.getMask();
            for(int i=0; i< mask.length(); i++) {
                final int bitPosition = mask.length() - i - 1;
                if (mask.charAt(i) == '1') {
                    orMask = orMask.add(BigInteger.ONE.shiftLeft(bitPosition));
                    andMask = andMask.add(BigInteger.ONE.shiftLeft(bitPosition));
//                    orMaskString += "1";
//                    andMaskString += "1";
                } else if (mask.charAt(i) == 'X') {
                    andMask = andMask.add(BigInteger.ONE.shiftLeft(bitPosition));
//                    orMaskString += "0";
//                    andMaskString += "1";
                } else {
                    // nothing
//                    orMaskString += "0";
//                    andMaskString += "0";
                }
            }
//            System.out.println("[" + mask + "]--");
//            System.out.println("[" + orMaskString + "]OR -> " + orMask);
//            System.out.println("[" + andMaskString + "]AND -> " + andMask);

            for (final Mem mem : maskMem.getMems()) {
                final BigInteger newValue = mem.getValue().and(andMask).or(orMask);
//                System.out.println(mem.getValue() + " m[" + mask + "] = " + newValue);
                memory.put(mem.getPosition(), newValue);
            }
        }

//        System.out.println("Memory " + memory.toString());

        final BigInteger sum = memory.values().stream().reduce(BigInteger.ZERO, BigInteger::add);

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

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
        private final Integer position;
        private final BigInteger value;

        private Mem(final Integer position, final BigInteger value) {
            this.position = position;
            this.value = value;
        }

        public Integer getPosition() {
            return position;
        }

        public BigInteger getValue() {
            return value;
        }
    }
}
