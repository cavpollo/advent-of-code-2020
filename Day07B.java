package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07B {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input::");

        final Pattern outerBagPattern = Pattern.compile("^([a-z]+ [a-z]+) bags", Pattern.CASE_INSENSITIVE);
        final Pattern innerBagsPattern = Pattern.compile("([0-9]+) ([a-z]+ [a-z]+) bags?[\\,\\.]", Pattern.CASE_INSENSITIVE);

        final ArrayList<Bag> bagList = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final Matcher outerMatcher = outerBagPattern.matcher(readLine.trim());
                final boolean foundOuter = outerMatcher.find();

                final String outerBagDescription = outerMatcher.group(1);

                final Matcher innerMatcher = innerBagsPattern.matcher(readLine.trim());

                final Set<Bag.Leaf> leaves = new HashSet<>();
                while (innerMatcher.find()) {
                    final Integer innerBagCount = Integer.parseInt(innerMatcher.group(1));
                    final String innerBagDescription = innerMatcher.group(2);
                    leaves.add(new Bag.Leaf(innerBagCount, innerBagDescription));
                }

                Bag bag = new Bag(outerBagDescription, leaves);
                bagList.add(bag);
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("got " + bagList.size() + " bag lists");

        final String searchFor = "shiny gold";
        int containedBags = containedBags(bagList, searchFor);

        System.out.println("Needs more bags: " + containedBags);
    }

    public static int containedBags(final ArrayList<Bag> bagList, final String searchFor) {
        int containedBags = 0;
        for (final Bag bag : bagList) {
            if (bag.getParentBag().equals(searchFor)) {

                System.out.println(searchFor + " needs more bags:" + bag.getChildBags());

                for (final Bag.Leaf childBag : bag.getChildBags()) {
                    containedBags += childBag.getQuantity();
                    containedBags += childBag.getQuantity() * containedBags(bagList, childBag.getChildBag());
                }
            }
        }
        return containedBags;
    }

    private static class Bag {
        private final String parentBag;
        private final Set<Leaf> childBags;

        private Bag(final String parentBag, final Set<Leaf> childBags) {
            this.parentBag = parentBag;
            this.childBags = childBags;
        }

        public String getParentBag() {
            return parentBag;
        }

        public Set<Leaf> getChildBags() {
            return childBags;
        }

        public boolean contains(final String bag) {
            for (Leaf leaf : childBags) {
                if (leaf.getChildBag().equals(bag)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public String toString() {
            return "TreeNode{" +
                    "bag='" + parentBag + '\'' +
                    ", leaves=" + childBags +
                    '}';
        }

        private static class Leaf {
            private final Integer quantity;
            private final String childBag;

            private Leaf(final Integer quantity, final String childBag) {
                this.quantity = quantity;
                this.childBag = childBag;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public String getChildBag() {
                return childBag;
            }

            @Override
            public String toString() {
                return "Leaf{" +
                        "quantity=" + quantity +
                        ", childBag='" + childBag + '\'' +
                        '}';
            }
        }
    }
}
