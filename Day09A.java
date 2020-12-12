package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day09A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        final ArrayList<Long> codeList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                codeList.add(Long.parseLong(readLine.trim()));
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + codeList.size() + " actions");

        int i = 25;
        for (; i < codeList.size(); i++) {
            final Long code = codeList.get(i);
            final List<Long> sublist = codeList.subList(0, i);
            boolean fine = false;
            for (int j = 0; j < i; j++) {
                if (sublist.contains(code - codeList.get(j))) {
//                    System.out.println("Found");
                    fine = true;
                    break;
                }
            }
            if (!fine) {
                break;
            }
        }

        System.out.println("Bad code (" + i + "): " + codeList.get(i));
    }
}
