package com.lyndonarmitage.sudoku;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Lyndon on 07/05/2015.
 */
public class SudokuTest {

    @Test
    public void testGetHints() throws Exception {
        Sudoku sudoku = new Sudoku();
        assertEquals(0, sudoku.getHints());
        sudoku.setAbsolute(0, 0, 1);
        sudoku.setAbsolute(Sudoku.GRID_SIZE - 1, Sudoku.GRID_SIZE - 1, 1);
        assertEquals(2, sudoku.getHints());
        // TODO: Add test for out of bounds
    }

    @Test
    public void testSetGetAbsolute() throws Exception {
        Sudoku sudoku = new Sudoku();
        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
                sudoku.setAbsolute(x, y, x + 1);
            }
        }

        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
                assertEquals("Get value not correct", x + 1, sudoku.getAbsolute(x, y));
            }
        }

        // TODO: Add test for out of bounds
    }

    @Test
    public void testGetRelative() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void testSetRelative() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void testIsBoxComplete() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void testIsRowComplete() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void testIsColumnComplete() throws Exception {
        fail("Not implemented");
    }
}