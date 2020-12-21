package advent;

import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21B {
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

        final Set<Pair<String, String>> ingredientAllergenPairs = new HashSet<>();
        do {
            for (final Map.Entry<String, List<String>> entry : allergenMap.entrySet()) {
                if (entry.getValue().size() == 1) {
                    final String ingredient = entry.getValue().get(0);
                    ingredientAllergenPairs.add(Pair.of(entry.getKey(), ingredient));
                    for (final List<String> values : allergenMap.values()) {
                        values.remove(ingredient);
                    }
                }
            }
        } while (allergenMap.values().stream().anyMatch(value -> value.size() > 0));

//        for (final Pair<String, String> pair : ingredientAllergenPairs) {
//            System.out.println("Ingredient " + pair.fst + " is mapped to the allergen " + pair.snd);
//        }

        final String ingredientsOrderedByAllergen = ingredientAllergenPairs.stream()
                .sorted(Comparator.comparing(pair -> pair.fst))
                .map(pair -> pair.snd)
                .collect(Collectors.joining(","));

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("The ingredients ordered by allergen string is:");
        System.out.println(ingredientsOrderedByAllergen);
        System.out.println("In " + timeElapsed + "ms");
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
