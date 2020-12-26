package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

// Big brain here thought one did not know the Subject Number
//   and was suppose to figure it out from the two public keys...
// Just left it here as some day I may laugh at the +8h wasted on this
public class Day25A2 {
    private static final int MAGIC_CONSTANT = 20201227;
    private static final boolean LOG = true;
    private static final boolean LOG_LIST = false;
    private static final boolean LOG_LOOPS = false;
    private static final boolean LOG_MATCHES = false;
    private static final boolean OP_LOG = false;

    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final String readLineCard = bufferedReader.readLine();
        final String readLineDoor = bufferedReader.readLine();
        final long cardPublicKey = Long.parseLong(readLineCard);
        final long doorPublicKey = Long.parseLong(readLineDoor);

        bufferedReader.close();

        if (LOG) System.out.println("CardPublicKey: " + cardPublicKey);
        if (LOG) System.out.println("DoorPublicKey: " + doorPublicKey);

        final Instant start = Instant.now();

        final List<Long> cardInversePercentage = inversePercentages(cardPublicKey);
        final List<Long> doorInversePercentage = inversePercentages(doorPublicKey);

        if (LOG) System.out.println("cardInversePercentage: " + cardInversePercentage.size());
        if (LOG) System.out.println("doorInversePercentage: " + doorInversePercentage.size());

        final Set<Long> cardDivisors = cardInversePercentage.parallelStream()
                .map(Day25A2::getDivisors)
                .flatMap(Collection::stream)
                .collect(toSet());
        final Set<Long> doorDivisors = doorInversePercentage.parallelStream()
                .map(Day25A2::getDivisors)
                .flatMap(Collection::stream)
                .collect(toSet());

        if (LOG) System.out.println("cardDivisors: " + cardDivisors.size());
        if (LOG) System.out.println("doorDivisors: " + doorDivisors.size());

        final List<Pair<Long, Long>> subjectNumbersPairs = cardDivisors.stream()
                .filter(doorDivisors::contains)
                .map(it -> Pair.of(it, it))
                .collect(toList());

        System.out.println("subjectNumbersPairs: " + subjectNumbersPairs.size());
        if (LOG_LIST) {
            for (final Pair<Long, Long> subjectNumbersPair : subjectNumbersPairs) {
                System.out.print(subjectNumbersPair.fst + ", ");
            }
            System.out.println();
        }


        final int[] cards = new int[subjectNumbersPairs.size()];
        final int[] doors = new int[subjectNumbersPairs.size()];

        final Long subjectNumber;
        final int cardLoops;
        final int doorLoops;
        int loops = 2;
        do {
            Integer stopI = null;
            if (LOG_LOOPS) System.out.println("loops " + loops);
            for (int i = 0; i < subjectNumbersPairs.size(); i++) {
                final Pair<Long, Long> subjectNumberPair = subjectNumbersPairs.get(i);

                final long calculatedPublicKey = subjectNumberPair.fst;
                final Long testSubjectNumber = subjectNumberPair.snd;
                if (calculatedPublicKey < 0) continue;

                final long value = transform(calculatedPublicKey, testSubjectNumber);

                if (value == cardPublicKey && cards[i] == 0) {
                    if (LOG_MATCHES)
                        System.out.println("card loop " + loops + " subjectNumber:" + testSubjectNumber + " value:" + value);

                    cards[i] = loops;

                    if (doors[i] != 0) {
                        stopI = i;
                        break;
                    }
                }
                if (value == doorPublicKey && doors[i] == 0) {
                    if (LOG_MATCHES)
                        System.out.println("door loop " + loops + " subjectNumber:" + testSubjectNumber + " value:" + value);

                    doors[i] = loops;

                    if (cards[i] != 0) {
                        stopI = i;
                        break;
                    }
                }

                if (LOG_LOOPS) System.out.println("loop " + loops + " i:" + i + " value:" + value);
                subjectNumbersPairs.set(i, Pair.of(value, testSubjectNumber));
            }

            if (stopI != null) {
                subjectNumber = subjectNumbersPairs.get(stopI).snd;
                cardLoops = cards[stopI];
                doorLoops = doors[stopI];
                break;
            }
            loops++;
        } while (true);

        if (LOG) System.out.println("subjectNumber " + subjectNumber);
        if (LOG) System.out.println("cardLoops " + cardLoops);
        if (LOG) System.out.println("doorLoops " + doorLoops);

        long encryptedCardValue = 1;
        for (int i = 0; i < doorLoops; i++) {
//            if (LOG) System.out.println(i + " card loop: " + encryptedCardValue);
            encryptedCardValue = transform(encryptedCardValue, cardPublicKey);
        }
        System.out.println(cardPublicKey + " card after " + doorLoops + " loops: " + encryptedCardValue + " encryptionKey");

        long encryptedDoorValue = 1;
        for (int i = 0; i < cardLoops; i++) {
//            if (LOG) System.out.println(i + " door loop: " + encryptedDoorValue);
            encryptedDoorValue = transform(encryptedDoorValue, doorPublicKey);
        }
        System.out.println(doorPublicKey + " door after " + cardLoops + " loops: " + encryptedDoorValue + " encryptionKey");


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("In " + timeElapsed + "ms");
    }

    private static List<Long> inversePercentages(final Long publicKeyValue) {
        final List<Long> inversePercentages = new ArrayList<>();
//        final long temp = (Long.MAX_VALUE - publicKeyValue) / MAGIC_CONSTANT;
//        if (OP_LOG) System.out.println("publicKeyValue " + publicKeyValue + " temp:" + temp);
        for (long i = 0; i < 100000; i++) { //Long.MAX_VALUE = 456 574 842 550
            inversePercentages.add((i * MAGIC_CONSTANT) + publicKeyValue);
        }
        return inversePercentages;
    }

    private static Set<Long> getDivisors(final Long value) {
        final Set<Long> divisors = new HashSet<>();
        if (OP_LOG) System.out.println("Divisors " + value + " until:" + Math.sqrt(value));
        for (long i = 2; i <= Math.sqrt(value); i++) {
            if (value % i == 0) {
                divisors.add(i);
                divisors.add(value / i);
            }
        }
        return divisors;
    }

    private static Long transform(final Long value, final long subjectNumber) {
        return (value * subjectNumber) % MAGIC_CONSTANT;
    }
}
