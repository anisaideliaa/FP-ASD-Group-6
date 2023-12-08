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
    JButton btnHelp = new JButton("Help");

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk responsivitas


        // Panel 1: Board Sudoku
        GridBagConstraints gbcBoard = new GridBagConstraints();
        gbcBoard.gridx = 0;
        gbcBoard.gridy = 0;
        gbcBoard.gridwidth = 3;
        gbcBoard.insets = new Insets(10, 10, 10, 10); // Margin
        cp.add(board, gbcBoard);

        // Panel 2: Button2
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcBPanel = new GridBagConstraints();
        gbcBPanel.gridx = 0;
        gbcBPanel.gridy = 1;
        gbcBPanel.gridwidth = 3;
        gbcBPanel.insets = new Insets(10, 10, 10, 10);
        cp.add(bPanel, gbcBPanel);

        // GridBagConstraints untuk tombol New Game
        GridBagConstraints gbcBtnNewGame = new GridBagConstraints();
        gbcBtnNewGame.gridx = 0;
        gbcBtnNewGame.gridy = 0;
        gbcBtnNewGame.insets = new Insets(0, 0, 0, 5);
        bPanel.add(btnNewGame, gbcBtnNewGame);


        // GridBagConstraints untuk tombol Help
        GridBagConstraints gbcHelp = new GridBagConstraints();
        gbcHelp.gridx = 1;
        gbcHelp.gridy = 0;
        gbcHelp.insets = new Insets(0, 0, 0, 5);
        bPanel.add(btnHelp, gbcHelp);

        btnNewGame.setFont(new Font("Cooper Black", Font.PLAIN, 16));
        btnNewGame.setForeground(Color.WHITE);
        btnNewGame.setBackground(new Color(71, 159, 205));

        btnHelp.setFont(new Font("Cooper Black", Font.PLAIN, 15));
        btnHelp.setForeground(new Color(71, 159, 205));
        btnHelp.setBackground(Color.WHITE);

        btnNewGame.addActionListener(e -> { //ini fungsinya ketika tombol new game di click, maka akan nge-restart
            board.newGame();
        });

        btnHelp.addActionListener(e -> { //ini fungsinya ketika tombol new game di click, maka akan nge-restart
            board.newHelp();
        });

        // Initialize the game board to start the game
        board.newGame();

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        //newGames
        JMenu newGameItem = new JMenu("Developed By Group 6");
        menuBar.add(newGameItem);

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    public static void main(String[] args) {
        // [TODO 1] Check "Swing program template" on how to run
        //  the constructor of "SudokuMain"
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Welcome! click OK to start game!","Sudoku", JOptionPane.INFORMATION_MESSAGE);
            new SudokuMain();
        });
    }
}
