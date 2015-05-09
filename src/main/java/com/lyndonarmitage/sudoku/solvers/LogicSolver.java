package com.lyndonarmitage.sudoku.solvers;

import com.lyndonarmitage.sudoku.Sudoku;
import com.lyndonarmitage.sudoku.SudokuException;
import com.lyndonarmitage.sudoku.SudokuSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lyndon on 09/05/2015.
 */
public class LogicSolver implements SudokuSolver {
    private static final Logger logger = LoggerFactory.getLogger(LogicSolver.class);
    private PencilMark[][] pencilMarks;

    public static void main(String[] args) throws IOException, SudokuException {
        File file = null;
        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            JFileChooser chooser = new JFileChooser(new File("."));
            chooser.setDialogTitle("Choose Sudoku file");
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                if (file == null || file.isDirectory()) {
                    return;
                }
            }
        }
        Sudoku sudoku = new Sudoku(file);
        System.out.println("Loaded Sudoku: ");
        System.out.println(sudoku.toString());
        sudoku.solve(new LogicSolver());
        System.out.println("Solved Sudoku: ");
        System.out.println(sudoku.toString());
    }

    @Override
    public void solve(Sudoku sudoku) throws SudokuException {
        initPencilMarks(sudoku); // init the pencilMarks array now as we haven't solve the sudoku yet
        solveWithPencilMarks(sudoku);
    }

    /**
     * Runs an iteration attempting to solve the sudoku using a built Pencil Marks apporach.<br/>
     * You should call initPencilMarks before starting to use this method
     *
     * @param sudoku The sudoku being solved
     * @throws SudokuException
     * @see LogicSolver#initPencilMarks(Sudoku)
     */
    private void solveWithPencilMarks(Sudoku sudoku) throws SudokuException {
        int lastChanges; // count of how many changes were done in the last iteration
        long startTime = System.currentTimeMillis();
        do {
            lastChanges = 0;
            // perform basic logical steps
            lastChanges = runBasicLogicSolution(sudoku, lastChanges);
            if (sudoku.getHints() >= 81) {
                // sudoku has been solved
                break;
            }
            lastChanges = candidateLines(sudoku, lastChanges);


        } while (lastChanges > 0);
        if (sudoku.getHints() < 81) {
            logger.warn("Couldn't completely finish Sudoku, {} incomplete sections.", (81 - sudoku.getHints()));
        }
        logger.debug("Took {}ms", System.currentTimeMillis() - startTime);
    }

    private int runBasicLogicSolution(Sudoku sudoku, int lastChanges) throws SudokuException {
        for (int x = 0; x < 9; x++) {
            int[] column = sudoku.getColumn(x);
            for (int y = 0; y < 9; y++) {
                PencilMark mark = pencilMarks[x][y];
                if (sudoku.getAbsolute(x, y) == 0) {
                    if (mark.getRealValue() > 0) {
                        sudoku.setAbsolute(x, y, mark.getRealValue());
                        lastChanges++;
                    } else {
                        int[] row = sudoku.getRow(y);

                        // remove all the ones already present
                        for (int i = 0; i < 9; i++) {
                            mark.remove(row[i]);
                            mark.remove(column[i]);
                        }
                        if (mark.getRealValue() > 0) {
                            // we have worked out a value from the column and row alone
                            sudoku.setAbsolute(x, y, mark.getRealValue());
                            lastChanges++;
                        } else {

                            // convert x, y to get boxX, boxY and check the box it is in to limit possibilities further
                            int[][] box = sudoku.getBox(x / Sudoku.BOX_SIZE, y / Sudoku.BOX_SIZE);
                            for (int xPos = 0; xPos < Sudoku.BOX_SIZE; xPos++) {
                                for (int yPos = 0; yPos < Sudoku.BOX_SIZE; yPos++) {
                                    mark.remove(box[xPos][yPos]);
                                }
                            }
                            if (mark.getRealValue() > 0) {
                                // we have worked out a value by covering the column, row, and box
                                sudoku.setAbsolute(x, y, mark.getRealValue());
                                lastChanges++;
                            }
                        }
                    }
                    // at this point we have covered the basics in the same way SimpleLogicSolver does
                } else if (mark.size() > 1) {
                    mark = new PencilMark(sudoku.getAbsolute(x, y));
                    pencilMarks[x][y] = mark;
                }
            }
        }
        return lastChanges;
    }

    private int candidateLines(Sudoku sudoku, int lastChanges) throws SudokuException {
        // see https://www.sudokuoftheday.com/techniques/candidate-lines/ for details
        for (int boxX = 0; boxX < Sudoku.BOX_COUNT; boxX++) {
            for (int boxY = 0; boxY < Sudoku.BOX_COUNT; boxY++) {
                int[][] box = sudoku.getBox(boxX, boxY);
                PencilMark[][] boxPencilMarks = new PencilMark[Sudoku.BOX_SIZE][Sudoku.BOX_SIZE];
                for (int relX = 0; relX < Sudoku.BOX_SIZE; relX++) {
                    for (int relY = 0; relY < Sudoku.BOX_SIZE; relY++) {
                        int x = Sudoku.convertRelativeToAbsolute(boxX, relX);
                        int y = Sudoku.convertRelativeToAbsolute(boxY, relY);
                        int value = box[relX][relY];
                        PencilMark mark = pencilMarks[x][y];
                        boxPencilMarks[relX][relY] = mark;
                        if (value == 0) {
                            // TODO: Complete

                        }
                    }
                }


            }
        }
        return lastChanges;
    }

    /**
     * Initializes the pencil marks array for this instance of LogicSolver using the given sudoku.
     *
     * @param sudoku The sudoku to solve
     * @throws SudokuException
     */
    private void initPencilMarks(Sudoku sudoku) throws SudokuException {
        pencilMarks = new PencilMark[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku.getAbsolute(x, y) != 0) {
                    pencilMarks[x][y] = new PencilMark(sudoku.getAbsolute(x, y));
                } else {
                    pencilMarks[x][y] = new PencilMark();
                }
            }
        }
    }


    private class PencilMark {
        private Set<Integer> couldBes;

        public PencilMark() {
            couldBes = new HashSet<>(9);
            for (int i = 1; i <= 9; i++) {
                couldBes.add(i);
            }
        }

        public PencilMark(int actualValue) {
            couldBes = new HashSet<>(1);
            couldBes.add(actualValue);
        }

        public void add(int value) {
            if (value > 0 && value <= 9) {
                couldBes.add(value);
            }
        }

        public void remove(int value) {
            couldBes.remove(value);
        }

        public int size() {
            return couldBes.size();
        }

        public int getRealValue() {
            if (couldBes.size() == 1) {
                return couldBes.iterator().next();
            } else {
                return 0;
            }
        }
    }
}
