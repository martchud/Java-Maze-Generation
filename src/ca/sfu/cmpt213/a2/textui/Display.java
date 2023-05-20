package ca.sfu.cmpt213.a2.textui;
import ca.sfu.cmpt213.a2.model.Cell;
import ca.sfu.cmpt213.a2.model.Pathway;
/**
 This is my Display class and it is responsible for displaying all of the necessary output of the program. It displays
 the maze, winning and losing conditions, instructions, and feedback to the user.
 */
public class Display {
    private final static int HEIGHT = 16;
    private final static int LENGTH = 20;

    public static void printMazeFunction(Cell[][] Maze) {
        System.out.println("\nMaze:");
        for (int i = 0; i < HEIGHT; i ++) {
            for (int k = 0; k < LENGTH; k++) {
                if (Maze[i][k].getTreasureHunterDead()) {
                    System.out.print("X");
                }
                else if (Maze[i][k].getGuardianCount() > 0) {
                    System.out.print("!");
                }
                else if (Maze[i][k].getRelic()) {
                    System.out.print("^");
                }
                else if (Maze[i][k].getInvisibleWall()){
                    System.out.print(".");
                }
                else if (Maze[i][k].getCell() == Pathway.FULL) {
                    System.out.print("#");
                }
                else if (Maze[i][k].getTreasureHunter()) {
                    System.out.print("@");
                }
                else if (Maze[i][k].getInvisiblePath()){
                    System.out.print(".");
                }
                else if (Maze[i][k].getCell() == Pathway.PATH) {
                    System.out.print(" ");
                }
                else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
    }

    public static void printInstructions() {
        System.out.println ("\nDIRECTIONS:");
        System.out.println ("\tCollect 3 relics!");
        System.out.println ("LEGEND:");
        System.out.println ("\t#: Wall");
        System.out.println ("\t@: You (the treasure hunter)");
        System.out.println ("\t!: Guardian");
        System.out.println ("\t^: Relic");
        System.out.println ("\t.: Unexplored space");
        System.out.println ("MOVES:");
        System.out.println ("\tUse W (up), A (left), S (down) and D (right) to move.");
        System.out.println("\t(You must press enter after each move).");
    }

    public static void printRelicProgression(int relicCount, int relicsNeeded) {
        System.out.println ("Total number of relics to be collected: " + relicsNeeded +
                "\nNumber of relics currently in possession: " + relicCount);
    }

    public static void printWinningText() {
        System.out.println ("Congratulations! You won!");
    }

    public static void printStartingMessage(){
        System.out.print ("----------\n1) Start\n----------");
    }

    public static void printLosingText() {
        System.out.println ("GAME OVER... please try again.");
    }

    public static void printInvalidEntry() {
        System.out.println ("This command is not valid. Please enter w,a,s,d for movement or ? for help and m for map reveal ");
    }

    public static void printInvalidMovement() {
        System.out.println ("Invalid move: you cannot move through walls!");
    }

    public static void printDeathByGuardian() {
        System.out.println ("Oh no! The hunter has been killed!");
    }

    public static void printUserMovementPrompt() {
        System.out.print ("Enter your move [WASD?]: ");
    }
}