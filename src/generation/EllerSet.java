package generation;

import java.util.Arrays;

public class EllerSet {

    int [][] tiles;
    int width;
    int id;
    int size;

    // cosnstructor
    public EllerSet(int id, int width) {
        this.size = 0;
        this.width = width;
        this.id = id;
        this.tiles = new int[this.width][2];
    }

    // getter for set id
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return this.size;
    }

    // add a coordinate tile to the tiles array
    // parameters are the x and y integer coordinates of the new tile
    public void addTile(int x, int y) {
        System.out.println("(" + x + ", " + y + ") added to set " + this.id + ".\n");

        if (this.contains(x,y)) System.out.println("Multiples Detected");

        // add new tile to tile array
        if (!this.contains(x, y)) {
            this.tiles[this.size][0] = x;
            this.tiles[this.size][1] = y;


            // increment size integer
            this.size++;
        }

        System.out.println("Current Set Profile");
        System.out.println("Set " + this.id);
        System.out.println("Size: " + this.size);

        for (int i = 0; i < this.size; i++) {
            System.out.println(Arrays.toString(tiles[i]));
        }
    }

    public void printInfo() {
        System.out.println("Current Set Profile");
        System.out.println("Set " + this.id);
        System.out.println("Size: " + this.size);

        for (int i = 0; i < this.size; i++) {
            System.out.println(Arrays.toString(tiles[i]));
        }
    }

    // returns a random selection of the set's tiles of the amount specified in the argument
    public int [][] provideDigTiles(int holeAmount) {
        System.out.println("Adding " + holeAmount + " holes to set " + this.id + ".");
        int [][] digTiles = new int[holeAmount][2];
        int [] tileIndexes = new int[size];
        for (int i = 0; i < size; i++) {
            tileIndexes[i] = i;
        }

        for (int i = 0; i < holeAmount; i++) {
            int selectedIndex = (int) (Math.random() * (tileIndexes.length - 1));
            digTiles[i][0] = this.tiles[tileIndexes[selectedIndex]][0];
            digTiles[i][1] = this.tiles[tileIndexes[selectedIndex]][1];
            System.out.println("Adding new digTile below (" + this.tiles[tileIndexes[selectedIndex]][0] + ", " + this.tiles[tileIndexes[selectedIndex]][1] + ").");
            //if (i > 0 && Arrays.equals(digTiles[i], digTiles[i - 1])) {
            //    System.out.println("Wait! Something's not quite right!");
            //}
            int [] newIndexes = new int[tileIndexes.length - 1];

            // populate new array minus the selected index
            int counter = 0;
            for (int j = 0; j < tileIndexes.length; j++) {
                if (j != selectedIndex) {
                    if (counter == newIndexes.length) {
                        System.out.println("Help!");
                        System.out.println("Something broken here!");
                    }
                    newIndexes[counter] = tileIndexes[j];
                    counter++;
                }
            }
            tileIndexes = newIndexes;
        }

        return digTiles;
    }

    // clears the row above the primary row for working with next row
    public void clearRow() {
        while (this.tiles[this.size - 1][1] > this.tiles[0][1]) {
            this.pop();
            this.size--;
        }
    }

    public void annex(int x, int y) {
        System.out.println("(" + x + ", " + y + ") is being annexed.");
        for (int i = 0; i < this.size; i++) {
            if (this.tiles[i][0] == x && this.tiles[i][1] == y) {
                this.pop(i);
                this.size--;
            }
        }
    }

    public boolean checkDuplicates() {
        for (int i = 0; i < this.size - 1; i++) {
            if (this.tiles[i] == this.tiles[i + 1]) return true;
        }
        return false;
    }

    // pops element from front of tile array and shifts array down
    // (doesn't actually modify the size of the 2d array)
    private void pop() {
        //System.out.println()
        for (int i = 0; i < this.size; i++) {
            this.tiles[i] = this.tiles[i+1];
        }
    }

    private void pop(int index) {
        for (int i = index; i < this.size; i++) {
            this.tiles[i] = this.tiles[i + 1];
        }
    }

    // check if specified integer coordinates are in the tiles array
    public boolean contains(int x, int y) {
        for (int i = 0; i < size; i++) {
            if (this.tiles[i][0] == x && this.tiles[i][1] == y) return true;
        }

        return false;
    }
}
