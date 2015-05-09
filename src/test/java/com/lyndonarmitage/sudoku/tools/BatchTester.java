package com.lyndonarmitage.sudoku.tools;

import com.lyndonarmitage.sudoku.Sudoku;
import com.lyndonarmitage.sudoku.SudokuException;
import com.lyndonarmitage.sudoku.SudokuSolver;
import com.lyndonarmitage.sudoku.solvers.SimpleLogicSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * A quick tool test a whole bunch of sudoku files to
 */
public class BatchTester {
    private static final Logger logger = LoggerFactory.getLogger(BatchTester.class);

    /**
     * Main entry point for tool
     *
     * @param args <ul>
     *             <li>arg[0] is the folder the sudokus are in</li>
     *             <li>arg[1] is the optional file pattern to use to only process certain files (e.g."\d+\.sudoku.txt")</li>
     *             <li>arg[2] is the optional option of which solver to use, currently only supports "logic"</li>
     *             </ul>
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("First argument should be folder of sudokus");
            System.exit(-1);
        }
        String filePattern = null;
        File folder = new File(args[0]);
        logger.info("Using folder: {}", folder.getPath());
        if (args.length >= 2) {
            filePattern = args[1];
            logger.info("Using file pattern: {}", filePattern);
        } else {
            logger.warn("No file pattern set, using all files in directory.");
        }
        SudokuSolver solver = null;
        if (args.length >= 3) {
            switch (args[2].toLowerCase()) {
                case "logic":
                    logger.info("Using logic solver");
                    solver = new SimpleLogicSolver();
                    break;
                default:
                    logger.warn("Using default logic solver");
                    solver = new SimpleLogicSolver();
            }
        } else {
            logger.warn("Using default logic solver");
            solver = new SimpleLogicSolver();
        }

        File[] sudokuFiles = null;
        if (filePattern != null) {
            final String finalFilePattern = filePattern;
            sudokuFiles = folder.listFiles((folder1, name) -> {
                File file = new File(folder1, name);
                return !file.isDirectory() && name.matches(finalFilePattern);
            });
        } else {
            sudokuFiles = folder.listFiles();
        }
        if (sudokuFiles != null) {
            int completed = 0;
            int successes = 0;
            int errors = 0;

            for (File file : sudokuFiles) {
                Sudoku sudoku = null;
                try {
                    sudoku = new Sudoku(file);
                } catch (IOException | SudokuException e) {
                    logger.error("Error reading sudoku file " + file.getName(), e);
                }
                if (sudoku != null) {
                    try {
                        sudoku.solve(solver);
                        if (sudoku.getHints() >= 81) {
                            successes++;
                        } else {
                            System.out.println("Failed to completely solve " + file.getName());
                        }
                        completed++;
                    } catch (SudokuException e) {
                        logger.error("Error solving sudoku file " + file.getName(), e);
                        errors++;
                    }
                } else {
                    errors++;
                }
            }
            System.out.println("Completed " + completed + "/" + sudokuFiles.length + " sudokus");
            System.out.println(successes + "/" + completed + " sudokus were completely solved");
            System.out.println(errors + " errors occurred");
        } else {
            System.err.println(folder.getPath() + " is not a folder");
        }

    }
}
