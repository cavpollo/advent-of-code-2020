package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day06B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

        final List<List<Character>> list = new ArrayList<>();
        List<Character> characterList = new ArrayList<>();
        int count = 0;
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                if (count == 0) {
                    for (int i = 0; i < readLine.trim().length(); i++) {
                        characterList.add(readLine.trim().charAt(i));
                    }
                } else {
//                    System.out.println("group: " + characterList.toString());
                    for (int i = 0; i < characterList.size(); ) {
                        final String character = characterList.get(i).toString();
                        if (!readLine.trim().contains(character)) {
                            characterList.remove(i);
                        } else {
                            i++;
                        }
//                        System.out.println("group_loop: " + characterList.toString());
                    }
                }
                count++;
            } else {
                if (count == 0) {
//                    System.out.println("group: " + characterList.toString());
                    break;
                } else {
//                    System.out.println("adding group: " + characterList.toString());
                    list.add(characterList);
                    characterList = new ArrayList<>();
                    count = 0;
                }
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + list.size() + " groups");

        int count2 = 0;
        for (final List<Character> groupCharacterList : list) {
            count2 += groupCharacterList.size();
        }


        System.out.println("Yes answer count: " + count2);
    }
}
