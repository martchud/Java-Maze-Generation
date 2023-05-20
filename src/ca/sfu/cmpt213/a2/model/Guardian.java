package ca.sfu.cmpt213.a2.model;
/**
 This is my Guardian class and its only responsibility is to track the guardian objects positioning and modify it.
 */
public class Guardian {
    private int horizontalPosition;
    private int verticalPosition;

    public Guardian(int horizontalPos, int verticalPos) {
        this.horizontalPosition = horizontalPos;
        this.verticalPosition = verticalPos;
    }

    public void Up() {
        verticalPosition--;
    }

    public void Down() {
        verticalPosition++;
    }

    public void Right() {
        horizontalPosition++;
    }

    public void Left() {
        horizontalPosition--;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }
}