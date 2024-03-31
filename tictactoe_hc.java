import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 650; 

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    String playerHuman = "X";
    String playerComputer = "O";
    String currentPlayer = playerHuman;

    boolean gameOver = false;
    int turns = 0;

    TicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 60));
                tile.setFocusable(false);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource();
                        if (tile.getText().isEmpty()) {
                            tile.setText(playerHuman);
                            turns++;
                            checkWinner();
                            if (!gameOver) {
                                currentPlayer = playerComputer;
                                textLabel.setText(currentPlayer + "'s turn.");
                                computerMove();
                            }
                        }
                    }
                });
            }
        }
    }

    void checkWinner() {
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().isEmpty()) continue;
            if (board[r][0].getText().equals(board[r][1].getText()) &&
                    board[r][1].getText().equals(board[r][2].getText())) {
                setWinner(board[r][0], board[r][1], board[r][2]);
                return;
            }
        }
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().isEmpty()) continue;
            if (board[0][c].getText().equals(board[1][c].getText()) &&
                    board[1][c].getText().equals(board[2][c].getText())) {
                setWinner(board[0][c], board[1][c], board[2][c]);
                return;
            }
        }
        if (!board[0][0].getText().isEmpty() &&
                board[0][0].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][2].getText())) {
            setWinner(board[0][0], board[1][1], board[2][2]);
            return;
        }

        if (!board[0][2].getText().isEmpty() &&
                board[0][2].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][0].getText())) {
            setWinner(board[0][2], board[1][1], board[2][0]);
            return;
        }

        if (turns == 9) {
            setTie();
        }
    }

    void setWinner(JButton... tiles) {
        for (JButton tile : tiles) {
            tile.setForeground(Color.green);
        }
        if (currentPlayer.equals(playerHuman)) {
            textLabel.setText("You win!");
        } else {
            textLabel.setText("Computer wins!");
        }
        gameOver = true;
    }

    void setTie() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setBackground(Color.orange);
            }
        }
        textLabel.setText("Tie!");
        gameOver = true;
    }

    void computerMove() {
        int[] move = findBestMove();
        if (move != null) {
            JButton tile = board[move[0]][move[1]];
            tile.setText(playerComputer);
            turns++;
            checkWinner();
            if (!gameOver) {
                currentPlayer = playerHuman;
                textLabel.setText(currentPlayer + "'s turn.");
            }
        }
    }

    int evaluate() {
        for (int r = 0; r < 3; r++) {
            if (!board[r][0].getText().isEmpty() &&
                    board[r][0].getText().equals(board[r][1].getText()) &&
                    board[r][0].getText().equals(board[r][2].getText())) {
                if (board[r][0].getText().equals(playerComputer)) return 10;
                else if (board[r][0].getText().equals(playerHuman)) return -10;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (!board[0][c].getText().isEmpty() &&
                    board[0][c].getText().equals(board[1][c].getText()) &&
                    board[0][c].getText().equals(board[2][c].getText())) {
                if (board[0][c].getText().equals(playerComputer)) return 10;
                else if (board[0][c].getText().equals(playerHuman)) return -10;
            }
        }

        if (!board[0][0].getText().isEmpty() &&
                board[0][0].getText().equals(board[1][1].getText()) &&
                board[0][0].getText().equals(board[2][2].getText())) {
            if (board[0][0].getText().equals(playerComputer)) return 10;
            else if (board[0][0].getText().equals(playerHuman)) return -10;
        }

        if (!board[0][2].getText().isEmpty() &&
                board[0][2].getText().equals(board[1][1].getText()) &&
                board[0][2].getText().equals(board[2][0].getText())) {
            if (board[0][2].getText().equals(playerComputer)) return 10;
            else if (board[0][2].getText().equals(playerHuman)) return -10;
        }

        return 0;
    }

    boolean isMovesLeft() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().isEmpty()) return true;
            }
        }
        return false;
    }

    int minimax(int depth, boolean isMax) {
        int score = evaluate();

        if (score == 10 || score == -10) return score;

        if (!isMovesLeft()) return 0;

        if (isMax) {
            int best = -1000;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().isEmpty()) {
                        board[r][c].setText(playerComputer);
                        best = Math.max(best, minimax(depth + 1, !isMax));
                        board[r][c].setText("");
                    }
                }
            }
            return best;
        } else {
            int best = 1000;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().isEmpty()) {
                        board[r][c].setText(playerHuman);
                        best = Math.min(best, minimax(depth + 1, !isMax));
                        board[r][c].setText("");
                    }
                }
            }
            return best;
        }
    }

    int[] findBestMove() {
        int bestVal = -1000;
        int[] bestMove = new int[] {-1, -1};

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().isEmpty()) {
                    board[r][c].setText(playerComputer);
                    int moveVal = minimax(0, false);
                    board[r][c].setText("");
                    if (moveVal > bestVal) {
                        bestMove[0] = r;
                        bestMove[1] = c;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove[0] != -1 ? bestMove : null;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
