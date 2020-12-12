package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Day06A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        final ArrayList<Set<Character>> list = new ArrayList<>();
        Set<Character> characterSet = new HashSet<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (int i = 0; i < readLine.trim().length(); i++) {
                    characterSet.add(readLine.trim().charAt(i));
                }
            } else {
                if (characterSet.isEmpty()) {
                    break;
                } else {
                    list.add(characterSet);
                    characterSet = new HashSet<>();
                }
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + list.size() + " groups");

        int count = 0;
        for (final Set<Character> groupCharacterSet : list) {
            count += groupCharacterSet.size();
        }


        System.out.println("Yes answer count: " + count);
    }
}
