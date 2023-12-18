import java.awt.*;
import java.awt.event.*;
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


public class Main extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the game board
    public static int ROWS = 5;  // ROWS x COLS cells
    public static int COLS = 5;

    // Define named constants for the drawing graphics
    public static int CELL_SIZE = 120; // cell width/height (square)
    public static final int GRID_WIDTH = 10;                  // Grid-line's width
    public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2;
    public static final int GAMEPANEL_SIZE= 600;
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 5;
//    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_GRID   = Color.BLACK;  // grid lines
    public static final Color COLOR_CROSS  = new Color(211, 45, 65);  // Red #D32D41
    public static final Color COLOR_NOUGHT = new Color(0, 255, 0); // Blue #4CB5F5
    public static final Font FONT_STATUS = new Font("Poppins", Font.BOLD, 14);

    // This enum (inner class) contains the various states of the game
    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }
    private State currentState;  // the current game state

    // This enum (inner class) is used for:
    // 1. Player: CROSS, NOUGHT
    // 2. Cell's content: CROSS, NOUGHT and NO_SEED
    public enum Seed {
        CROSS, NOUGHT, NO_SEED
    }
    private Seed currentPlayer; // the current player
    private Seed[][] board;     // Game board of ROWS-by-COLS cells

    // UI Components
    private GamePanel gamePanel; // Drawing canvas (JPanel) for the game board
    private JLabel statusBar;  // Status Bar
    private  JButton btnLevel;
    private JButton btnRestart;

    /** Constructor to setup the game and the GUI components */
    public Main() {
        // Initialize the game objects
        initGame();

        // Set up GUI components
        gamePanel = new GamePanel();  // Construct a drawing canvas (a JPanel)
        gamePanel.setPreferredSize(new Dimension(GAMEPANEL_SIZE, GAMEPANEL_SIZE));

        // The canvas (JPanel) fires a MouseEvent upon mouse-click
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0
                            && col < COLS && board[row][col] == Seed.NO_SEED) {
                        // Update board[][] and return the new game state after the move
                        currentState = stepGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {       // game over
                    newGame(); // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        // Create a panel to hold the restart button and label
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // You can adjust the layout as needed

        btnRestart = new JButton();
        // Set properties for btnRestart
        btnRestart.setBackground(new Color(71, 210, 52));
        btnRestart.setFont(new Font("Gill Sans Ultra Bold Condensed", 0, 14));
        btnRestart.setForeground(new Color(255, 255, 255)); //warna text
        btnRestart.setText("RESTART");
        btnRestart.setPreferredSize(new Dimension(90, 30));
        btnRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Confirm if you want to restart the game", "Tic Tac Toe", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    newGame();
                    gamePanel.repaint(); // Repaint the game panel to display the new game
                }
            }
        });
        // Add the restart button and label to the panel
        buttonPanel.add(btnRestart);

        btnLevel = new JButton();
        // Set properties for btnRestart
        btnLevel.setBackground(new Color(255, 255, 255));
        btnLevel.setFont(new Font("Gill Sans Ultra Bold Condensed", 0, 14));
        btnLevel.setForeground(new Color(71, 210, 52));
        btnLevel.setText("SELECT GRID");
        btnLevel.setPreferredSize(new Dimension(120, 30));
        btnLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chooseLevel();
                gamePanel.revalidate(); //memperbarui ulang tata letak (layout)
                newGame(); // Atur ulang permainan dengan papan yang baru
                gamePanel.repaint(); // Perbarui panel permainan dengan papan yang baru
            }
        });

        // Add the restart button and label to the panel
        buttonPanel.add(btnLevel);

        // Set up content pane
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Create a JPanel to hold the gamePanel and status bar, and use GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        cp.add(centerPanel, BorderLayout.CENTER);

        // Add gamePanel to centerPanel
        centerPanel.add(gamePanel);

        // Add status bar to centerPanel
        cp.add(statusBar, BorderLayout.PAGE_START);

        // Add buttonPanel to cp
        cp.add(buttonPanel, BorderLayout.PAGE_END);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();  // pack all the components in this JFrame
        setTitle("Tic Tac Toe-Developed By Group 6");
        setVisible(true);  // show this JFrame

        newGame();

    }

    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS]; // allocate array
    }

    public static void chooseLevel(){
        Object[] options = {"3x3", "5x5"};
        int choice = JOptionPane.showOptionDialog(null, "Choose board size:", "Board Size",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]
        );
        if (choice == 0) {
            ROWS = 3;
            COLS = 3;
        } else {
            ROWS = 5;
            COLS = 5;
        }
    }

    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        // Jika ROWS awalnya bukan 5, set ROWS menjadi 3
        ROWS = (ROWS == 5) ? 5 : 3;
        COLS = (COLS == 5) ? 5 : 3;

        board = new Seed[ROWS][COLS]; // Atur ulang array sesuai dengan ukuran yang baru

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState = State.PLAYING; // ready to play
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        board[selectedRow][selectedCol] = player;

        // Compute and return the new game state
        if (COLS == 3 && ROWS == 3) {
            if (board[selectedRow][0] == player  // 3-in-the-row
                    && board[selectedRow][1] == player
                    && board[selectedRow][2] == player
                    || board[0][selectedCol] == player // 3-in-the-column
                    && board[1][selectedCol] == player
                    && board[2][selectedCol] == player
                    || selectedRow == selectedCol  // 3-in-the-diagonal
                    && board[0][0] == player
                    && board[1][1] == player
                    && board[2][2] == player
                    || selectedRow + selectedCol == 2 // 3-in-the-opposite-diagonal
                    && board[0][2] == player
                    && board[1][1] == player
                    && board[2][0] == player) {
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            } else {
                // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
                for (int row = 0; row < ROWS; ++row) {
                    for (int col = 0; col < COLS; ++col) {
                        if (board[row][col] == Seed.NO_SEED) {
                            return State.PLAYING; // still have empty cells
                        }
                    }
                }
                return State.DRAW; // no empty cell, it's a draw
            }
        } else {
            if (board[selectedRow][0] == player  // 5-in-the-row
                    && board[selectedRow][1] == player
                    && board[selectedRow][2] == player
                    && board[selectedRow][3] == player
                    && board[selectedRow][4] == player
                    || board[0][selectedCol] == player // 5-in-the-column
                    && board[1][selectedCol] == player
                    && board[2][selectedCol] == player
                    && board[3][selectedCol] == player
                    && board[4][selectedCol] == player
                    || selectedRow == selectedCol  // 5-in-the-diagonal
                    && board[0][0] == player
                    && board[1][1] == player
                    && board[2][2] == player
                    && board[3][3] == player
                    && board[4][4] == player
                    || selectedRow + selectedCol == 4 // 5-in-the-opposite-diagonal
                    && board[0][4] == player
                    && board[1][3] == player
                    && board[2][2] == player
                    && board[3][1] == player
                    && board[4][0] == player) {
                return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            } else {
                // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
                for (int row = 0; row < ROWS; ++row) {
                    for (int col = 0; col < COLS; ++col) {
                        if (board[row][col] == Seed.NO_SEED) {
                            return State.PLAYING; // still have empty cells
                        }
                    }
                }
                return State.DRAW; // no empty cell, it's a draw
            }
        }
    }
    /**
     *  Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L; // to prevent serializable warning

        @Override
        public void paintComponent(Graphics g) {  // Callback via repaint()
            super.paintComponent(g);

            // Load background image
            ImageIcon bgImageIcon = new ImageIcon(getClass().getResource("BG.jpg"));
            Image bgImage = bgImageIcon.getImage();

            // Draw background image
            g.drawImage(bgImage, 0, 0, CELL_SIZE*5, CELL_SIZE*5, this);

            // Draw the grid lines
            g.setColor(COLOR_GRID);
            CELL_SIZE = Math.min(gamePanel.getWidth() / COLS, gamePanel.getHeight() / ROWS);
            for (int row = 1; row < ROWS; ++row) {
                int y = row * CELL_SIZE - GRID_WIDHT_HALF;
                g.fillRect(0, y, GAMEPANEL_SIZE, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                int x = col * CELL_SIZE - GRID_WIDHT_HALF;
                g.fillRect(x, 0, GRID_WIDTH, GAMEPANEL_SIZE);
            }

            // Draw the Seeds of all the cells if they are not empty
            // Use Graphics2D which allows us to set the pen's stroke
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.CROSS) {  // draw a 2-line cross
                        g2d.setColor(COLOR_CROSS);
                        int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);
                    } else if (board[row][col] == Seed.NOUGHT) {  // draw a circle
                        g2d.setColor(COLOR_NOUGHT);
                        g2d.drawOval(x1, y1, CELL_SIZE - CELL_PADDING * 2, CELL_SIZE - CELL_PADDING * 2);
                    }
                }
            }

            // Print status message
            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.RED);
                statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
            } else if (currentState == State.DRAW) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "It's a Draw!", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    newGame();
                    gamePanel.repaint(); // Repaint the game panel to display the new game
                });
            } else if (currentState == State.CROSS_WON) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "'X' Won!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    newGame();
                    gamePanel.repaint(); // Repaint the game panel to display the new game
                });
            } else if (currentState == State.NOUGHT_WON) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "'O' Won!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    newGame();
                    gamePanel.repaint(); // Repaint the game panel to display the new game
                });
            }
        }
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Welcome! click OK to start game!", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                chooseLevel();
                new Main(); // Let the constructor do the job
            }
        });
    }
}
