package com.lyndonarmitage.sudoku;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;

/**
 * Created by Lyndon on 07/05/2015.
 */
public class SudokuTest {

    private static Logger logger = LoggerFactory.getLogger(SudokuTest.class);

    // @formatter:off
    private static final String testSudokuString =
            "003020600\n" +
            "900305001\n" +
            "001806400\n" +
            "008102900\n" +
            "700000008\n" +
            "006708200\n" +
            "002609500\n" +
            "800203009\n" +
            "005010300";
    // @formatter:on

    private static final int[][] testArray = {
            {0, 0, 3, 0, 2, 0, 6, 0, 0},
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };

    private static File tempSudokuFile;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Write out text to tempSudokuFile file
        tempSudokuFile = new File(".tmp", "test.sudoku");
        logger.info("Creating test file sudoku: {}", tempSudokuFile.getPath());
        if (tempSudokuFile.exists()) {
            logger.warn("Test file sudoku already exists, deleting");
            tempSudokuFile.delete();
        }
        tempSudokuFile.getParentFile().mkdirs();
        tempSudokuFile.createNewFile();
        FileWriter writer = new FileWriter(tempSudokuFile);
        writer.write(testSudokuString);
        writer.close();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        logger.info("Deleting test file sudoku: {}", tempSudokuFile.getPath());
        tempSudokuFile.delete();
    }

    @Test
    public void testGetHints() throws Exception {
        Sudoku sudoku = new Sudoku();
        assertEquals(0, sudoku.getHints());
        sudoku.setAbsolute(0, 0, 1);
        sudoku.setAbsolute(Sudoku.GRID_SIZE - 1, Sudoku.GRID_SIZE - 1, 1);
        assertEquals(2, sudoku.getHints());
        // TODO: Add test for out of bounds
        logger.info("\n{}", sudoku.toString());
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
        logger.info("\n{}", sudoku.toString());
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
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testSetRelative() throws Exception {
        Sudoku sudoku = new Sudoku();
        sudoku.setRelative(0, 0, 0, 0, 1);
        sudoku.setRelative(0, 0, 1, 0, 2);
        sudoku.setRelative(1, 0, 0, 0, 1);
        sudoku.setRelative(1, 0, 1, 0, 2);
        sudoku.setRelative(1, 1, 0, 0, 1);
        sudoku.setRelative(1, 1, 1, 0, 2);
        sudoku.setRelative(2, 2, 0, 0, 1);
        sudoku.setRelative(2, 2, 1, 0, 2);
        assertEquals(1, sudoku.getAbsolute(0, 0));
        assertEquals(2, sudoku.getAbsolute(1, 0));
        assertEquals(1, sudoku.getAbsolute(3, 0));
        assertEquals(2, sudoku.getAbsolute(4, 0));
        assertEquals(1, sudoku.getAbsolute(3, 3));
        assertEquals(2, sudoku.getAbsolute(4, 3));
        assertEquals(1, sudoku.getAbsolute(6, 6));
        assertEquals(2, sudoku.getAbsolute(7, 6));
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testIsBoxComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        assertFalse(sudoku.isBoxComplete(0, 0));
        for (int x = 0; x < Sudoku.BOX_SIZE; x++) {
            for (int y = 0; y < Sudoku.BOX_SIZE; y++) {
                sudoku.setRelative(0, 0, x, y, 1);
            }
        }
        assertTrue(sudoku.isBoxComplete(0, 0));
        logger.info("\n{}", sudoku.toString());
    }


    @Test
    public void testIsRowComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        int y = 0;
        assertFalse(sudoku.isRowComplete(y));
        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            sudoku.setAbsolute(x, y, x + 1);
        }
        assertTrue(sudoku.isRowComplete(y));
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testIsColumnComplete() throws Exception {
        Sudoku sudoku = new Sudoku();
        int x = 0;
        assertFalse(sudoku.isColumnComplete(x));
        for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
            sudoku.setAbsolute(x, y, y + 1);
        }
        assertTrue(sudoku.isColumnComplete(x));
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testNewSudokuFromArray() throws Exception {
        Sudoku sudoku = new Sudoku(testArray);
        assertEquals("Parsing integer array did not work correctly", testSudokuString, sudoku.toString());
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testParseStream() throws Exception {
        // Convert the given string into a stream
        byte[] bytes = testSudokuString.getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        // Try to parse it
        Sudoku sudoku = new Sudoku(stream);
        assertEquals("ParseStream did not work correctly", testSudokuString, sudoku.toString());
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testParseString() throws Exception {
        // Parse the String
        Sudoku sudoku = new Sudoku(testSudokuString);
        assertEquals("ParseString did not work correctly", testSudokuString, sudoku.toString());
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testParseFile() throws Exception {
        Sudoku sudoku = new Sudoku(tempSudokuFile);
        assertEquals("ParseFile did not work correctly", testSudokuString, sudoku.toString());
        logger.info("\n{}", sudoku.toString());
    }

    @Test
    public void testSolve() throws Exception {
        Sudoku sudoku = new Sudoku();
        sudoku.solve(new DummySolver());
        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
                assertEquals(x + "," + y + " did not match expected output", 1, sudoku.getAbsolute(x, y));
            }
        }
        logger.info("\n{}", sudoku.toString());
    }
}