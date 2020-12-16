package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day02B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final ArrayList<Password> passwordList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final String[] split = readLine.trim().split(" ");
                final String[] boundsSplit = split[0].split("-");
                passwordList.add(new Password(Integer.parseInt(boundsSplit[0]), Integer.parseInt(boundsSplit[1]), split[1].charAt(0), split[2]));
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + passwordList.size() + " passwords");

        int count = 0;
        for (final Password password : passwordList) {
            final boolean valid = password.isValid();
            System.out.println(password.toString() + " is " + (valid ? "valid" : "INVALID"));

            if (valid) {
                count++;
            }
        }


        System.out.println("Valid passwords: " + count);
    }

    private static class Password {
        private final Integer posA;
        private final Integer posB;
        private final char character;
        private final String password;

        public Password(final Integer posA, final Integer posB, final char character, final String password) {
            this.posA = posA;
            this.posB = posB;
            this.character = character;
            this.password = password;
        }

        public boolean isValid() {
            if (password.length() < posB) return false;

            final boolean isOnA = password.charAt(posA - 1) == character;
            final boolean isOnB = password.charAt(posB - 1) == character;
            return (isOnA && !isOnB) || (!isOnA && isOnB);
        }

        @Override
        public String toString() {
            return "Password{" +
                    "min=" + posA +
                    ", max=" + posB +
                    ", character='" + character + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
