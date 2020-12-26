package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

public class Day25A {
    private static final int MAGIC_CONSTANT = 20201227;
    private static final int SUBJECT_NUMBER = 7;
    private static final boolean LOG = false;
    private static final boolean LOG_LOOPS = false;
    private static final boolean LOG_MATCHES = false;

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

        Integer cardLoops = null;
        Integer doorLoops = null;
        long calculatedPublicKey = 1;
        int loops = 1;
        do {
            if (LOG_LOOPS) System.out.println("loops " + loops);

            calculatedPublicKey = transform(calculatedPublicKey, SUBJECT_NUMBER);

            if (calculatedPublicKey == cardPublicKey && cardLoops == null) {
                if (LOG_MATCHES) System.out.println("card loop " + loops + " value:" + calculatedPublicKey);

                cardLoops = loops;
            }

            if (calculatedPublicKey == doorPublicKey && doorLoops == null) {
                if (LOG_MATCHES) System.out.println("door loop " + loops + " value:" + calculatedPublicKey);

                doorLoops = loops;
            }

            loops++;
        } while (doorLoops == null || cardLoops == null);

        if (LOG) System.out.println("cardLoops " + cardLoops);
        if (LOG) System.out.println("doorLoops " + doorLoops);

        long encryptedCardValue = 1;
        for (int i = 0; i < doorLoops; i++) {
            encryptedCardValue = transform(encryptedCardValue, cardPublicKey);
        }
        System.out.println(cardPublicKey + " card after " + doorLoops + " loops: " + encryptedCardValue + " encryptionKey");

        long encryptedDoorValue = 1;
        for (int i = 0; i < cardLoops; i++) {
            encryptedDoorValue = transform(encryptedDoorValue, doorPublicKey);
        }
        System.out.println(doorPublicKey + " door after " + cardLoops + " loops: " + encryptedDoorValue + " encryptionKey");


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("In " + timeElapsed + "ms");
    }

    private static Long transform(final Long value, final long subjectNumber) {
        return (value * subjectNumber) % MAGIC_CONSTANT;
    }
}
