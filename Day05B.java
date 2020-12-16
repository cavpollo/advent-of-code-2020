package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day05B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final ArrayList<String> boardingPasses = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                boardingPasses.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + boardingPasses.size() + " BoardingPasses");

        final List<Pair<Integer, Integer>> seats = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 8; j++) {
                seats.add(Pair.of(i, j));
            }
        }

        for (final String boardingPass : boardingPasses) {
            int row = 0;
            int col = 0;
            for (int i = 0; i < boardingPass.length(); i++) {
//                System.out.println(boardingPass.charAt(i) + " i: " + i);
                if (i < boardingPass.length() - 3) {
                    if (boardingPass.charAt(i) == 'B') {
                        row += 1 << (6 - i);
//                        System.out.println("row: " + row);
                    }
                } else {
                    if (boardingPass.charAt(i) == 'R') {
                        col += 1 << (9 - i);
//                        System.out.println("col: " + col);
                    }
                }
            }

            System.out.println("Remove BoardingPass: " + boardingPass + " row: " + row + "col: " + col);

            seats.remove(Pair.of(row, col));
        }


        for (final Pair<Integer, Integer> seat : seats) {
            final int seatId = (seat.fst * 8) + seat.snd;
            System.out.println("LeftOver SeatId: " + seatId + " row: " + seat.fst + "col: " + seat.snd);
        }
    }
}
