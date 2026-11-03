

package Q4_MapComparison;

import java.util.*;

public class MapComparison {

    static final int LOOKUPS = 100000;
    static final int REPETITIONS = 5;
    static volatile long resultCheck = 0;

    static class Medicine {
        String name;
        int quantity;
        double price;

        Medicine(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " | Stock: " + quantity
                    + " | Price: Rs." + price;
        }
    }

    static Map<String, Medicine> createMap(
            String type, int size) {

        Map<String, Medicine> map;

        if (type.equals("HashMap")) {
            map = new HashMap<>();
        } else if (type.equals("TreeMap")) {
            map = new TreeMap<>();
        } else {
            map = new LinkedHashMap<>();
        }

        for (int i = 0; i < size; i++) {
            String code = String.format("MED%06d", i);

            map.put(code, new Medicine(
                    "Medicine-" + i,
                    100 + i,
                    10.0 + (i % 100)
            ));
        }

        return map;
    }

    static void measureLookups(
            String type, Map<String, Medicine> map, int size) {

        Random random = new Random(42);

        String[] searchCodes = new String[LOOKUPS];

        for (int i = 0; i < LOOKUPS; i++) {
            int index = random.nextInt(size);
            searchCodes[i] = String.format("MED%06d", index);
        }

        // Warm-up
        for (int i = 0; i < 2; i++) {
            long sum = 0;

            for (String code : searchCodes) {
                Medicine medicine = map.get(code);
                if (medicine != null) {
                    sum += medicine.quantity;
                }
            }

            resultCheck += sum;
        }

        long totalTime = 0;

        for (int r = 0; r < REPETITIONS; r++) {

            long sum = 0;
            long start = System.nanoTime();

            for (String code : searchCodes) {
                Medicine medicine = map.get(code);

                if (medicine != null) {
                    sum += medicine.quantity;
                }
            }

            long end = System.nanoTime();

            totalTime += end - start;
            resultCheck += sum;
        }

        double averageMs =
                totalTime / (double) REPETITIONS / 1_000_000.0;

        System.out.printf(
                "%-15s Lookup time: %.3f ms%n",
                type, averageMs);
    }

    static void measureReport(
            String type, Map<String, Medicine> map) {

        // Warm-up
        for (int i = 0; i < 2; i++) {
            resultCheck += generateReport(map).length();
        }

        long totalTime = 0;

        for (int r = 0; r < REPETITIONS; r++) {

            long start = System.nanoTime();

            String report = generateReport(map);

            long end = System.nanoTime();

            totalTime += end - start;
            resultCheck += report.length();
        }

        double averageMs =
                totalTime / (double) REPETITIONS / 1_000_000.0;

        System.out.printf(
                "%-15s Report time: %.3f ms%n",
                type, averageMs);
    }

    static String generateReport(Map<String, Medicine> map) {

        StringBuilder report = new StringBuilder();

        for (Map.Entry<String, Medicine> entry : map.entrySet()) {
            report.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return report.toString();
    }

    static void checkOrder(Map<String, Medicine> map) {

        List<String> keys = new ArrayList<>(map.keySet());

        boolean sorted = true;

        for (int i = 1; i < keys.size(); i++) {
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
                sorted = false;
                break;
            }
        }

        System.out.println("Sorted by medicine code: " + sorted);
    }

    static void demonstrateOrder() {

        System.out.println("\nORDER DEMONSTRATION");

        Map<String, String> hashMap = new HashMap<>();
        Map<String, String> treeMap = new TreeMap<>();
        Map<String, String> linkedMap = new LinkedHashMap<>();

        String[] codes = {"MED003", "MED001", "MED004", "MED002"};

        for (String code : codes) {
            hashMap.put(code, "Medicine");
            treeMap.put(code, "Medicine");
            linkedMap.put(code, "Medicine");
        }

        System.out.println("HashMap order: " + hashMap.keySet());
        System.out.println("TreeMap order: " + treeMap.keySet());
        System.out.println("LinkedHashMap order: "
                + linkedMap.keySet());
    }

    static void runExperiment(int size) {

        System.out.println("\n======================================");
        System.out.println("INVENTORY SIZE: " + size);
        System.out.println("======================================");

        String[] types = {
            "HashMap", "TreeMap", "LinkedHashMap"
        };

        for (String type : types) {

            Map<String, Medicine> map = createMap(type, size);

            System.out.println("\n" + type);

            measureLookups(type, map, size);
            measureReport(type, map);

            checkOrder(map);
        }
    }

    public static void main(String[] args) {

        demonstrateOrder();

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {
            runExperiment(size);
        }

        System.out.println("\nFinal check value: " + resultCheck);
        System.out.println("PHARMACY INVENTORY TEST COMPLETED");
    }
}
