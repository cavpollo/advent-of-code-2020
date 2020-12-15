package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day01A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed nums:");

        final ArrayList<Integer> nums = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Integer num = Integer.parseInt(readLine.trim());
                nums.add(num);
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + nums.size() + " nums");

        boolean found = false;
        for (int i = 0; i < nums.size(); i++) {
            final Integer numA = nums.get(i);

            for (int j = i + 1; j < nums.size(); j++) {
                final Integer numB = nums.get(j);

                if (numA + numB >= 2020) {
                    continue;
                }

                for (int k = j + 1; k < nums.size(); k++) {
                    final Integer numC = nums.get(k);

//                    System.out.println(numA + " + " + numB + " + " + numC + " = " + (numA + numB + numC));
                    if (numA + numB + numC == 2020) {
                        found = true;
                        System.out.println(numA + " x " + numB + " x " + numC + " = " + (numA * numB * numC));
                        break;
                    }
                }
            }
            if (found) break;
        }

        if (!found) {
            System.out.println("not found =(");
        }
    }
}
