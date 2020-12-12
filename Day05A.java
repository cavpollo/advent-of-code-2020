package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day05A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

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

        int highestSeatId = 0;
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

            System.out.println("BoardingPass: " + boardingPass + " row: " + row + "col: " + col);

            final int newSeatId = (row * 8) + col;
            if (highestSeatId < newSeatId) {
                highestSeatId = newSeatId;
            }
        }

        System.out.println("Highest BoardingPass: " + highestSeatId);
    }
}
