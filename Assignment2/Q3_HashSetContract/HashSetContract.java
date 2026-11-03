
package Q3_HashSetContract;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Incorrect implementation: equals() without hashCode()
class BrokenStudent {
    String regNo;
    String name;

    BrokenStudent(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BrokenStudent)) return false;

        BrokenStudent other = (BrokenStudent) obj;
        return Objects.equals(regNo, other.regNo);
    }

    // hashCode() intentionally missing for demonstration
}

// Correct implementation
class Student {
    String regNo;
    String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;

        Student other = (Student) obj;
        return Objects.equals(regNo, other.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return regNo + " - " + name;
    }
}

public class HashSetContract {

    public static void main(String[] args) {

        System.out.println("TEST 1: Same object added twice");

        Set<BrokenStudent> test1 = new HashSet<>();

        BrokenStudent s1 =
                new BrokenStudent("192472074", "Gayathri");

        test1.add(s1);
        test1.add(s1);

        System.out.println("Set size: " + test1.size());

        System.out.println("\nTEST 2: Different objects with same regNo");

        Set<BrokenStudent> test2 = new HashSet<>();

        test2.add(new BrokenStudent("192472074", "Gayathri"));
        test2.add(new BrokenStudent("192472074", "Gayathri"));

        System.out.println("Set size: " + test2.size());
        System.out.println("Expected unique students: 1");

        System.out.println("\nTEST 3: Correct equals() and hashCode()");

        Set<Student> students = new HashSet<>();

        students.add(new Student("192472074", "Gayathri"));
        students.add(new Student("192472074", "Gayathri"));
        students.add(new Student("192511334", "Hariny"));

        System.out.println("Unique students: " + students.size());

        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("\nTEST 4: Modifying regNo after insertion");

        Student mutableStudent =
                new Student("192472074", "Gayathri");

        Set<Student> mutableSet = new HashSet<>();
        mutableSet.add(mutableStudent);

        System.out.println("Before modification: "
                + mutableSet.contains(mutableStudent));

        mutableStudent.regNo = "192472075";

        System.out.println("After modification: "
                + mutableSet.contains(mutableStudent));

        System.out.println("\nTESTING COMPLETED");
    }
}
