
package Q5_GenericLeaderboard;

import java.util.*;

class Team {
    private final String name;
    private final int points;
    private final int goalDifference;

    Team(String name, int points, int goalDifference) {
        this.name = name;
        this.points = points;
        this.goalDifference = goalDifference;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDifference + ")";
    }
}

class Leaderboard<T> {

    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    // TODO 1: Return a sorted copy without modifying original list
    public List<T> getRankedList(List<? extends T> items) {

        List<T> result = new ArrayList<>(items);

        result.sort(order);

        return result;
    }
}

public class LeaderboardDemo {

    // TODO 2: Comparator for team ranking
    static Comparator<Team> teamComparator =
            Comparator.comparingInt(Team::getPoints)
                    .reversed()
                    .thenComparing(
                            Comparator.comparingInt(
                                    Team::getGoalDifference
                            ).reversed()
                    )
                    .thenComparing(Team::getName);

    public static void main(String[] args) {

        List<Team> teams = new ArrayList<>();

        teams.add(new Team("Cobras", 6, 1));
        teams.add(new Team("Panthers", 9, 4));
        teams.add(new Team("Eagles", 9, 6));
        teams.add(new Team("Bisons", 9, 4));

        System.out.println("Original team list:");
        System.out.println(teams);

        Leaderboard<Team> leaderboard =
                new Leaderboard<>(teamComparator);

        List<Team> ranked =
                leaderboard.getRankedList(teams);

        System.out.println("\nRanked leaderboard:");
        System.out.println(ranked);

        System.out.println("\nOriginal list after ranking:");
        System.out.println(teams);

        System.out.println("\nTIE BREAKING TEST:");

        System.out.println("Bisons before Panthers: "
                + (teamComparator.compare(
                        teams.get(3), teams.get(1)) < 0));

        System.out.println("\nCOMPARATOR CONSISTENCY TEST:");

        Team a = new Team("Eagles", 9, 6);
        Team b = new Team("Bisons", 9, 4);
        Team c = new Team("Cobras", 6, 1);

        int ab = Integer.signum(teamComparator.compare(a, b));
        int ba = Integer.signum(teamComparator.compare(b, a));

        System.out.println("Antisymmetry test: "
                + (ab == -ba));

        boolean transitive =
                teamComparator.compare(a, b) < 0
                && teamComparator.compare(b, c) < 0
                && teamComparator.compare(a, c) < 0;

        System.out.println("Transitivity test: " + transitive);

        System.out.println("\nTESTING COMPLETED");
    }
}
