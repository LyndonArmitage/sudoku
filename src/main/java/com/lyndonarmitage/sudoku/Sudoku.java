package com.lyndonarmitage.sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Sudoku class <br />
 * Represents a whole Sudoku with an underlying 2D integer array.<br />
 * Values of each square should be between 0-9, 0 is a placeholder for empty
 */
public class Sudoku {

    public static final int BOX_SIZE = 3;
    public static final int BOX_COUNT = 3;
    public static final int GRID_SIZE = BOX_SIZE * BOX_COUNT;
    private int[][] grid;

    /**
     * Initialize empty Sudoku (all values are 0)
     */
    public Sudoku() {
        this.grid = new int[GRID_SIZE][GRID_SIZE]; // empty grid
    }

    /**
     * Initialize Sudoku based on an existing array.
     * This array will be copied
     *
     * @param grid A fixed sized (9x9) 2D array representing a Sudoku. Unknown values are 0
     * @see Sudoku#parseArray(int[][])
     */
    public Sudoku(int[][] grid) throws SudokuException {
        parseArray(grid);
    }

    /**
     * Load Sudoku from a file
     *
     * @param sudokuFile The text file to load from
     * @throws IOException
     * @throws SudokuException
     * @see Sudoku#parseFile(File)
     */
    public Sudoku(File sudokuFile) throws IOException, SudokuException {
        parseFile(sudokuFile);
    }

    /**
     * Load a sudoku from a generic InputStream (will not close the stream)
     *
     * @param sudoku The InputStream
     * @throws SudokuException
     * @see Sudoku#parseStream(InputStream)
     */
    public Sudoku(InputStream sudoku) throws SudokuException {
        parseStream(sudoku);
    }

    /**
     * Load the Sudoku from a String
     *
     * @param sudoku The string to load from
     * @throws SudokuException
     * @see Sudoku#parseString(String)
     */
    public Sudoku(String sudoku) throws SudokuException {
        parseString(sudoku);
    }

    /**
     * Parses a Sudoku as a 2D array.
     * The given array will be validated and copied into the new Sudoku
     *
     * @param grid A fixed sized (9x9) 2D array representing a Sudoku. Unknown values are 0
     * @throws SudokuException
     */
    public void parseArray(int[][] grid) throws SudokuException {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        // clone the multidimensional array
        if (grid.length != GRID_SIZE) {
            throw new SudokuException(this, "Couldn't parse array wrong size should be " + GRID_SIZE + " was " + grid.length);
        }

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                int value = grid[y][x];
                if (value < 0 || value > 9) {
                    throw new SudokuException(this, "Couldn't parse array, entry at row " + y + " column " + x + " was " + value + " not a number between 0-9");
                }
                this.grid[x][y] = value;
            }

        }
    }

    /**
     * Parses a Sudoku text file into the internal 2D integer array
     *
     * @param file The file to parse
     * @throws SudokuException
     * @throws IOException
     * @see Sudoku#parseString(String)
     */
    public void parseFile(File file) throws SudokuException, IOException {
        FileInputStream fin = new FileInputStream(file);
        parseStream(fin);
        fin.close();
    }

    /**
     * Parses a stream of data in as a Sudoku into the internal 2D integer array.
     *
     * @param in The sudoku stream
     * @throws SudokuException
     * @see Sudoku#parseString(String)
     */
    public void parseStream(InputStream in) throws SudokuException {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        parseString(scanner.hasNext() ? scanner.next() : "");
    }

    /**
     * Parses a Sudoku string into the internal 2D integer array. <br />
     * In the form of: <br />
     * <pre>
     * 003020600
     * 900305001
     * 001806400
     * 008102900
     * 700000008
     * 006708200
     * 002609500
     * 800203009
     * 005010300
     * </pre>
     *
     * @param sudoku The Sudoku string
     * @throws SudokuException
     */
    public void parseString(String sudoku) throws SudokuException {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        if (sudoku == null || sudoku.length() <= 0) {
            throw new SudokuException(this, "Cannot parse empty/null string as a Sudoku");
        }
        String[] rows = sudoku.split("\\r?\\n");
        if (rows.length != 9) {
            throw new SudokuException(this, "Couldn't parse String, wrong number of rows. Found " + rows.length + " should be 9");
        }
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            if (row.length() != 9) {
                throw new SudokuException(this, "Couldn't parse String, wrong number of columns on row " + y + ". Found " + row.length() + " should be 9");
            }
            for (int x = 0; x < row.length(); x++) {
                int value;
                String valueStr = row.substring(x, x + 1);
                try {
                    value = Integer.parseInt(valueStr);
                } catch (NumberFormatException e) {
                    throw new SudokuException(this, "Couldn't parse String, entry at row " + y + " column " + x + " was " + valueStr + " not a number between 0-9");
                }
                this.grid[x][y] = value;
            }
        }
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
            throw new SudokuException(this, "Value " + value + " is not in range");
        }
        if (x < 0 || x > GRID_SIZE) {
            throw new SudokuException(this, "x " + x + " is out of bound");
        }
        if (y < 0 || y > GRID_SIZE) {
            throw new SudokuException(this, "y " + y + " is out of bound");
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
            throw new SudokuException(this, "x " + x + " is out of bound");
        }
        if (y < 0 || y > GRID_SIZE) {
            throw new SudokuException(this, "y " + y + " is out of bound");
        }
        return this.grid[x][y];
    }

    private int convertRelativeToAbsolute(int box, int rel) {
        return rel + (box * BOX_SIZE);
    }

    private void testRelative(int boxX, int boxY, int relX, int relY) throws SudokuException {
        if (boxX >= BOX_COUNT) {
            throw new SudokuException(this, "boxX is out of bounds: is " + boxX + ", must be less than " + BOX_COUNT);
        }
        if (boxY >= BOX_COUNT) {
            throw new SudokuException(this, "boxY is out of bounds: is " + boxY + ", must be less than " + BOX_COUNT);
        }
        if (relX >= BOX_SIZE) {
            throw new SudokuException(this, "relX is out of bounds: is " + relX + ", must be less than " + BOX_SIZE);
        }
        if (relY >= BOX_SIZE) {
            throw new SudokuException(this, "relY is out of bounds: is " + relY + ", must be less than " + BOX_SIZE);
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
            throw new SudokuException(this, "Value " + value + " is not in range");
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
        for (int x = 0; x < BOX_SIZE; x++) {
            for (int y = 0; y < BOX_SIZE; y++) {
                if (getRelative(boxX, boxY, x, y) > 0) {
                    count++;
                }
            }
        }
        return (count >= BOX_SIZE * BOX_SIZE);
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
        for (int x = 0; x < GRID_SIZE; x++) {
            if (this.grid[x][row] > 0) {
                count++;
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
        for (int y = 0; y < GRID_SIZE; y++) {
            if (this.grid[column][y] > 0) {
                count++;
            }
        }
        return (count >= GRID_SIZE);
    }

    /**
     * Check to make sure that the given x and y isn't occupied.
     *
     * @param x absolute x
     * @param y absolute y
     * @return false if that part of the grid is occupied
     */
    public boolean canPutAbsolute(int x, int y) {
        // TODO: Add test
        return this.grid[x][y] == 0;
    }

    /**
     * Check to make sure a given x and y isn't occupied <b>and</b> that you can place the desired value there
     *
     * @param x     absolute x
     * @param y     absolute y
     * @param value value to place there
     * @return false if the space is occupied or putting the value there is an invalid move
     */
    public boolean canPutAbsolute(int x, int y, int value) {
        // TODO: Add test
        if (canPutAbsolute(x, y)) {
            throw new RuntimeException("Not yet implemented");
        } else {
            return false;
        }
    }

    /**
     * Check to make sure that the given relative position isn't occupied.
     *
     * @param boxX Box x (0,1,2)
     * @param boxY Box y (0,1,2)
     * @param relX The relative x within the box
     * @param relY The relative y within the box
     * @return false if the position is occupied
     * @throws SudokuException
     */
    public boolean canPutRelative(int boxX, int boxY, int relX, int relY) throws SudokuException {
        // TODO: Add test
        return getRelative(boxX, boxY, relX, relY) == 0;
    }

    /**
     * Check to make sure that the given relative position isn't occupied <b>and</b> that you can place the given value there
     *
     * @param boxX  Box x (0,1,2)
     * @param boxY  Box y (0,1,2)
     * @param relX  The relative x within the box
     * @param relY  The relative y within the box
     * @param value The value to place there
     * @return false if the position is occupied or placing value there is an invalid move
     * @throws SudokuException
     */
    public boolean canPutRelative(int boxX, int boxY, int relX, int relY, int value) throws SudokuException {
        // TODO: Add test
        if (canPutRelative(boxX, boxY, relX, relY)) {
            throw new RuntimeException("Not yet implemented");
        } else {
            return false;
        }
    }

    /**
     * @return true if the Sudoku is valid
     * @throws SudokuException
     */
    public boolean isValid() throws SudokuException {
        for (int y = 0; y < GRID_SIZE; y++) {
            if (!isRowValid(y)) {
                return false;
            }
        }
        for (int x = 0; x < GRID_SIZE; x++) {
            if (!isColumnValid(x)) {
                return false;
            }
        }
        // Not sure if this check is really needed
        for (int x = 0; x < BOX_SIZE; x++) {
            for (int y = 0; y < BOX_SIZE; y++) {
                if (!isBoxValid(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param row row to check
     * @return true if the row is valid
     */
    public boolean isRowValid(int row) {
        Set<Integer> set = new HashSet<>(9);
        for (int x = 0; x < GRID_SIZE; x++) {
            if (this.grid[x][row] == 0) {
                return false;
            } else {
                set.add(this.grid[x][row]);
            }
        }
        return set.size() == 9;
    }

    /**
     * @param column column to check
     * @return true if the column is valid
     */
    public boolean isColumnValid(int column) {
        Set<Integer> set = new HashSet<>(9);
        for (int y = 0; y < GRID_SIZE; y++) {
            if (this.grid[column][y] == 0) {
                return false;
            } else {
                set.add(this.grid[column][y]);
            }
        }
        return set.size() == 9;
    }

    /**
     * @param boxX Box x (0,1,2)
     * @param boxY Box y (0,1,2)
     * @return true if the box is valid
     * @throws SudokuException
     */
    public boolean isBoxValid(int boxX, int boxY) throws SudokuException {
        testRelative(boxX, boxY, 0, 0);
        Set<Integer> set = new HashSet<>(9);
        for (int x = 0; x < BOX_SIZE; x++) {
            for (int y = 0; y < BOX_SIZE; y++) {
                int value = getRelative(boxX, boxY, x, y);
                if (value == 0) {
                    return false;
                } else {
                    set.add(value);
                }
            }
        }
        return set.size() == 9;
    }

    /**
     * Attempt to solve the Sudoku using the given solver.
     *
     * @param solver A class that implements the Solver interface
     * @throws SudokuException
     */
    public void solve(SudokuSolver solver) throws SudokuException {
        solver.solve(this);
    }

    /**
     * Returns a string version of the Sudoku that can be parsed by the parseString method
     *
     * @return A string version of the Sudoku
     * @see Sudoku#parseString(String)
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder((GRID_SIZE * GRID_SIZE) + GRID_SIZE);
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                builder.append(this.grid[x][y]);
            }
            if (y < GRID_SIZE - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }
}
