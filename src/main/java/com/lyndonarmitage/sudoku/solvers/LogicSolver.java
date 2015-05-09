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
        // TODO: Iterative Logic approach
        int lastChanges; // count of how many changes were done in the last iteration
        long startTime = System.currentTimeMillis();
        do {
            lastChanges = 0;
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    int[] row = sudoku.getRow(y);
                    int[] column = sudoku.getColumn(x);
                    Set<Integer> present = new HashSet<>(9);
                    for (int i = 0; i < 9; i++) {
                        if (row[i] != 0) {
                            present.add(row[i]);
                        }
                        if (column[i] != 0) {
                            present.add(column[i]);
                        }
                    }
                    // convert x, y to get boxX, boxY
                    int[][] box = sudoku.getBox(x / Sudoku.BOX_SIZE, y / Sudoku.BOX_SIZE);
                    for (int xPos = 0; xPos < Sudoku.BOX_SIZE; xPos++) {
                        for (int yPos = 0; yPos < Sudoku.BOX_SIZE; yPos++) {
                            if (box[xPos][yPos] != 0) {
                                present.add(box[xPos][yPos]);
                            }
                        }
                    }
                    if (present.size() == 8) {
                        int value = 45 - present.stream().mapToInt(Integer::intValue).sum();
                        if (!present.contains(value) && sudoku.canPutAbsolute(x, y, value)) {
                            lastChanges++;
                            sudoku.setAbsolute(x, y, value);
                            logger.debug("Setting value at {},{} to {}", x, y, value);
                        }
                    }
                }
            }
        } while (lastChanges > 0);
        if (sudoku.getHints() < 81) {
            logger.warn("Couldn't completely finish Sudoku, {} incomplete sections.", (81 - sudoku.getHints()));
        }
        logger.debug("Took {}ms", System.currentTimeMillis() - startTime);
    }
}
