package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day03A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        final ArrayList<String> treeLines = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                treeLines.add(readLine.trim());
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + treeLines.size() + " treeLines");

        int count = 0;
        int pos = 0;
        for (final String treeLine : treeLines) {
            if (treeLine.charAt(pos) == '#') {
                count++;
            }
            pos += 3;
            if (pos >= treeLine.length()) {
                pos = pos - treeLine.length();
            }
        }

        System.out.println("Trees found: " + count);
    }
}
