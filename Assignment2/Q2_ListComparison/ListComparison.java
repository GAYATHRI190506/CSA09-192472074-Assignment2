
package Q2_ListComparison;

import java.util.*;

public class ListComparison {

    static final int REPETITIONS = 5;
    static final int WARMUP = 2;
    static final int OPERATIONS = 200;
    static final int GET_OPERATIONS = 200;
    static final int ITERATION_PASSES = 5;

    static volatile long resultCheck = 0;

    interface ListFactory {
        List<Integer> create(int size);
    }

    interface ListAction {
        long execute(List<Integer> list);
    }

    interface DequeAction {
        long execute(Deque<Integer> deque);
    }

    static List<Integer> createArrayList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    static List<Integer> createLinkedList(int size) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    static Deque<Integer> createArrayDeque(int size) {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deque.addLast(i);
        }
        return deque;
    }

    static void benchmarkList(
            String operation,
            int size,
            ListFactory factory,
            ListAction action) {

        // Warm-up
        for (int i = 0; i < WARMUP; i++) {
            List<Integer> list = factory.create(size);
            resultCheck += action.execute(list);
        }

        long totalTime = 0;

        // Measured repetitions
        for (int i = 0; i < REPETITIONS; i++) {
            List<Integer> list = factory.create(size);

            long start = System.nanoTime();
            long value = action.execute(list);
            long end = System.nanoTime();

            resultCheck += value;
            totalTime += end - start;
        }

        double averageMs =
                totalTime / (double) REPETITIONS / 1_000_000.0;

        System.out.printf("%-27s %12.3f ms%n",
                operation, averageMs);
    }

    static void benchmarkDeque(
            String operation,
            int size,
            DequeAction action) {

        // Warm-up
        for (int i = 0; i < WARMUP; i++) {
            Deque<Integer> deque = createArrayDeque(size);
            resultCheck += action.execute(deque);
        }

        long totalTime = 0;

        // Measured repetitions
        for (int i = 0; i < REPETITIONS; i++) {
            Deque<Integer> deque = createArrayDeque(size);

            long start = System.nanoTime();
            long value = action.execute(deque);
            long end = System.nanoTime();

            resultCheck += value;
            totalTime += end - start;
        }

        double averageMs =
                totalTime / (double) REPETITIONS / 1_000_000.0;

        System.out.printf("%-27s %12.3f ms%n",
                operation, averageMs);
    }

    static void testArrayList(int size) {

        System.out.println("\nARRAYLIST | Size: " + size);
        System.out.println("----------------------------------------------");

        ListFactory factory = ListComparison::createArrayList;

        benchmarkList("Add at end", size, factory, list -> {
            for (int i = 0; i < OPERATIONS; i++) {
                list.add(-i);
            }
            return list.size();
        });

        benchmarkList("Insert at front", size, factory, list -> {
            for (int i = 0; i < OPERATIONS; i++) {
                list.add(0, -i);
            }
            return list.size();
        });

        benchmarkList("Insert and remove middle", size, factory, list -> {
            for (int i = 0; i < OPERATIONS; i++) {
                int middle = list.size() / 2;
                list.add(middle, -i);
                list.remove(middle);
            }
            return list.size();
        });

        benchmarkList("Remove from front", size, factory, list -> {
            long sum = 0;
            for (int i = 0; i < OPERATIONS; i++) {
                sum += list.remove(0);
            }
            return sum;
        });

        benchmarkList("Get kth element", size, factory, list -> {
            long sum = 0;
            for (int i = 0; i < GET_OPERATIONS; i++) {
                int index = (i * 37) % list.size();
                sum += list.get(index);
            }
            return sum;
        });

        benchmarkList("Iterate playlist", size, factory, list -> {
            long sum = 0;
            for (int p = 0; p < ITERATION_PASSES; p++) {
                for (Integer value : list) {
                    sum += value;
                }
            }
            return sum;
        });
    }

    static void testLinkedList(int size) {

        System.out.println("\nLINKEDLIST | Size: " + size);
        System.out.println("----------------------------------------------");

        ListFactory factory = ListComparison::createLinkedList;

        benchmarkList("Add at end", size, factory, list -> {
            for (int i = 0; i < OPERATIONS; i++) {
                list.add(-i);
            }
            return list.size();
        });

        benchmarkList("Insert at front", size, factory, list -> {
            for (int i = 0; i < OPERATIONS; i++) {
                list.add(0, -i);
            }
            return list.size();
        });

        benchmarkList("Insert and remove middle", size, factory, list -> {
            long sum = 0;

            for (int i = 0; i < OPERATIONS; i++) {
                int middle = list.size() / 2;

                ListIterator<Integer> iterator =
                        list.listIterator(middle);

                iterator.add(-i);
                iterator.next();
                sum += iterator.next();
                iterator.remove();
            }

            return sum;
        });

        benchmarkList("Remove from front", size, factory, list -> {
            long sum = 0;
            for (int i = 0; i < OPERATIONS; i++) {
                sum += list.remove(0);
            }
            return sum;
        });

        benchmarkList("Get kth element", size, factory, list -> {
            long sum = 0;
            for (int i = 0; i < GET_OPERATIONS; i++) {
                int index = (i * 37) % list.size();
                sum += list.get(index);
            }
            return sum;
        });

        benchmarkList("Iterate playlist", size, factory, list -> {
            long sum = 0;
            for (int p = 0; p < ITERATION_PASSES; p++) {
                for (Integer value : list) {
                    sum += value;
                }
            }
            return sum;
        });
    }

    static void testArrayDeque(int size) {

        System.out.println("\nARRAYDEQUE | Size: " + size);
        System.out.println("----------------------------------------------");

        benchmarkDeque("Add at end", size, deque -> {
            for (int i = 0; i < OPERATIONS; i++) {
                deque.addLast(-i);
            }
            return deque.size();
        });

        benchmarkDeque("Add at front", size, deque -> {
            for (int i = 0; i < OPERATIONS; i++) {
                deque.addFirst(-i);
            }
            return deque.size();
        });

        benchmarkDeque("Remove from front", size, deque -> {
            long sum = 0;
            for (int i = 0; i < OPERATIONS; i++) {
                sum += deque.removeFirst();
            }
            return sum;
        });

        benchmarkDeque("Remove from end", size, deque -> {
            long sum = 0;
            for (int i = 0; i < OPERATIONS; i++) {
                sum += deque.removeLast();
            }
            return sum;
        });

        benchmarkDeque("Iterate playlist", size, deque -> {
            long sum = 0;
            for (int p = 0; p < ITERATION_PASSES; p++) {
                for (Integer value : deque) {
                    sum += value;
                }
            }
            return sum;
        });
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        System.out.println("MUSIC PLAYLIST COLLECTION BENCHMARK");
        System.out.println("Warm-up runs: " + WARMUP);
        System.out.println("Measured repetitions: " + REPETITIONS);
        System.out.println("Operations per test: " + OPERATIONS);

        for (int size : sizes) {

            System.out.println("\n==============================================");
            System.out.println("PLAYLIST SIZE: " + size);
            System.out.println("==============================================");

            testArrayList(size);
            testLinkedList(size);
            testArrayDeque(size);
        }

        System.out.println("\nFinal validation value: " + resultCheck);
        System.out.println("BENCHMARK COMPLETED SUCCESSFULLY");
    }
}
