package ca.sfu.cmpt213.a2.model;
/**
 This is my TreasureHunter class, this class is responsible for controlling the treasure hunter. It is mainly
 responsible for moving him around the maze and providing his positioning but it also tracks his life status.
 */
public class TreasureHunter {
    private int horizontalPosition;
    private int verticalPosition;
    private boolean lifeStatus;

    public TreasureHunter() {
        horizontalPosition = 1;
        verticalPosition = 1;
        lifeStatus = true;
    }

    public void goUp() {
        verticalPosition--;
    }

    public void goLeft() {
        horizontalPosition--;
    }

    public void goDown() {
        verticalPosition++;
    }

    public void goRight() {
        horizontalPosition++;
    }

    public void setToDead() {
        lifeStatus = false;
    }

    public boolean checkIfAlive() {
        return lifeStatus;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public void callReveal(TreasureHunter treasureHunter, Maze Maze){
        revealNearbyCells(treasureHunter, Maze);
    }

    private void revealNearbyCells(TreasureHunter treasureHunter, Maze currentMaze) {
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition(),  treasureHunter.getVerticalPosition(), currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() + 1,  treasureHunter.getVerticalPosition(), currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() - 1,  treasureHunter.getVerticalPosition(), currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition(),  treasureHunter.getVerticalPosition() - 1, currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() + 1,  getVerticalPosition() - 1, currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() - 1,  getVerticalPosition() - 1, currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() - 1,  treasureHunter.getVerticalPosition() + 1, currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition() + 1,  treasureHunter.getVerticalPosition() + 1, currentMaze);
        Maze.revealMazeCell(treasureHunter.getHorizontalPosition(),  treasureHunter.getVerticalPosition() + 1, currentMaze);
    }
}