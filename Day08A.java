package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

        final Pattern linePattern = Pattern.compile("^([a-z]+) ([\\+\\-]+)([0-9]+)$", Pattern.CASE_INSENSITIVE);

        final ArrayList<Instruction> instructionList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher lineMatcher = linePattern.matcher(readLine.trim());
                final boolean found = lineMatcher.find();

                if (!found) {
                    throw new IllegalArgumentException(readLine.trim());
                }

                final String action = lineMatcher.group(1);
                final Integer quantity = (lineMatcher.group(2).equals("-") ? -1 : 1) * Integer.parseInt(lineMatcher.group(3));

                Instruction instruction = new Instruction(action, quantity);
                instructionList.add(instruction);
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + instructionList.size() + " actions");

        final Set<Integer> position = new HashSet<>();
        Long value = 0L;
        for (int i = 0; i < instructionList.size(); ) {
            if (position.contains(i)) {
                break;
            }
            position.add(i);
            final Instruction instruction = instructionList.get(i);
            if (instruction.getAction().equals("acc")) {
                value += instruction.getQuantity();
            } else if (instruction.getAction().equals("jmp")) {
                i = i + instruction.getQuantity();
            } else {
                i++;
            }
        }

        System.out.println("Last position: " + value);
    }

    private static class Instruction {
        private String action;
        private Integer quantity;

        public Instruction(final String action, final Integer quantity) {
            this.action = action;
            this.quantity = quantity;
        }

        public String getAction() {
            return action;
        }

        public Integer getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "action='" + action + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
