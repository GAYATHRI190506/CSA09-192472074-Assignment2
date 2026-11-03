
package Q6_HospitalTriage;

import java.util.*;

class Patient {
    String name;
    int severity;
    long arrivalOrder;

    Patient(String name, int severity, long arrivalOrder) {
        this.name = name;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

class HospitalTriage {

    // TODO: PriorityQueue declaration with comparator
    static PriorityQueue<Patient> patients =
            new PriorityQueue<>(
                Comparator.comparingInt((Patient p) -> p.severity)
                          .thenComparingLong(p -> p.arrivalOrder)
            );

    static long arrivalCounter = 0;

    static void addPatient(String name, int severity) {
        patients.add(
            new Patient(name, severity, arrivalCounter++)
        );
    }

    static void displayTreatmentOrder() {
        System.out.println("\nPatient Treatment Order:");

        while (!patients.isEmpty()) {
            System.out.println(patients.poll());
        }
    }

    public static void main(String[] args) {

        System.out.println("HOSPITAL EMERGENCY TRIAGE SYSTEM");

        // Add patients in arrival order
        addPatient("Ravi", 3);
        addPatient("Meena", 1);
        addPatient("John", 3);
        addPatient("Kumar", 1);
        addPatient("Asha", 5);
        addPatient("Lata", 2);

        displayTreatmentOrder();

        System.out.println("\nEXPECTED ORDER:");
        System.out.println(
            "Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)"
        );

        System.out.println("\nTESTING FIFO FOR EQUAL PRIORITY");

        PriorityQueue<Patient> testQueue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrivalOrder)
        );

        testQueue.add(new Patient("Patient-A", 2, 1));
        testQueue.add(new Patient("Patient-B", 2, 2));
        testQueue.add(new Patient("Patient-C", 2, 3));

        while (!testQueue.isEmpty()) {
            System.out.println(testQueue.poll());
        }

        System.out.println("\nTESTING COMPLETED");
    }
}
