package ca.sfu.cmpt213.a2.model;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 This is my Maze class and it is responsible for constructing the maze for my program. This class controls the creation
 of the maze, modification of it, and revealing of the cells. It contains and uses
 "depthFirstSearchRecursiveImplementation" function to create the maze. It also modifies the maze to create additional
 loops and fixes any conditions that break the rules.
 */
public class Maze {
    private static final int LENGTH = 20;
    private static final int HEIGHT = 16;
    private static Cell[][] Maze;
    private int wallsToRemove = ThreadLocalRandom.current().nextInt(13, 18);

    public Maze() {
        Maze = new Cell[HEIGHT][LENGTH];
        constructMaze();
        setCurrentCellVisited(1, 1);
        depthFirstSearchRecursiveImplementation(1, 1);
        clearExtraWallBorder();
        removeRandomWalls();
        finalWallCheck();
    }

    public void constructMaze() {
        constructMazeCells();
        setWallBorderVisited();
    }

    private void constructMazeCells() {
        for (int i = 0; i < LENGTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                Maze[k][i] = new Cell();
            }
        }
    }

    private void setAllCellsAsWalls() {
        for (int i = 1; i < LENGTH - 1; i++) {
            for (int k = 1; k < HEIGHT - 1; k++) {
                Maze[k][i] = new Cell();
            }
        }
    }

    private void setCurrentCellVisited(int verticalPosition, int horizontalPosition) {
        Maze[verticalPosition][horizontalPosition].setVisited();
        Maze[verticalPosition][horizontalPosition].setCell(Pathway.PATH);
    }

    private void setCellMovement (int a, int b,int c, int d, int verticalPosition, int horizontalPosition){
        Maze[verticalPosition + a][horizontalPosition + b].setCell(Pathway.PATH);
        Maze[verticalPosition + c][horizontalPosition + d].setVisited();
    }

    private void setWallBorderVisited() {
        for (int i = 0; i < LENGTH; i++) {
            Maze[0][i].setVisited();
        }
        for (int k = 0, i = HEIGHT - 1; k < LENGTH; k++) {
            Maze[i][k].setVisited();
        }
        for (int i = 1; i < HEIGHT; i++) {
            Maze[i][0].setVisited();
        }
        for (int i = 0, k = LENGTH - 1; i < HEIGHT; i++) {
            Maze[i][k].setVisited();
        }
    }

    // References used to make function below : https://stackoverflow.com/questions/45903585/what-is-optimal-way-to-get-four-unique-random-number-from-0-9
    // https://en.wikipedia.org/wiki/Maze_generation_algorithm

    private void depthFirstSearchRecursiveImplementation(int verticalPosition, int horizontalPosition) {
        List<Integer> directions = IntStream.range(1,5).boxed().collect(Collectors.toList());
        Collections.shuffle (directions);

        for (Integer direction : directions) {
            //Move right
            if (direction.equals(1) && (horizontalPosition + 2) < LENGTH
                    && (Maze[verticalPosition][horizontalPosition + 2].getVisitedInverted())) {
                setCellMovement(0, 1, 0, 1, verticalPosition, horizontalPosition);
                setCurrentCellVisited(verticalPosition, horizontalPosition + 2);
                depthFirstSearchRecursiveImplementation(verticalPosition, horizontalPosition + 2);
            }
            //Move left
            else if (direction.equals(4) && ((horizontalPosition - 2) > 0)
                    && (Maze[verticalPosition][horizontalPosition - 2].getVisitedInverted())) {
                setCellMovement(0, -1, 0, -1, verticalPosition, horizontalPosition);
                setCurrentCellVisited(verticalPosition, horizontalPosition - 2);
                depthFirstSearchRecursiveImplementation(verticalPosition, horizontalPosition - 2);
            }
            //Move down
            else if (direction.equals(3) && ((verticalPosition + 2) < HEIGHT)
                    && (Maze[verticalPosition + 2][horizontalPosition].getVisitedInverted())) {
                setCellMovement(1, 0, 1, 0, verticalPosition, horizontalPosition);
                setCurrentCellVisited(verticalPosition + 2, horizontalPosition);
                depthFirstSearchRecursiveImplementation(verticalPosition + 2, horizontalPosition);
            }
            //Move up
            else if (direction.equals(2) && ((verticalPosition - 2) > 0)
                    && (Maze[verticalPosition - 2][horizontalPosition].getVisitedInverted())) {
                setCellMovement(-1, 0, -1, 0, verticalPosition, horizontalPosition);
                setCurrentCellVisited(verticalPosition - 2, horizontalPosition);
                depthFirstSearchRecursiveImplementation(verticalPosition - 2, horizontalPosition);
            }
        }
    }

    private void finalWallCheck() {
        for (int i = 1; i < HEIGHT - 1; i++) {
            for (int k = 1; k < LENGTH - 1; k++) {
                if (Maze[i][k].getCell() == Pathway.FULL && Maze[i][k + 1].getCell() == Pathway.FULL &&
                        Maze[i + 1][k].getCell() == Pathway.FULL && Maze[i + 1][k + 1].getCell() == Pathway.FULL) {
                    setAllCellsAsWalls();
                    depthFirstSearchRecursiveImplementation(1, 1);
                    clearExtraWallBorder();
                    removeRandomWalls();
                    finalWallCheck();
                }
                if (Maze[i][k].getCell() == Pathway.PATH && Maze[i][k + 1].getCell() == Pathway.PATH &&
                        Maze[i + 1][k].getCell() == Pathway.PATH && Maze[i + 1][k + 1].getCell() == Pathway.PATH) {
                    setAllCellsAsWalls();
                    depthFirstSearchRecursiveImplementation(1, 1);
                    clearExtraWallBorder();
                    removeRandomWalls();
                    finalWallCheck();
                }
            }
        }
    }

    private void clearExtraWallBorder() {

        boolean wallSpawned = true;
        int counter = 0;
        int removedWallNumber = 0;

        Maze[1][1].setCell(Pathway.PATH);
        Maze[1][LENGTH - 2].setCell(Pathway.PATH);
        Maze[HEIGHT - 2][1].setCell(Pathway.PATH);
        Maze[HEIGHT - 2][LENGTH - 2].setCell(Pathway.PATH);

        for(int i = 0, k = 3; i < 10; i++,k++)
        {
            if(Maze[k][18].getCell() == Maze[k][17].getCell())
            {
                Maze[k][18].setCell(Pathway.PATH);
            }
        }

        for(int i = 0, k = 1;i < 6; i++){
            k+=2;
            Maze[k][18].setCell(Pathway.PATH);
        }

        for(int i = 0, k = 4;i < 13; i++,k++) {
            if(Maze[13][k].getCell() == Maze[14][k].getCell()){
                counter ++;
                if(counter > 1)
                {
                    setAllCellsAsWalls();
                    depthFirstSearchRecursiveImplementation(1, 1);
                    clearExtraWallBorder();
                    removeRandomWalls();
                    finalWallCheck();
                    return;
                }
                Maze[14][k].setCell(Pathway.PATH);
                Maze[14][k-1].setCell(Pathway.PATH);
                wallSpawned = false;
                removedWallNumber = k;
            }
        }
        if(wallSpawned){
            setAllCellsAsWalls();
            depthFirstSearchRecursiveImplementation(1, 1);
            clearExtraWallBorder();
            removeRandomWalls();
            finalWallCheck();
        }

        if(!wallSpawned){

            int wallsToBeRemovedLeft = removedWallNumber - 1;
            int wallsToBeRemovedRight = removedWallNumber;
            final int RIGHT_WALL_BORDER = 15;
            final int LEFT_WALL_BORDER = 4;

            for(int i = 0;i < 6;i++){
                if(wallsToBeRemovedRight < RIGHT_WALL_BORDER) {
                    wallsToBeRemovedRight += 2;
                    Maze[14][wallsToBeRemovedRight].setCell(Pathway.PATH);
                }
                if(wallsToBeRemovedLeft > LEFT_WALL_BORDER){
                    wallsToBeRemovedLeft-=2;
                    Maze[14][wallsToBeRemovedLeft].setCell(Pathway.PATH);
                }
            }
        }
    }

    private void removeRandomWalls() {
        int pickHorizontal = ThreadLocalRandom.current().nextInt(1, LENGTH - 2);
        int pickVertical = ThreadLocalRandom.current().nextInt(1, HEIGHT - 2);

        if(wallsToRemove > 0 && Maze[pickVertical][pickHorizontal].getCell() == Pathway.FULL
                && Maze[pickVertical][pickHorizontal + 1].getCell() == Pathway.FULL
                && Maze[pickVertical][pickHorizontal + 2].getCell() == Pathway.FULL)
        {
            Maze[pickVertical][pickHorizontal + 1].setCell(Pathway.PATH);
            wallsToRemove--;
        }

        if(wallsToRemove > 0 && Maze[pickVertical][pickHorizontal].getCell() == Pathway.FULL
                && Maze[pickVertical + 1][pickHorizontal].getCell() == Pathway.FULL
                && Maze[pickVertical + 2][pickHorizontal].getCell() == Pathway.FULL)
        {
            Maze[pickVertical + 1][pickHorizontal].setCell(Pathway.PATH);
            wallsToRemove--;
        }

        if(wallsToRemove > 0) {
            removeRandomWalls();
        }
    }

    public Pathway getMazeCell(int xPosition, int yPosition) {
        return Maze[yPosition][xPosition].getCell();
    }

    public Cell getIndividualCell (int horizontalPosition, int verticalPosition){
        return Maze[verticalPosition][horizontalPosition];
    }


    public void setMazeCell(int xPosition, int yPosition, Pathway pathway) {
        Maze[yPosition][xPosition].setCell(pathway);
    }

    public Cell[][] getMaze() {
        return Maze;
    }

    public static void revealMazeCell(int i, int k, Maze Maze){
        if (Maze.getIndividualCell(i, k).getInvisiblePath()) {
            Maze.getIndividualCell(i, k).setInvisiblePath(false);
            Maze.setMazeCell(i, k, Pathway.PATH);
        } else if (Maze.getIndividualCell(i, k).getInvisibleWall()) {
            Maze.getIndividualCell(i, k).setInvisibleWall(false);
            Maze.setMazeCell(i, k, Pathway.FULL);
        }
    }
}