package sudoku;
import java.awt.*;
import javax.swing.*;

/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2023/2024
 * Group Capstone Project
 * Group #6
 * 1 - 5026211081 - Muhammad Hudzaifah Abdurrasyid
 * 2 - 5026221034 - Dwi Indah Retnanik
 * 3 - 5026221200 - Anisa Fatin Idelia
 */

/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Add a button to the south to re-start the game via board.newGame()
        // Use GridBagLayout for the south panel
        JPanel southPanel = new JPanel(new GridBagLayout());

        // Set GridBagConstraints to place the button on the right
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        btnNewGame.setPreferredSize(new Dimension(150, 50));
        Font buttonFont = new Font("Cooper Black", Font.PLAIN, 16);
        btnNewGame.setFont(buttonFont);

        southPanel.add(btnNewGame, gbc);

        btnNewGame.addActionListener(e -> {
            board.newGame();
        });

        cp.add(southPanel, BorderLayout.EAST);

        // Initialize the game board to start the game
        board.newGame();

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    /**
     * The entry main() entry method
     */
    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        SwingUtilities.invokeLater(() -> {
            new SudokuMain();
        });
    }
}
