package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day04A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> keyMap = new HashMap<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                for (String pair : readLine.trim().split(" ")) {
                    final String[] keyValue = pair.split(":");
                    keyMap.put(keyValue[0], keyValue[1]);
                }
            } else {
                if (keyMap.isEmpty()) {
                    break;
                } else {
                    list.add(keyMap);
                    keyMap = new HashMap<>();
                }
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + list.size() + " passports");

        List<String> mustHaves = new ArrayList<>();
        mustHaves.add("byr");
        mustHaves.add("iyr");
        mustHaves.add("eyr");
        mustHaves.add("hgt");
        mustHaves.add("hcl");
        mustHaves.add("ecl");
        mustHaves.add("pid");

        int valid = 0;
        for (final Map<String, String> map : list) {
            if (map.keySet().containsAll(mustHaves)) {
                valid++;
            }
        }

        System.out.println("Valid passport count: " + valid);
    }
}
