package ca.sfu.cmpt213.a2.model;
/**
 This is my relic class. It is responsible for keeping track of the amount of relics owned as well as set the
 condition to 1 relic when the game cheat is called.
 */
public class Relic {

    private static int relicCount;
    private static int relicWinningCondition = 3;

    public static void addRelicToCount() {
        relicCount++;
    }

    public static int getRelicCount() {
        return relicCount;
    }

    public static void setRelicWinningCondition(int winningCondition) {
        relicWinningCondition = winningCondition;
    }

    public static int getRelicWinningCondition() {
        return relicWinningCondition;
    }
}