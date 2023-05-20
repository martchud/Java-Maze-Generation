package ca.sfu.cmpt213.a2.model;
import ca.sfu.cmpt213.a2.textui.Display;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
This is my ProgramLogic class and is responsible for how the whole program works together. This class makes use of all
 the other classes and uses the info it collects and modifies to then call display to show all the necessary
 information.
 */
public class ProgramLogic {
    private final static int HEIGHT = 16;
    private final static int WIDTH = 20;
    private static Maze maze = null;
    private static TreasureHunter treasureHunter = null;
    private static ArrayList<Guardian> Guard = null;

    private static boolean relicWinningCondition = false;

    public static void main(String[] args) {
        setVisibility();
        callConstructors();
        revealTreasureHunterSurroundings();
        callDisplayFunctions();
        callGameplayLoop();
        diedToGuardian();
        showAllCells();
        Display.printMazeFunction(maze.getMaze());
        Display.printRelicProgression(Relic.getRelicCount(), Relic.getRelicWinningCondition());
        callWinOrLoseOutcome();
        System.exit(0);
    }

    private static void callConstructors(){
        constructTreasureHunter();
        constructGuardians();
        relicSpawn();
    }

    private static void callDisplayFunctions(){
        Display.printStartingMessage();
        Display.printInstructions();
    }

    private static void callGameplayLoop(){
        while (isTreasureHunterAlive(treasureHunter) && !relicWinningCondition) {
            Display.printMazeFunction(maze.getMaze());
            Display.printRelicProgression(Relic.getRelicCount(), Relic.getRelicWinningCondition());
            playerDecision(Guard);
            if (Relic.getRelicCount() == Relic.getRelicWinningCondition()) {
                relicWinningCondition = true;
            }
        }
    }

    private static void diedToGuardian(){
        if (!isTreasureHunterAlive(treasureHunter)) {
            Display.printDeathByGuardian();
        }
    }

    private static void callWinOrLoseOutcome(){
        if(isTreasureHunterAlive(treasureHunter)){
            Display.printWinningText();
        }
        if (!isTreasureHunterAlive(treasureHunter)) {
            Display.printLosingText();
        }
    }

    private static void setVisibility() {
        maze = new Maze();
        for (int i = 1; i < HEIGHT - 1; i++) {
            for (int k = 1; k < WIDTH - 1; k++) {
                if (maze.getMazeCell(k, i) == Pathway.PATH) {
                    maze.getIndividualCell(k, i).setInvisiblePath(true);
                } else if (maze.getMazeCell(k, i) == Pathway.FULL) {
                    maze.getIndividualCell(k, i).setInvisibleWall(true);
                }
            }
        }
    }

    private static void constructTreasureHunter() {
        treasureHunter = new TreasureHunter();
        maze.getIndividualCell(1, 1).setTreasureHunter(true);
    }

    private static void constructGuardians() {
        Guard = new ArrayList<>();
        Guard.add(new Guardian(WIDTH - 2, 1));
        Guard.add(new Guardian(WIDTH - 2, HEIGHT - 2));
        Guard.add(new Guardian(1, HEIGHT -2));
        maze.getIndividualCell(Guard.get(0).getHorizontalPosition(), Guard.get(0).getVerticalPosition()).incrementGuardianCount();
        maze.getIndividualCell(Guard.get(1).getHorizontalPosition(), Guard.get(1).getVerticalPosition()).incrementGuardianCount();
        maze.getIndividualCell(Guard.get(2).getHorizontalPosition(), Guard.get(2).getVerticalPosition()).incrementGuardianCount();
    }

    private static void relicSpawn() {
        int pickHorizontal = ThreadLocalRandom.current().nextInt(1, WIDTH - 2);
        int pickVertical = ThreadLocalRandom.current().nextInt(1, HEIGHT - 2);

        if((maze.getMazeCell(pickHorizontal, pickVertical) == Pathway.PATH
                || maze.getIndividualCell(pickHorizontal, pickVertical).getInvisiblePath())
                && !maze.getIndividualCell(pickHorizontal, pickVertical).getTreasureHunter()) {
            maze.getIndividualCell(pickHorizontal, pickVertical).setRelic(true);
        }
        else relicSpawn();
    }

    public static boolean isTreasureHunterAlive(TreasureHunter treasureHunter) {
        return treasureHunter.checkIfAlive();
    }

    public static void revealTreasureHunterSurroundings() {
        treasureHunter.callReveal(treasureHunter, maze);
    }

    public static void showAllCells() {
        for (int i = 1; i < HEIGHT - 1; i++) {
            for (int k = 1; k < WIDTH - 1; k++) {
                Maze.revealMazeCell(k, i, maze);
                if(!maze.getIndividualCell(k, i).getRelic() && maze.getMazeCell(k, i) != Pathway.FULL
                        &&!maze.getIndividualCell(k, i).getTreasureHunter() && maze.getIndividualCell(k,i).getGuardianCount() > 1){
                        maze.setMazeCell(k, i, Pathway.PATH);
                }
            }
        }
    }

    public static void playerDecision(ArrayList<Guardian> Guardians) {
        Scanner userStringInput = new Scanner(System.in);
        String moveEntry;
        while (true) {
            Display.printUserMovementPrompt();
            moveEntry = userStringInput.nextLine();
            if (isTreasureHunterAlive(treasureHunter) && moveEntry.equals("S") || moveEntry.equals("s")
                    || moveEntry.equals("W") || moveEntry.equals("w") || moveEntry.equals("A")
                    || moveEntry.equals("a") || moveEntry.equals("D") || moveEntry.equals("d")
                    || moveEntry.equals("?") || moveEntry.equals("m") || moveEntry.equals("M")
                    || moveEntry.equals("c") || moveEntry.equals("C")) {
                break;
            }
            Display.printInvalidEntry();
        }
        switch (moveEntry) {
            case "?" -> Display.printInstructions();
            case "M", "m" -> showAllCells();
            case "c", "C" -> {
                Relic.setRelicWinningCondition(1);
                playerDecision(Guardians);
            }
            default -> advanceTreasureGuardian(moveEntry, Guardians);
        }
    }

    public static void advanceTreasureGuardian(String moveEntry, ArrayList<Guardian> Guardian) {
        boolean Dead = false;

        if (moveEntry.equals("W") || moveEntry.equals("w") && (maze.getMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() - 1) == Pathway.PATH
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() - 1).getRelic()
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).getGuardianCount() > 0)) {

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() - 1).getRelic()) {
                Relic.addRelicToCount();
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() - 1).setRelic(false);
                relicSpawn();
            }

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() - 1).getGuardianCount() > 0) {
                maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
                Dead = true;
            }
            maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
            maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(false);
            treasureHunter.goUp();

            if (Dead) {
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunterDead(true);
                treasureHunter.setToDead();
            } else maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(true);

        } else if (moveEntry.equals("A") || moveEntry.equals("a") && (maze.getMazeCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()) == Pathway.PATH
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()).getRelic()
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()).getGuardianCount() > 0)) {

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()).getRelic()) {
                Relic.addRelicToCount();
                maze.getIndividualCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()).setRelic(false);
                relicSpawn();
            }

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition() - 1, treasureHunter.getVerticalPosition()).getGuardianCount() > 0) {
                maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
                Dead = true;
            }
            maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
            maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(false);
            treasureHunter.goLeft();

            if (Dead) {
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunterDead(true);
                treasureHunter.setToDead();
            } else maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(true);

        } else if (moveEntry.equals("S") || moveEntry.equals("s") && (maze.getMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1) == Pathway.PATH
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1).getRelic()
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1).getGuardianCount() > 0)) {

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1).getRelic()) {
                Relic.addRelicToCount();
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1).setRelic(false);
                relicSpawn();
            }

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition() + 1).getGuardianCount() > 0) {
                maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
                Dead = true;
            }
            maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
            maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(false);
            treasureHunter.goDown();

            if (Dead) {
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunterDead(true);
                treasureHunter.setToDead();
            } else maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(true);

        } else if (moveEntry.equals("D") || (moveEntry.equals("d") && maze.getMazeCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()) == Pathway.PATH
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()).getRelic()
                || maze.getIndividualCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()).getGuardianCount() > 0)) {

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()).getRelic()) {
                Relic.addRelicToCount();
                maze.getIndividualCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()).setRelic(false);
                relicSpawn();
            }

            if (maze.getIndividualCell(treasureHunter.getHorizontalPosition() + 1, treasureHunter.getVerticalPosition()).getGuardianCount() > 0) {
                maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
                Dead = true;
            }
            maze.setMazeCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition(), Pathway.PATH);
            maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(false);
            treasureHunter.goRight();

            if (Dead) {
                maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunterDead(true);
                treasureHunter.setToDead();
            } else maze.getIndividualCell(treasureHunter.getHorizontalPosition(), treasureHunter.getVerticalPosition()).setTreasureHunter(true);
        } else {
            Display.printInvalidMovement();
            playerDecision(Guardian);
            return;
        }
        revealTreasureHunterSurroundings();

        if(!Dead){
            advanceAllGuardians(Guardian);
        }
    }

    // Reference used to make function below : https://stackoverflow.com/questions/45903585/what-is-optimal-way-to-get-four-unique-random-number-from-0-9

    private static void advanceAllGuardians(ArrayList<Guardian> Guardians) {
        for (Guardian Guardian : Guardians) {
            boolean successfulMove = false;
            Pathway pathwayToCompare;
            Cell cellToCompare;

            List<Integer> Way = IntStream.range(1,4).boxed().collect(Collectors.toList());
            Collections.shuffle (Way);

            while (!successfulMove) {
                for (Integer way : Way) {
                    if (!successfulMove) {
                        if (way.equals(1)) {
                            pathwayToCompare = maze.getMazeCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition() - 1);
                            cellToCompare = maze.getIndividualCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition() - 1);
                        } else if (way.equals(2)) {
                            pathwayToCompare = maze.getMazeCell(Guardian.getHorizontalPosition() - 1, Guardian.getVerticalPosition());
                            cellToCompare = maze.getIndividualCell(Guardian.getHorizontalPosition() - 1, Guardian.getVerticalPosition());
                        } else if (way.equals(3)) {
                            pathwayToCompare = maze.getMazeCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition() + 1);
                            cellToCompare = maze.getIndividualCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition() + 1);
                        } else {
                            pathwayToCompare = maze.getMazeCell(Guardian.getHorizontalPosition() + 1, Guardian.getVerticalPosition());
                            cellToCompare = maze.getIndividualCell(Guardian.getHorizontalPosition() + 1, Guardian.getVerticalPosition());
                        }

                        if (cellToCompare.getTreasureHunter()) {
                            guardianMoves(Guardian, way);
                            maze.getIndividualCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition()).setTreasureHunterDead(true);
                            successfulMove = true;
                        }
                        else if (pathwayToCompare == Pathway.PATH || cellToCompare.getInvisiblePath() || cellToCompare.getRelic()) {
                            guardianMoves(Guardian, way);
                            maze.getIndividualCell(Guardian.getHorizontalPosition(), Guardian.getVerticalPosition()).incrementGuardianCount();
                            successfulMove = true;
                        }
                    }
                }
            }
        }
    }

    private static void guardianMoves(Guardian Guard, Integer direction) {
        maze.getIndividualCell(Guard.getHorizontalPosition(), Guard.getVerticalPosition()).decrementGuardianCount();

        if(direction.equals(2)){
            Guard.Left();
        }
        else if(direction.equals(1)){
            Guard.Up();
        }
        else if(direction.equals(3)){
            Guard.Down();
        }
        else {
            Guard.Right();
        }
    }
}