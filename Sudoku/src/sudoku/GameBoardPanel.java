package sudoku;

/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2023/2024
 * Group Capstone Project
 * Group #6
 * 1 - 5026211081 - Muhammad Hudzaifah Abdurrasyid
 * 2 - 5026221034 - Dwi Indah Retnanik
 * 3 - 5026221200 - Anisa Fatin Idelia
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    // Board width/height in pixels


    // Define properties
    /**
     * The game board composes of 9x9 Cells (customized JTextFields)
     */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /**
     * It also contains a Puzzle with array numbers and isGiven
     */
    private Puzzle puzzle = new Puzzle();
    private CellInputListener listener = new CellInputListener();

    /**
     * Constructor
     */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));  // JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        // gambar grid 1x1
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);

                cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));

                super.add(cells[row][col]);   // JPanel
                //grid 3x3
                if ((col + 1) % 3 == 0 && col < SudokuConstants.GRID_SIZE - 1) {
                    Border border = new MatteBorder(0, 0, 0, 2, Color.BLACK);
                    cells[row][col].setBorder(new CompoundBorder(cells[row][col].getBorder(), border));
                }

                if ((row + 1) % 3 == 0 && row < SudokuConstants.GRID_SIZE - 1) {
                    Border border = new MatteBorder(0, 0, 2, 0, Color.BLACK);
                    cells[row][col].setBorder(new CompoundBorder(cells[row][col].getBorder(), border));
                }
            }
        }

        //pada cell yang harus diisi
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }

        super.setBorder(new LineBorder(Color.BLACK, 3)); //garis luar
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void newGame() {
        // Generate a new puzzle
        puzzle.newPuzzle(2);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                boolean isGiven = puzzle.isGiven[row][col];
                cells[row][col].newGame(puzzle.numbers[row][col], isGiven);

                // Disable editing for cells with given numbers
                cells[row][col].setEditable(!isGiven);
            }
        }
    }
    public void newHelp() {

        // Tampilkan pesan dialog dengan aturan Sudoku
        String helpMessage = "Aturan Sudoku:\n" +
                "1. Setiap baris harus berisi angka 1-9 tanpa duplikasi.\n" +
                "2. Setiap kolom harus berisi angka 1-9 tanpa duplikasi.\n" +
                "3. Setiap sub-grid 3x3 harus berisi angka 1-9 tanpa duplikasi.\n" +
                "4. Beberapa sel mungkin sudah diisi dan tidak dapat diubah.";
        JOptionPane.showMessageDialog(this, helpMessage, "Aturan Sudoku", JOptionPane.INFORMATION_MESSAGE);
    }
    public void developName() {
        String develop = " Group 6:\n" +
                "1. 5026211081 - Muhammad Hudzaifah Abdurrasyid\n" +
                "2. 5026221034 - Dwi Indah Retnanik\n" +
                "3. 5026221200 - Anisa Fatin Idelia";

        // Membuat panel untuk menampung teks dan gambar
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Teks
        JTextArea textArea = new JTextArea(develop);
        textArea.setEditable(false);
        textArea.setBackground(new Color(238, 238, 238));
        panel.add(textArea, BorderLayout.CENTER);

        // Gambar
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("group6.jpg"));// Ganti dengan path gambar Anda
        int width = 150; // Ubah sesuai dengan lebar yang diinginkan
        int height = 150; // Ubah sesuai dengan tinggi yang diinginkan
        Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledIcon);
        panel.add(imageLabel, BorderLayout.PAGE_START);

        // Tampilkan kotak dialog dengan teks dan gambar
        JOptionPane.showMessageDialog(this, panel, "Sudoku", JOptionPane.INFORMATION_MESSAGE);
    }


    //mengecek apabila semua sudah terisi dan jawabannya benar
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    private class CellInputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference to the JTextField that triggers this action event
            Cell sourceCell = (Cell) e.getSource();

            // Check if the cell is editable
            if (!sourceCell.isEditable()) {
                // If the cell is not editable, do nothing
                return;
            }

            // Attempt to parse the input as an integer
            int numberIn;
            try {
                numberIn = Integer.parseInt(sourceCell.getText());
            } catch (NumberFormatException ex) {
                // Handle the case when the input is not a number
                // For example, you may show an error message
                System.out.println("Invalid input. Please enter a number.");
                return;
            }

            // For debugging purposes
            System.out.println("You entered " + numberIn);

            // Check if the entered number is correct
            if (numberIn == sourceCell.number) {
                sourceCell.status = CellStatus.CORRECT_GUESS;
            } else {
                sourceCell.status = CellStatus.WRONG_GUESS;
            }

            // Re-paint the cell based on its status
            sourceCell.paint();

            // Check if the puzzle is solved
            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! Puzzle Solved!");
                newGame();
            }
        }
    }
}
