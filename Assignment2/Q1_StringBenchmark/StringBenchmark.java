package Q1_StringBenchmark;
public class StringBenchmark {

    static class Workload {
        int tickets;
        int fields;

        Workload(int tickets, int fields) {
            this.tickets = tickets;
            this.fields = fields;
        }
    }

    static volatile int resultCheck = 0;

    // Method 1: String concatenation
    static String buildUsingString(Workload w) {
        StringBuilder allTickets = new StringBuilder();

        for (int i = 0; i < w.tickets; i++) {
            String ticket = "Ticket-" + i + "\n";

            for (int j = 0; j < w.fields; j++) {
                ticket = ticket + "Field-" + j + " ";
            }

            allTickets.append(ticket).append("\n");
        }

        return allTickets.toString();
    }

    // Method 2: StringBuilder
    static String buildUsingStringBuilder(Workload w) {
        StringBuilder allTickets = new StringBuilder();

        for (int i = 0; i < w.tickets; i++) {
            StringBuilder ticket = new StringBuilder();

            ticket.append("Ticket-").append(i).append("\n");

            for (int j = 0; j < w.fields; j++) {
                ticket.append("Field-").append(j).append(" ");
            }

            allTickets.append(ticket).append("\n");
        }

        return allTickets.toString();
    }

    // Method 3: StringBuffer
    static String buildUsingStringBuffer(Workload w) {
        StringBuilder allTickets = new StringBuilder();

        for (int i = 0; i < w.tickets; i++) {
            StringBuffer ticket = new StringBuffer();

            ticket.append("Ticket-").append(i).append("\n");

            for (int j = 0; j < w.fields; j++) {
                ticket.append("Field-").append(j).append(" ");
            }

            allTickets.append(ticket).append("\n");
        }

        return allTickets.toString();
    }

    static void benchmark(String methodName, Workload w, int method) {
        long totalTime = 0;
        int repetitions = 10;

        // Warm-up runs
        for (int i = 0; i < 2; i++) {
            runMethod(w, method);
        }

        // Repeated measurements
        for (int i = 0; i < repetitions; i++) {
            long start = System.nanoTime();

            String output = runMethod(w, method);

            long end = System.nanoTime();

            resultCheck += output.hashCode();

            totalTime += (end - start);
        }

        double averageMs =
                (totalTime / (double) repetitions) / 1_000_000.0;

        System.out.printf("%-15s Average time: %.3f ms%n",
                methodName, averageMs);
    }

    static String runMethod(Workload w, int method) {
        if (method == 1) {
            return buildUsingString(w);
        } else if (method == 2) {
            return buildUsingStringBuilder(w);
        } else {
            return buildUsingStringBuffer(w);
        }
    }

    // Approximate memory measurement
    static void measureMemory(String methodName, Workload w, int method) {
        Runtime runtime = Runtime.getRuntime();

        System.gc();

        long before = runtime.totalMemory() - runtime.freeMemory();

        String output = runMethod(w, method);

        long after = runtime.totalMemory() - runtime.freeMemory();

        long memoryUsed = after - before;

        resultCheck += output.hashCode();

        System.out.printf("%-15s Approx. heap change: %d bytes%n",
                methodName, memoryUsed);
    }

    public static void main(String[] args) {

        Workload[] workloads = {
            new Workload(1, 6),
            new Workload(10000, 50),
            new Workload(1000, 200)
        };

        String[] workloadNames = {
            "Small workload",
            "Many tickets",
            "Many fields"
        };

        for (int i = 0; i < workloads.length; i++) {

            System.out.println("\n==================================");
            System.out.println(workloadNames[i]);
            System.out.println("Tickets: " + workloads[i].tickets);
            System.out.println("Fields per ticket: " + workloads[i].fields);
            System.out.println("==================================");

            benchmark("String", workloads[i], 1);
            benchmark("StringBuilder", workloads[i], 2);
            benchmark("StringBuffer", workloads[i], 3);

            System.out.println("\nMemory measurement:");

            measureMemory("String", workloads[i], 1);
            measureMemory("StringBuilder", workloads[i], 2);
            measureMemory("StringBuffer", workloads[i], 3);
        }

        System.out.println("\nCheck value: " + resultCheck);
    }
}
