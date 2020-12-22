package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Day22B {
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

        recursiveGame(player1Cards, player2Cards);


        final int cardTotal;
        final Queue<Integer> winner;
        if (player1Cards.isEmpty()) {
            winner = player2Cards;
            cardTotal = player2Cards.size();
        } else {
            winner = player1Cards;
            cardTotal = player1Cards.size();
        }

        Long total = 0L;
        for (int i = cardTotal; i > 0; i--) {
            total += i * winner.poll();
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Winning player's score is " + total + " in " + timeElapsed + "ms");
    }

    private static boolean recursiveGame(final Queue<Integer> player1Cards, final Queue<Integer> player2Cards) {
        final Set<Pair<Integer, Integer>> gameHashes = new HashSet<>();
        boolean player1Won;
        do {
//            System.out.println("Player 1's deck: " + player1Cards);
//            System.out.println("Player 2's deck: " + player2Cards);

            final Pair<Integer, Integer> bothPlayersHash = Pair.of(player1Cards.hashCode(), player2Cards.hashCode());
            if (gameHashes.contains(bothPlayersHash)) {
//                System.out.println("--HASH FOUND!!!");
                return true;
            }
            gameHashes.add(bothPlayersHash);

            final Integer player1Card = player1Cards.poll();
            final Integer player2Card = player2Cards.poll();

//            System.out.println(player1Card + " vs " + player2Card);

            if (player1Card <= player1Cards.size() && player2Card <= player2Cards.size()) {
//                System.out.println("-->>recursiveGame!!");

                final Queue<Integer> recursivePlayer1Cards = cloneNItems(player1Cards, player1Card);
                final Queue<Integer> recursivePlayer2Cards = cloneNItems(player2Cards, player2Card);

                player1Won = recursiveGame(recursivePlayer1Cards, recursivePlayer2Cards);

//                System.out.println("--<<recursiveGame!!");
            } else {
                player1Won = (player1Card > player2Card);
            }

//            System.out.println("Player " + (player1Won ? "1" : "2") + " won\n");

            if (player1Won) {
                player1Cards.add(player1Card);
                player1Cards.add(player2Card);
            } else {
                player2Cards.add(player2Card);
                player2Cards.add(player1Card);
            }
        } while (!player1Cards.isEmpty() && !player2Cards.isEmpty());

        return player1Won;
    }

    private static Queue<Integer> cloneNItems(final Queue<Integer> playerCards, final int size) {
        final Queue<Integer> recursivePlayerCards = new LinkedList<>();
        final Iterator<Integer> iterator = playerCards.iterator();
        for (int i = 0; i < size; i++) {
            recursivePlayerCards.add(iterator.next());
        }
        return recursivePlayerCards;
    }
}
