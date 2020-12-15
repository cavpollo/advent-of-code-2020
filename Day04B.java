package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day04B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

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

        List<String> eyeColors = new ArrayList<>();
        eyeColors.add("amb");
        eyeColors.add("blu");
        eyeColors.add("brn");
        eyeColors.add("gry");
        eyeColors.add("grn");
        eyeColors.add("hzl");
        eyeColors.add("oth");

        final Pattern hairColorPattern = Pattern.compile("^#[0-9a-fA-F]{6}$", Pattern.CASE_INSENSITIVE);
        final Pattern passportIdPattern = Pattern.compile("^[0-9]{9}$", Pattern.CASE_INSENSITIVE);

        int valid = 0;
        for (final Map<String, String> map : list) {
            if (!map.keySet().containsAll(mustHaves)) {
                System.out.println("Missing field");
                continue;
            }

            if (map.get("byr").length() != 4 || Integer.parseInt(map.get("byr")) < 1920 || Integer.parseInt(map.get("byr")) > 2002) {
                System.out.println("byr: " + map.get("byr"));
                continue;
            }

            if (map.get("iyr").length() != 4 || Integer.parseInt(map.get("iyr")) < 2010 || Integer.parseInt(map.get("iyr")) > 2020) {
                System.out.println("iyr: " + map.get("iyr"));
                continue;
            }

            if (map.get("eyr").length() != 4 || Integer.parseInt(map.get("eyr")) < 2020 || Integer.parseInt(map.get("eyr")) > 2030) {
                System.out.println("eyr: " + map.get("eyr"));
                continue;
            }

            if ((map.get("hgt").endsWith("cm") && Integer.parseInt(map.get("hgt").substring(0, map.get("hgt").length() - 2)) >= 150 && Integer.parseInt(map.get("hgt").substring(0, map.get("hgt").length() - 2)) <= 193) ||
                    (map.get("hgt").endsWith("in") && Integer.parseInt(map.get("hgt").substring(0, map.get("hgt").length() - 2)) >= 59 && Integer.parseInt(map.get("hgt").substring(0, map.get("hgt").length() - 2)) <= 76)) {
                //nothing
            } else {
                System.out.println("hgt: " + map.get("hgt"));
                continue;
            }

            if (!hairColorPattern.matcher(map.get("hcl")).matches()) {
                System.out.println("hcl: " + map.get("hcl"));
                continue;
            }

            if (!eyeColors.contains(map.get("ecl"))) {
                System.out.println("ecl: " + map.get("ecl"));
                continue;
            }

            if (!passportIdPattern.matcher(map.get("pid")).matches()) {
                System.out.println("pid: " + map.get("pid"));
                continue;
            }

            valid++;
        }

        System.out.println("Valid passport count: " + valid);
    }
}
