package com.lyndonarmitage.sudoku;

/**
 * Created by Lyndon on 07/05/2015.
 */
public class Sudoku {

    private static final int BOX_SIZE = 3;
    private static final int BOX_COUNT = 3;
    private static final int GRID_SIZE = BOX_SIZE * BOX_COUNT;
    private final int[][] grid;

    /**
     * Initialize empty Sudoku (all values are 0)
     */
    public Sudoku() {
        grid = new int[GRID_SIZE][GRID_SIZE]; // empty grid
    }

    /**
     * Initialize Sudoku based on an existing array.
     * This array will be copied
     *
     * @param grid A fixed sized (9x9) 2D array representing a Sudoku. Unknown values are 0
     */
    public Sudoku(int[][] grid) {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        // clone the multidimensional array
        // TODO: Add validation of the grid (will likely need to copy whole thing manually to do this)
        for (int i = 0; i < grid.length; i++)
            this.grid[i] = grid[i].clone();
    }

    /**
     * @return The number of filled in grid positions (out of 81)
     */
    public int getHints() {
        int count = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (this.grid[x][y] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Set the absolute value of an x and y position
     *
     * @param x     The x position
     * @param y     The y position
     * @param value the Value to set it to (between 0 and 9)
     * @throws SudokuException
     */
    public void setAbsolute(int x, int y, int value) throws SudokuException {
        if (value < 0 || value > 9) {
            throw new SudokuException("Value " + value + " is not in range");
        }
        if (x < 0 || x > GRID_SIZE) {
            throw new SudokuException("x " + x + " is out of bound");
        }
        if (y < 0 || y > GRID_SIZE) {
            throw new SudokuException("y " + y + " is out of bound");
        }
        this.grid[x][y] = value;
    }

    /**
     * Gets the absolute value of a given x and y
     *
     * @param x The x position
     * @param y The y position
     * @return The value of the x and y position (0 if it is empty)
     * @throws SudokuException
     */
    public int getAbsolute(int x, int y) throws SudokuException {
        if (x < 0 || x > GRID_SIZE) {
            throw new SudokuException("x " + x + " is out of bound");
        }
        if (y < 0 || y > GRID_SIZE) {
            throw new SudokuException("y " + y + " is out of bound");
        }
        return this.grid[x][y];
    }

    private int convertRelativeToAbsolute(int box, int rel) {
        return rel + (box * BOX_SIZE);
    }

    private void testRelative(int boxX, int boxY, int relX, int relY) throws SudokuException {
        if (boxX > BOX_COUNT - 1) {
            throw new SudokuException("boxX is out of bounds: is " + boxX + ", must be less than " + BOX_COUNT);
        }
        if (boxY > BOX_COUNT - 1) {
            throw new SudokuException("boxY is out of bounds: is " + boxY + ", must be less than " + BOX_COUNT);
        }
        if (relX > BOX_SIZE - 1) {
            throw new SudokuException("relX is out of bounds: is " + relX + ", must be less than " + BOX_SIZE);
        }
        if (relY > BOX_SIZE - 1) {
            throw new SudokuException("relY is out of bounds: is " + relY + ", must be less than " + BOX_SIZE);
        }
    }

    /**
     * Get value based on boxX, boxY and relative x and y positions within the box
     *
     * @param boxX The box x
     * @param boxY The box y
     * @param relX The relative x within the box
     * @param relY The relative y within the box
     * @return The value of the field being referenced
     * @throws SudokuException
     */
    public int getRelative(int boxX, int boxY, int relX, int relY) throws SudokuException {
        testRelative(boxX, boxY, relX, relY);
        int absoluteX, absoluteY;
        absoluteX = convertRelativeToAbsolute(boxX, relX);
        absoluteY = convertRelativeToAbsolute(boxY, relY);
        return this.grid[absoluteX][absoluteY];
    }

    /**
     * Set value of s field based on boxX, boxY and relative x and y positions within the box
     *
     * @param boxX  The box x
     * @param boxY  The box y
     * @param relX  The relative x within the box
     * @param relY  The relative y within the box
     * @param value The value to set it to (0 means empty)
     * @throws SudokuException
     */
    public void setRelative(int boxX, int boxY, int relX, int relY, int value) throws SudokuException {
        if (value < 0 || value > 9) {
            throw new SudokuException("Value " + value + " is not in range");
        }
        testRelative(boxX, boxY, relX, relY);
        int absoluteX, absoluteY;
        absoluteX = convertRelativeToAbsolute(boxX, relX);
        absoluteY = convertRelativeToAbsolute(boxY, relY);
        this.grid[absoluteX][absoluteY] = value;
    }

    /**
     * Checks if a given box is complete <br />
     * <b>Note:</b> This does not test the validity of the box, just whether all its values have been filled.
     *
     * @param boxX Box x (0,1,2)
     * @param boxY Box y (0,1,2)
     * @return Whether the box is full
     * @throws SudokuException
     */
    public boolean isBoxComplete(int boxX, int boxY) throws SudokuException {
        testRelative(boxX, boxY, 0, 0);
        int count = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (getRelative(boxX, boxY, x, y) > 0) {
                    count++;
                }
            }
        }
        return (count >= GRID_SIZE * GRID_SIZE);
    }

    /**
     * Check if a row is complete
     * <b>Note:</b> This does not test the validity of the row, just whether all its values have been filled.
     *
     * @param row the row to check
     * @return Whether the row is full
     */
    public boolean isRowComplete(int row) {
        int count = 0;
        for (int x = 0; x < GRID_SIZE; x ++) {
            if (this.grid[x][row] > 0) {
                count ++;
            }
        }
        return (count >= GRID_SIZE);
    }

    /**
     * Check if a column is complete
     * <b>Note:</b> This does not test the validity of the column, just whether all its values have been filled.
     *
     * @param column the column to check
     * @return Whether the column is full
     */
    public boolean isColumnComplete(int column) {
        int count = 0;
        for (int y = 0; y < GRID_SIZE; y ++) {
            if (this.grid[column][y] > 0) {
                count ++;
            }
        }
        return (count >= GRID_SIZE);
    }
}
