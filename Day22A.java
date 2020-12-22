package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

public class Day22A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        bufferedReader.readLine(); // Skip Player 1

        final Queue<Integer> player1Cards = new LinkedList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                player1Cards.add(Integer.parseInt(readLine.trim()));
            } else {
                break;
            }
        } while (true);

        bufferedReader.readLine(); // Skip Player 2

        final Queue<Integer> player2Cards = new LinkedList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                player2Cards.add(Integer.parseInt(readLine.trim()));
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + player1Cards.size() + " & " + player2Cards.size() + " cards");

        final Instant start = Instant.now();

        final int cardTotal = player1Cards.size() + player2Cards.size();

        do {
            final Integer player1Card = player1Cards.poll();
            final Integer player2Card = player2Cards.poll();

            if (player1Card > player2Card) {
                player1Cards.add(player1Card);
                player1Cards.add(player2Card);
            } else {
                player2Cards.add(player2Card);
                player2Cards.add(player1Card);
            }
        } while (!player1Cards.isEmpty() && !player2Cards.isEmpty());

        final Queue<Integer> winner;
        if (player1Cards.isEmpty()) {
            winner = player2Cards;
        } else {
            winner = player1Cards;
        }

        Long total = 0L;
        for (int i = cardTotal; i > 0; i--) {
            total += i * winner.poll();
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Winning player's score is " + total + " in " + timeElapsed + "ms");
    }
}
