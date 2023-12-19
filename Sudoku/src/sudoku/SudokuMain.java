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
    JButton btnHelp = new JButton("Rules");
    private JLabel statusBar;

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        statusBar = new JLabel("       ");
        statusBar.setFont(new Font("Poppins", Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(216, 216, 216));
        cp.add(statusBar, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        cp.add(centerPanel, BorderLayout.CENTER);
        GridBagConstraints gbcBoard = new GridBagConstraints();
        gbcBoard.gridx = 0;
        gbcBoard.gridy = 0;
        gbcBoard.insets = new Insets(10, 10, 10, 10); // Margin
        centerPanel.add(board, gbcBoard);

        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        GridBagConstraints gbcBPanel = new GridBagConstraints();
        gbcBPanel.gridx = 0;
        gbcBPanel.gridy = 1; // Placing bPanel below the board
        gbcBPanel.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(bPanel, gbcBPanel);
        bPanel.add(btnNewGame);
        bPanel.add(btnHelp);

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
        JMenu newGameItem = new JMenu("Get to know about Group 6");
        menuBar.add(newGameItem);

        JMenuItem menuItem = new JMenuItem("Open for details"); // Ganti "Action" dengan teks yang sesuai
        menuItem.addActionListener(e -> {
            board.developName(); // Panggil metode yang diinginkan saat item menu diklik
        });
        newGameItem.add(menuItem); //


        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku - Developed By Group 6");
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

    public void setStatusText(String text) {
        statusBar.setText(text);
    }

}
