package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Day02A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

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
        private final Integer min;
        private final Integer max;
        private final char character;
        private final String password;

        public Password(final Integer min, final Integer max, final char character, final String password) {
            this.min = min;
            this.max = max;
            this.character = character;
            this.password = password;
        }

        public boolean isValid() {
            int count = 0;
            for (int i = 0; i < password.length(); i++) {
                if (password.charAt(i) == character) {
                    if (count == max) return false;

                    count++;
                }
            }

            return count >= min;
        }

        @Override
        public String toString() {
            return "Password{" +
                    "min=" + min +
                    ", max=" + max +
                    ", character='" + character + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
