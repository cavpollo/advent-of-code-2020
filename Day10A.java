package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class Day10A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        final ArrayList<Long> adapterList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                adapterList.add(Long.parseLong(readLine.trim()));
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + adapterList.size() + " adapters");

        adapterList.sort(Comparator.naturalOrder());

        long joltage = 0;
        int countDiff1 = 0;
        int countDiff2 = 0;
        int countDiff3 = 0;
        for (Long adapterJoltage : adapterList) {
            if (adapterJoltage - joltage == 1) {
                countDiff1++;
            } else if (adapterJoltage - joltage == 2) {
                countDiff2++;
            } else if (adapterJoltage - joltage == 3) {
                countDiff3++;
            } else {
                break;
            }
            joltage = adapterJoltage;
        }

        countDiff3++;

        System.out.println("Max joltage: " + joltage + " 1:" + countDiff1 + " 2:" + countDiff2 + " 3:" + countDiff3);
    }
}
