
package Q7_DesignChallenge;

import java.util.*;

public class DesignChallenge {

    static final int WARMUP_RUNS = 2;
    static final int MEASURED_RUNS = 5;
    static final int LOOKUP_OPERATIONS = 1000;

    static volatile long resultCheck = 0;

    static List<Integer> createLinkedList(int size) {
        List<Integer> list = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static List<Integer> createArrayList(int size) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static int[] createSearchPositions(int size) {
        int[] positions = new int[LOOKUP_OPERATIONS];

        Random random = new Random(42);

        for (int i = 0; i < LOOKUP_OPERATIONS; i++) {
            positions[i] = random.nextInt(size);
        }

        return positions;
    }

    static long performLookups(
            List<Integer> playlist, int[] positions) {

        long sum = 0;

        for (int position : positions) {
            sum += playlist.get(position);
        }

        return sum;
    }

    static double benchmark(
            String name,
            List<Integer> playlist,
            int[] positions) {

        // Warm-up
        for (int i = 0; i < WARMUP_RUNS; i++) {
            resultCheck += performLookups(playlist, positions);
        }

        long totalTime = 0;

        // Repeated measurements
        for (int i = 0; i < MEASURED_RUNS; i++) {

            long start = System.nanoTime();

            long sum = performLookups(playlist, positions);

            long end = System.nanoTime();

            resultCheck += sum;
            totalTime += end - start;
        }

        double averageMs =
                totalTime / (double) MEASURED_RUNS / 1_000_000.0;

        System.out.printf(
                "%-15s Average lookup time: %.3f ms%n",
                name, averageMs
        );

        return averageMs;
    }

    static void runExperiment(int size) {

        System.out.println("\n======================================");
        System.out.println("PLAYLIST SIZE: " + size);
        System.out.println("======================================");

        // Prepare both collections outside the timed section
        List<Integer> original = createLinkedList(size);
        List<Integer> redesigned = createArrayList(size);

        // Same search positions for both implementations
        int[] positions = createSearchPositions(size);

        double originalTime =
                benchmark("LinkedList", original, positions);

        double redesignedTime =
                benchmark("ArrayList", redesigned, positions);

        if (redesignedTime > 0) {
            double speedup = originalTime / redesignedTime;

            System.out.printf(
                    "Redesign speedup: %.2f times%n", speedup
            );
        }

        System.out.println("Both playlists contain the same songs: "
                + original.equals(redesigned));
    }

    public static void main(String[] args) {

        System.out.println(
                "Q7: LINKEDLIST VS ARRAYLIST DESIGN CHALLENGE"
        );

        System.out.println("Warm-up runs: " + WARMUP_RUNS);
        System.out.println("Measured runs: " + MEASURED_RUNS);
        System.out.println(
                "Lookups per run: " + LOOKUP_OPERATIONS
        );

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {
            runExperiment(size);
        }

        System.out.println("\nFinal validation value: " + resultCheck);
        System.out.println("DESIGN CHALLENGE COMPLETED");
    }
}
