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
    public static final String testSudokuString =
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

    public static final int[][] testArray = {
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

    public static final int[][] testArrayCompleted = {
            {4, 8, 3, 9, 2, 1, 6, 5, 7},
            {9, 6, 7, 3, 4, 5, 8, 2, 1},
            {2, 5, 1, 8, 7, 6, 4, 9, 3},
            {5, 4, 8, 1, 3, 2, 9, 7, 6},
            {7, 2, 9, 5, 6, 4, 1, 3, 8},
            {1, 3, 6, 7, 9, 8, 2, 4, 5},
            {3, 7, 2, 6, 8, 9, 5, 1, 4},
            {8, 1, 4, 2, 5, 3, 7, 6, 9},
            {6, 9, 5, 4, 1, 7, 3, 8, 2}
    };

    private static File tempSudokuFile;
    private static Sudoku invalidSudoku;
    private static Sudoku validRowsSudoku;
    private static Sudoku validColumnsSudoku;
    private static Sudoku all1sSudoku;
    private static Sudoku validSudoku;

    @SuppressWarnings("ResultOfMethodCallIgnored")
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

        logger.info("Loading valid and invalid Sudokus");
        invalidSudoku = new Sudoku(testArray);
        validRowsSudoku = new Sudoku();
        validColumnsSudoku = new Sudoku();
        all1sSudoku = new Sudoku();
        for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
            for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
                validRowsSudoku.setAbsolute(x, y, x + 1);
                validColumnsSudoku.setAbsolute(x, y, y + 1);
                all1sSudoku.setAbsolute(x, y, 1);
            }

        }
        validSudoku = new Sudoku(testArrayCompleted);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
    public void testSetAbsolute() throws Exception {
        Sudoku sudoku = new Sudoku();
        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
                sudoku.setAbsolute(x, y, x + 1);
            }
        }
    }

    @Test
    public void testGetAbsolute() throws Exception {
        for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
                assertEquals("Get value not correct", testArrayCompleted[y][x], validSudoku.getAbsolute(x, y));
            }
        }
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
    public void testParseArray() throws Exception {
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

    @Test
    public void testCanPutAbsolute() throws Exception {
        for (int y = 0; y < Sudoku.GRID_SIZE; y++) {
            for (int x = 0; x < Sudoku.GRID_SIZE; x++) {
                int value = testArrayCompleted[y][x];
                if (testArray[y][x] != 0) {
                    assertFalse(invalidSudoku.canPutAbsolute(x, y, value));
                    continue;
                }
                assertTrue("Problem placing " + value + " at " + x + "," + y, invalidSudoku.canPutAbsolute(x, y, value));
                assertFalse(all1sSudoku.canPutAbsolute(x, y, 2));
            }
        }
    }

    @Test
    public void testCanPutRelative() throws Exception {
        int[][] box;
        int[][] invalidBox;
        for (int boxX = 0; boxX < 3; boxX++) {
            for (int boxY = 0; boxY < 3; boxY++) {
                invalidBox = invalidSudoku.getBox(boxX, boxY);
                box = validSudoku.getBox(boxX, boxY);
                for (int relX = 0; relX < 3; relX++) {
                    for (int relY = 0; relY < 3; relY++) {
                        assertFalse(all1sSudoku.canPutRelative(boxX, boxY, relX, relY, box[relY][relX]));
                        if (invalidBox[relY][relX] != 0) {
                            assertFalse(invalidSudoku.canPutRelative(boxX, boxY, relX, relY, box[relY][relX]));
                        } else {
                            assertTrue(invalidSudoku.canPutRelative(boxX, boxY, relX, relY, box[relY][relX]));
                        }
                    }
                }
            }
        }

    }

    @Test
    public void testIsValid() throws Exception {
        assertFalse("Sudoku should be invalid", invalidSudoku.isValid());
        assertFalse("Sudoku should be invalid", validRowsSudoku.isValid());
        assertFalse("Sudoku should be invalid", all1sSudoku.isValid());
        assertTrue("Sudoku should be valid", validSudoku.isValid());
    }

    @Test
    public void testIsRowValid() throws Exception {
        for (int i = 0; i < Sudoku.GRID_SIZE; i++) {
            assertFalse("Row " + i + " should be invalid", invalidSudoku.isRowValid(i));
            assertTrue("Row " + i + " should be valid", validRowsSudoku.isRowValid(i));
            assertFalse("Row " + i + " should be invalid \n" + validColumnsSudoku.toString(), validColumnsSudoku.isRowValid(i));
            assertFalse("Row " + i + " should be invalid", all1sSudoku.isRowValid(i));
            assertTrue("Row " + i + " should be valid", validSudoku.isRowValid(i));
        }
    }

    @Test
    public void testIsColumnValid() throws Exception {
        for (int i = 0; i < Sudoku.GRID_SIZE; i++) {
            assertFalse("Column " + i + " should be invalid", invalidSudoku.isColumnValid(i));
            assertFalse("Column " + i + " should be invalid", validRowsSudoku.isColumnValid(i));
            assertFalse("Column " + i + " should be invalid", all1sSudoku.isColumnValid(i));
            assertTrue("Column " + i + " should be valid", validColumnsSudoku.isColumnValid(i));
            assertTrue("Column " + i + " should be valid", validSudoku.isColumnValid(i));
        }
    }

    @Test
    public void testIsBoxValid() throws Exception {
        for (int x = 0; x < Sudoku.BOX_SIZE; x++) {
            for (int y = 0; y < Sudoku.BOX_SIZE; y++) {
                assertFalse("Box " + x + "," + y + " should be invalid", invalidSudoku.isBoxValid(x, y));
                assertFalse("Box " + x + "," + y + " should be invalid", validRowsSudoku.isBoxValid(x, y));
                assertFalse("Box " + x + "," + y + " should be invalid", validColumnsSudoku.isBoxValid(x, y));
                assertFalse("Box " + x + "," + y + " should be invalid", all1sSudoku.isBoxValid(x, y));
                assertTrue("Box " + x + "," + y + " should be valid", validSudoku.isBoxValid(x, y));
            }
        }

    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Sudoku strings do not match", testSudokuString, invalidSudoku.toString());
    }

    @Test
    public void testGetColumn() throws Exception {
        int column = 0;
        int[] expected1 = new int[9];
        int[] expected2 = new int[9];
        for (int y = 0; y < 9; y++) {
            expected1[y] = testArray[y][column];
            expected2[y] = testArrayCompleted[y][column];
        }
        assertArrayEquals(expected1, invalidSudoku.getColumn(0));
        assertArrayEquals(expected2, validSudoku.getColumn(0));
    }

    @Test
    public void testGetRow() throws Exception {
        int[] expected1 = testArray[0];
        int[] expected2 = testArrayCompleted[0];
        assertArrayEquals(expected1, invalidSudoku.getRow(0));
        assertArrayEquals(expected2, validSudoku.getRow(0));
    }

    @Test
    public void testGetBox() throws Exception {
        int[][] expected1 = new int[3][3];
        int[][] expected2 = new int[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                expected1[x][y] = testArray[x][y];
                expected2[x][y] = testArrayCompleted[x][y];
                // not sure if correct
            }
        }
        assertArrayEquals(expected1, invalidSudoku.getBox(0, 0));
        assertArrayEquals(expected2, validSudoku.getBox(0, 0));
    }
}