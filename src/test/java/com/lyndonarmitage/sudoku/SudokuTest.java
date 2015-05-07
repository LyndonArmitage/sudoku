package com.lyndonarmitage.sudoku;

import org.junit.Test;

import static org.junit.Assert.*;

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
        System.out.println(sudoku);
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
        System.out.println(sudoku);
    }

    @Test
    public void testGetRelative() throws Exception {
        Sudoku sudoku = new Sudoku();
        assertEquals(0, sudoku.getRelative(0, 0, 0, 0));
        sudoku.setAbsolute(0, 0, 1);
        sudoku.setAbsolute(0, 1, 2);
        sudoku.setAbsolute(3, 0, 3);
        assertEquals(1, sudoku.getRelative(0, 0, 0, 0));
        assertEquals(2, sudoku.getRelative(0, 0, 0, 1));
        assertEquals(3, sudoku.getRelative(1, 0, 0, 0));
        System.out.println(sudoku);
    }

    @Test
    public void testSetRelative() throws Exception {
        Sudoku sudoku = new Sudoku();
        sudoku.setRelative(0, 0 ,0, 0, 1);
        sudoku.setRelative(0, 0 ,1, 0, 2);
        sudoku.setRelative(1, 0 ,0, 0, 1);
        sudoku.setRelative(1, 0 ,1, 0, 2);
        sudoku.setRelative(1, 1 ,0, 0, 1);
        sudoku.setRelative(1, 1 ,1, 0, 2);
        sudoku.setRelative(2, 2 ,0, 0, 1);
        sudoku.setRelative(2, 2 ,1, 0, 2);
        assertEquals(1, sudoku.getAbsolute(0, 0));
        assertEquals(2, sudoku.getAbsolute(1, 0));
        assertEquals(1, sudoku.getAbsolute(3, 0));
        assertEquals(2, sudoku.getAbsolute(4, 0));
        assertEquals(1, sudoku.getAbsolute(3, 3));
        assertEquals(2, sudoku.getAbsolute(4, 3));
        assertEquals(1, sudoku.getAbsolute(6, 6));
        assertEquals(2, sudoku.getAbsolute(7, 6));
        System.out.println(sudoku);
    }

    @Test
    public void testIsBoxComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        assertFalse(sudoku.isBoxComplete(0, 0));
        for (int x = 0; x < Sudoku.BOX_SIZE; x ++) {
            for (int y = 0; y < Sudoku.BOX_SIZE; y ++) {
                sudoku.setRelative(0, 0, x, y, 1);
            }
        }
        assertTrue(sudoku.isBoxComplete(0, 0));
        System.out.println(sudoku);
    }


    @Test
    public void testIsRowComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        int y = 0;
        assertFalse(sudoku.isRowComplete(y));
        for (int x = 0; x < Sudoku.GRID_SIZE; x ++) {
            sudoku.setAbsolute(x, y, x + 1);
        }
        assertTrue(sudoku.isRowComplete(y));
        System.out.println(sudoku);
    }

    @Test
    public void testIsColumnComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        int x = 0;
        assertFalse(sudoku.isColumnComplete(x));
        for (int y = 0; y < Sudoku.GRID_SIZE; y ++) {
            sudoku.setAbsolute(x, y, y + 1);
        }
        assertTrue(sudoku.isColumnComplete(x));
        System.out.println(sudoku);
    }
}