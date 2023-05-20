package ca.sfu.cmpt213.a2.model;
/**
 This is my Cell class and it is responsible for constructing cell objects and keeping track of and modifying all the
 inhabitants of the cell except for walls and paths as those are handled by Pathway enum class.
 */
public class Cell {
    private Pathway pathwayOfCell;
    private boolean wasVisited;
    private final boolean [] contents;
    private int guardianCount;

    public boolean getTreasureHunter() {
        return contents [0];
    }

    public void setTreasureHunter(boolean trueOrFalse) {
        contents [0] = trueOrFalse;
    }

    public boolean getTreasureHunterDead() {
        return contents [1];
    }

    public void setTreasureHunterDead(boolean aliveOrDead) {

        if(aliveOrDead)
        {
            contents [1] = true;
            setTreasureHunter(false);
        }
        else contents [1] = false;
    }

    public boolean getRelic() {
        return contents [2];
    }

    public void setRelic(boolean relic) {
        contents [2] = relic;
    }

    public boolean getInvisiblePath() {
        return contents [3];
    }

    public void setInvisiblePath(boolean invisiblePath) {
        contents [3] = invisiblePath;
    }

    public boolean getInvisibleWall() {
        return contents [4];
    }

    public void setInvisibleWall(boolean invisibleWall) {
        contents [4] = invisibleWall;
    }

    public int getGuardianCount() {
        return guardianCount;
    }

    public void incrementGuardianCount() {
        guardianCount++;
    }

    public void decrementGuardianCount() {
        guardianCount--;
    }

    public Cell() {
        contents = new boolean[]{false, false, false, false, false, false};
        guardianCount = 0;
        pathwayOfCell = Pathway.FULL;
        wasVisited = false;
    }

    public void setCell(Pathway cellPathway) {
        switch(cellPathway) {
            case PATH ->  pathwayOfCell = Pathway.PATH;
            case FULL ->  pathwayOfCell = Pathway.FULL;
            default -> {
                assert false;
            }
        }
    }

    public Pathway getCell() {
        return pathwayOfCell;
    }

    public void setVisited() {
        wasVisited = true;
    }

    public boolean getVisitedInverted() {
        return !wasVisited;
    }
}

