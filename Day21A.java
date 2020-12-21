package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day21A {
    public static void main(String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Paste the input:");

        final List<Food> foods = new ArrayList<>();
        do {
            final String readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {
                final String[] split = readLine.trim().split(" \\(contains ");

                final List<String> ingredients = new ArrayList<>();
                for (final String ingredient : split[0].split(" ")) {
                    ingredients.add(ingredient);
                }

                final List<String> allergens = new ArrayList<>();
                for (final String allergen : split[1].substring(0, split[1].length() - 1).split(", ")) {
                    allergens.add(allergen);
                }

                foods.add(new Food(ingredients, allergens));
            } else {
                break;
            }
        } while (true);

        System.out.println("Got " + foods.size() + " foods");

        final Instant start = Instant.now();

        final List<String> allIngredientsRepeated = foods.stream()
                .map(Food::getIngredients)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final Map<String, List<String>> allergenMap = new HashMap<>();
        for (final Food food : foods) {
            final List<String> allergens = food.getAllergens();
            final List<String> ingredients = food.getIngredients();

            for (final String allergen : allergens) {
                if (allergenMap.containsKey(allergen)) {
                    final List<String> intersectIngredients = allergenMap.get(allergen).stream()
                            .filter(ingredients::contains)
                            .collect(Collectors.toList());
                    allergenMap.put(allergen, intersectIngredients);
                } else {
                    allergenMap.put(allergen, ingredients);
                }
            }
        }


//        for (final Map.Entry<String, List<String>> allergenListEntry : allergenMap.entrySet()) {
//            System.out.println(allergenListEntry.getKey() + " is mapped to the ingredients (" + allergenListEntry.getValue().size() + "): " + allergenListEntry.getValue());
//        }

        do {
            for (final Map.Entry<String, List<String>> entry : allergenMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    final String ingredient = entry.getValue().get(0);
                    for (final List<String> values : allergenMap.values()) {
                        values.remove(ingredient);
                    }

                    while (allIngredientsRepeated.remove(ingredient)) {
                        // Trick to remove all occurrences
                    }
                }
            }
        } while (allergenMap.values().stream().anyMatch(value -> value.size() > 0));

//        for (final String ingredient : allIngredientsRepeated) {
//            System.out.println("Ingredient " + ingredient);
//        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("The allergen-less ingredients appear " + allIngredientsRepeated.size() + " times in " + timeElapsed + "ms");
    }

    private static class Food {
        private final List<String> ingredients;
        private final List<String> allergens;

        public Food(final List<String> ingredients, final List<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public List<String> getAllergens() {
            return allergens;
        }
    }
}
