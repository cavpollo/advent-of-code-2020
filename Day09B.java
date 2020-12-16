package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day09B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

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

        final Long theCode = codeList.get(i);
        System.out.println("Bad code (" + i + "): " + theCode);

        int upper = 1;
        int lower = 0;
        for (; lower < i; lower++) {
            boolean found = false;
            for (; ; upper++) {
                final Long sum = codeList.subList(lower, upper).stream().reduce(0L, Long::sum);
                if (sum.equals(theCode)) {
                    found = true;
                    break;
                } else if (sum > theCode) {
                    //Increase Upper and continue
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        final List<Long> subList = codeList.subList(lower, upper);
        System.out.println("Contiguous list from " + lower + " to " + upper + ": " + subList.toString());

        subList.sort(Comparator.naturalOrder());

        final Long min = subList.get(0);
        final Long max = subList.get(subList.size() - 1);

        System.out.println("Lowest " + min + " Uppest " + max + " = " + (min + max));
    }
}
