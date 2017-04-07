package generation;

import java.util.ArrayList;
import java.util.Iterator;

public class MazeBuilderEller extends MazeBuilder implements Runnable {

    ArrayList<EllerSet> sets = new ArrayList<EllerSet>();

    public MazeBuilderEller() {
        super();
        System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
    }

    public MazeBuilderEller(boolean det) {
        super(det);
        System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
    }

    @Override
    protected void generatePathways() {

        for (int y = 0; y < height; y++) {

            // iterate through row, adding set-less tiles to a new set
            // also randomly adds eastern neighbor to set of current tile if the two tiles are in different sets
            for (int x = 0; x < width; x++) {

                int currentTileSetId = this.inSet(x,y); // the set id of the current tile

                // checks if current tile is in a set, adds it to one if not
                if (currentTileSetId == -1) {
                    EllerSet newSet = new EllerSet(sets.size(), width);
                    sets.add(newSet);

                    newSet.addTile(x, y);
                    currentTileSetId = sets.size() - 1; // assign currentTileSetId and increment total sets
                }


                System.out.println("Current Set ID prior to next-tile check is: " + currentTileSetId);
                // check if next tile is in bounds
                if (x + 1 < width) {
                    int nextTileSetId = this.inSet(x + 1, y); // the set id of the tile to the right of the current tile

                    // check if current tile and next tile are in different sets
                    // if so, gives a 50% chance to destroy the wall between them and add the right tile to the current set
                    System.out.println("Attempting to smash through wall...");
                    if (nextTileSetId != currentTileSetId && Math.random() >= 0.5) {

                        System.out.println("Attempt Succeeded!");

                        if (nextTileSetId != -1) {
                            sets.get(nextTileSetId).annex(x + 1, y);
                            System.out.println("\nAnnexed Set Info--");
                            sets.get(nextTileSetId).printInfo();
                        }

                        System.out.println("\nCurrentTileId: " + currentTileSetId);
                        //Wall dyingWall = new Wall(x, y, CardinalDirection.East);
                        //cells.deleteWall(dyingWall);
                        sets.get(currentTileSetId).addTile(x + 1, y);
                    } else {
                        System.out.println("Attempt Failed.\n");
                    }
                }

            }

            // clean empty sets'
            System.out.println("First Cleaning Begun: " + sets.size() + " sets before.");
            this.cleanSets();
            System.out.println("First Cleaning Ended: " + sets.size() + " sets after.");

            // returns to beginning of same row and iterates through it again
            // chooses at least one southern wall to destroy per set in the row
            if (y + 1 < height) {
                for (EllerSet currentSet : sets) {
                    //if (currentSet.checkDuplicates()) {
                    //    System.out.println("Issue found!");
                    //}
                    int holeAmount = (int) (Math.random() * (currentSet.getSize() - 1) + 1);
                    int [][] digTiles = currentSet.provideDigTiles(holeAmount);
                    for (int [] digTile : digTiles) {
                        //Wall digWall = new Wall(digTile[0], digTile[1], CardinalDirection.South);
                        //cells.deleteWall(digWall);
                        if (currentSet.contains(digTile[0], digTile[1] + 1)) {
                            System.out.println("ERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERRORERROR!!!");
                        }
                        currentSet.addTile(digTile[0], digTile[1] + 1);
                    }
                }

                // clean empty sets again, ditch upper rows
                System.out.println("Second Cleaning Begun: " + sets.size() + " sets before.");
                this.cleanSets();
                System.out.println("Second Cleaning Ended: " + sets.size() + " sets after.");
            }
        }
    }

    // checks if selected tile is in a set
    // returns setId if true, -1 if false
    private int inSet(int x, int y) {
        System.out.println("Checking if (" + x + ", " + y + ") is in a set...");
        int inSet = -1;
        for (EllerSet containerSet: this.sets) {
            if (containerSet.contains(x, y)) inSet = containerSet.getId();
        }
        if (inSet == -1) System.out.println("(" + x + ", " + y + ") is not in a set.");
        if (inSet != -1) System.out.println("(" + x + ", " + y + ") is already in set " + inSet);
        return inSet;
    }

    // removes all empty sets from sets array list and updates new set ids accordingly
    private void cleanSets() {
        int setsRemoved = 0;

        System.out.println("List of Sets before Cleaning: ");
        for (EllerSet printSet : this.sets) {
            System.out.print(printSet.getId() + " - ");
        }


        Iterator<EllerSet> iter = this.sets.iterator();
        while (iter.hasNext()) {
            EllerSet currentSet = iter.next();
            //if (currentSet.checkDuplicates()) {
            //    System.out.println("Issue found!");
            //}

            // for every set, modifies id by the number of sets removed in the process of the cleanup
            currentSet.setId(currentSet.getId() - setsRemoved);

            // if the set is empty, remove it from the list and increase sets removed by 1
            if (currentSet.getSize() == 0) {
                System.out.println("Removing set " + currentSet.getId());
                iter.remove();
                setsRemoved++;
            }
        }

        for (EllerSet tallSet : this.sets) {
            tallSet.clearRow();
        }

        System.out.println("List of Sets after  Cleaning: ");
        for (EllerSet printSet : this.sets) {
            System.out.print(printSet.getId() + " - ");
        }
    }

    private Wall extractWallFromCandidateSetRandomly(final ArrayList<Wall> candidates) {
        return candidates.remove(random.nextIntWithinInterval(0, candidates.size()-1));
    }


    /**
     * Updates a list of all walls that could be removed from the maze based on walls towards new cells
     * @param x
     * @param y
     */
    private void updateListOfWalls(int x, int y, ArrayList<Wall> walls) {
        Wall wall = new Wall(x, y, CardinalDirection.East) ;
        for (CardinalDirection cd : CardinalDirection.values()) {
            wall.setWall(x, y, cd);
            if (cells.canGo(wall)) //
            {
                walls.add(new Wall(x, y, cd));
            }
        }
    }

}