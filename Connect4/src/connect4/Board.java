package connect4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import static java.lang.Thread.sleep;

/**
 * Board.java created by eberg on Apr 3, 2014 at 1:15:54 PM
 */
class Board extends Thread implements Cloneable {
    
    public static final byte xDimen = 7;
    public static final byte yDimen = 6;
    public static final byte rowToWin = 4;
    public static final char RED = 'r';
    public static final char BLACK = 'b';
    public static final char EMPTY = ' ';
    private char whoseMove = RED;
    private int panelWidth;
    private int panelHeight;
    private boolean gameOver = false;
    private char[][] board = new char[xDimen][yDimen];
    private static final Color spaces = new Color(235, 235, 235);
    private static final Color redPiece = new Color(233, 66, 66);
    private static final Color blackPiece = new Color(32, 32, 32);
    private static final Color boardColor = new Color(35, 128, 235);
    private static final Color backgroundColor = new Color(153, 153, 153);
    private static final Font playerFont = new Font("Arial", Font.PLAIN, 24);
    private byte move;
    RoteLearner learner = new RoteLearner();
    private static boolean training = false;
    // remove the declaration new Jafar() and see what happens
    Japanel thePanel;
    Jafar theFrame;
    
    public Board() {
        for (int col = 0; col < xDimen; col++) {
            for (int row = 0; row < yDimen; row++) {
                board[col][row] = EMPTY;
            }
        }
    }
    
    public void setTheFrame(Jafar f) {
        theFrame = f;
    }
    
    public void setThePanel(Japanel p) {
        thePanel = p;
    }
    
    public void run() {
        //System.out.println(theLearner.lossTable.toString());
        if (!training==true) {
            delay(1000);
        }
        
        while (!isGameOver()) {
            if (whoseMove == BLACK) {
                byte botMove = learnerMove();
                playMove(botMove);
            } else if (training == true) {
                byte botMove = botMove();
                playMove(botMove);
            } else 
            { 
                delay(100);
            }
                 // hang around and wait for the human to play...
        }
    }
    
    private void delay(int i) {
        try {
            sleep(i);
        } catch (Exception e) {
        }
    }
    
    public byte learnerMove() {
        return learner.playMove(this);
    }
    
    private byte botMove() {
        MoveList list = generateLegalMoves();
        assert (list.size() > 0);  // empty list?  Ack!!

        return list.get(rand((byte) list.size()));
    }
    
    byte rand(byte max) {
        return (byte) (max * Math.random());
    }
    
    void paint(Graphics g, Dimension size) {
        panelWidth = size.width;
        panelHeight = size.height;
        paint(g);
    }
    
    @Override
    public String toString() {
        String toReturn = "Current board state:\n";
        for (byte i = 0; i < xDimen; i++) {
            for (byte j = 0; j < yDimen; j++) {
                toReturn += board[i][j];
            }
            toReturn += "\n";
        }
        toReturn += "Have a nice day!";
        return toReturn;
    }
    
    void paint(Graphics g) {
        paintBackGround(g);
        drawBoard(g);
        drawSpaces(g);
        drawPieces(g);
        drawPlayer(g);
        //drawBanner(g);
    }
    
    private void drawBoard(Graphics g) {
        g.setColor(boardColor);
        g.fillRect(left(), top(), cellWidth() * xDimen, cellHeight() * yDimen);
        g.setColor(Color.black);
    }
    
    private void drawSpaces(Graphics g) {
        for (int col = 0; col < xDimen; col++) {
            for (int row = 0; row < yDimen; row++) {
                drawCircle(g, col, row);
            }
        }
    }
    
    private void drawPieces(Graphics g) {
        for (int col = 0; col < xDimen; col++) {
            for (int row = 0; row < yDimen; row++) {
                if (board[col][row] != EMPTY) {
                    drawCircle(g, col, row);
                }
                
            }
        }
    }
    
    int left() {
        return margin();
    }
    
    int top() {
        return margin();
    }
    
    int right() {
        return left() + cellWidth() * xDimen;
    }
    
    int bottom() {
        return top() + cellHeight() * yDimen;
    }
    
    int cellWidth() {
        return boardWidth() / xDimen;
        //return minDimen()/ xDimen;
    }
    
    int cellHeight() {
        return boardHeight() / yDimen;
        //return minDimen() / yDimen;
    }
    
    int margin() {
        return 50;
    }
    
    int boardWidth() {
        return panelWidth - 2 * margin();
    }
    
    int boardHeight() {
        return panelHeight - 2 * margin();
    }
    
    int minDimen() {
        if (boardHeight() < boardWidth()) {
            return boardHeight();
        } else {
            return boardWidth();
        }
    }
    
    private void drawCircle(Graphics g, int col, int row) {
        Color color;
        if (board[col][row] == EMPTY) {
            color = spaces;
        } else if (board[col][row] == RED) {
            color = redPiece;
        } else {
            color = blackPiece;
        }
        g.setColor(color);
        g.fillOval(left() + col * cellWidth() + 4, top() + row * cellHeight() + 4, cellWidth() - 8, cellHeight() - 8);
    }
    
    boolean checkMove(byte col, byte row) {
        if (checkHor(col, row) || checkVert(col, row) || checkDiag(col, row)) {
            if (whoseMove == RED) {
                learner.onLoss(this);
            }
            if (theFrame != null) {
                theFrame.repaint();
            }
            return true;
        }
        return false;
    }
    
    void released(int x, int y) {
        if (onBoard(x, y)) {
            x = convertToCol(x);
            playMove(x);
        } else {
            System.out.println("illegal move!");
        }
        
    }
    
    private boolean checkHor(int col, int row) {
        
        if (col == 0) {
            if (board[1][row] == whoseMove && board[2][row]
                    == whoseMove && board[3][row] == whoseMove) {
                return true;
            }
            return false;
        } else if (col == xDimen) {
            if (board[xDimen - 1][row] == whoseMove && board[xDimen - 2][row]
                    == whoseMove && board[xDimen - 3][row] == whoseMove) {
                return true;
            }
            return false;
        } else {
            byte count = 1;
            byte leftIndex = (byte) (col - 1);
            byte rightIndex = (byte) (col + 1);
            
            while (leftIndex >= 0) {
                if (board[leftIndex][row] == whoseMove) {
                    count++;
                    leftIndex--;
                } else {
                    break;
                }
            }
            
            while (rightIndex < xDimen) {
                if (board[rightIndex][row] == whoseMove) {
                    count++;
                    rightIndex++;
                } else {
                    break;
                }
            }
            
            if (count >= 4) {
                return true;
            }
        }
        return false;
        
    }
    
    private boolean checkVert(int col, int row) {
        if (row >= yDimen - 3) {
            return false;
        } else if (board[col][row + 1] == whoseMove && board[col][row + 2]
                == whoseMove && board[col][row + 3] == whoseMove) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean checkDiag(int col, int row) {
        byte count = 1;
        byte xIndex = (byte) (col - 1);
        byte yIndex = (byte) (row - 1);

        //Start by measuring up-left/down-right
        while (xIndex >= 0 && yIndex >= 0) {
            if (board[xIndex][yIndex] == whoseMove) {
                count++;
                xIndex--;
                yIndex--;
            } else {
                break;
            }
        }
        
        xIndex = (byte) (col + 1);
        yIndex = (byte) (row + 1);
        while (xIndex < xDimen && yIndex < yDimen) {
            if (board[xIndex][yIndex] == whoseMove) {
                count++;
                xIndex++;
                yIndex++;
            } else {
                break;
            }
        }
        
        if (count >= rowToWin) {
            return true;
        }

        //Now do down-left/up-right
        count = 1;
        xIndex = (byte) (col - 1);
        yIndex = (byte) (row + 1);
        while (xIndex >= 0 && yIndex < yDimen) {
            if (board[xIndex][yIndex] == whoseMove) {
                count++;
                xIndex--;
                yIndex++;
            } else {
                break;
            }
        }
        
        xIndex = (byte) (col + 1);
        yIndex = (byte) (row - 1);
        while (xIndex < xDimen && yIndex >= 0) {
            if (board[xIndex][yIndex] == whoseMove) {
                count++;
                xIndex++;
                yIndex--;
            } else {
                break;
            }
        }
        
        if (count >= rowToWin) {
            return true;
        }
        
        return false;
    }

//    private boolean legalMove(int col, int row) {
//        if (freespace(col, row) && !freespace(col + 1, row)) {
//            return true;
//        }
//
//        return false;
//
//    }
    private boolean onBoard(int x, int y) {
        return x >= left() && x <= right() && y <= bottom() && y >= top();
    }
    
    private boolean freespace(int col, int row) {
        if (row < yDimen) {
            if (board[col][row] == EMPTY) {
                return true;
            }
        }
        return false;
        
    }

//    private void playMove(int x, int y) {
//        int row = convertToRow(y);
//        int col = convertToCol(x);
//        if (legalMove(col, row)) {
//            board[col][row] = whoseMove;
//            if (whoseMove == RED) {
//                whoseMove = BLACK;
//            } else {
//                whoseMove = RED;
//            }
//        }
//    }
    private byte convertToRow(int y) {
        return (byte) ((y - margin()) / cellHeight());
    }
    
    private byte convertToCol(int x) {
        return (byte) ((x - margin()) / cellWidth());
    }
    /////////////////////////////////////////////////////////////////////////////

    private void playMove(int x) {
        //byte col = convertToCol(x);
        byte col = (byte) x;
        byte moveRow;
        if (legalMove(col)) {
            moveRow = getRow(col);
            board[col][moveRow] = whoseMove;
            if (checkMove(col, moveRow)) {
                gameOver = true;
                System.out.println("game is over");
                return;
            }
            if (whoseMove == RED) {
                whoseMove = BLACK;
            } else {
                whoseMove = RED;
            }
        }
        if (theFrame != null) {
            theFrame.repaint();
        }
        //System.out.println("works!" + this.toString());

    }
    
    private boolean legalMove(int col) {
        for (byte row = yDimen - 1; row >= 0; row--) {
            if (freespace(col, row)) {
                return true;
            }
        }
        return false;
    }
    
    private byte getRow(byte col) {
        byte returnMe = 0;
        for (byte row = yDimen - 1; row >= 0; row--) {
            if (freespace(col, row)) {
                return row;
            }
        }
        return returnMe;
    }
    
    public boolean isGameOver() {
        if (gameOver) {
            if (training) {
                theFrame.reset();
            }
            return true;
        }
        if (!spaceLeft()) {
            return true;
        }
        return gameOver;
    }
    
   
    
    private boolean spaceLeft() {
        for (int i = 0; i < xDimen; i++) {
            for (int j = 0; j < yDimen; j++) {
                if (board[i][j] == EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void drawPlayer(Graphics g) {
        g.setFont(playerFont);
        if (!isGameOver()) {
            if (whoseMove == RED) {
                g.setColor(redPiece);
                g.drawString("RED's turn", margin(), 2 * margin() / 3);
            } else {
                g.setColor(blackPiece);
                g.drawString("BLACK's turn", margin(), 2 * margin() / 3);
            }
        } else if (isGameOver() && !spaceLeft()) {
            g.setColor(spaces);
            g.drawString("Game is a draw!", margin(), 2 * margin() / 3);
        } else {
            g.drawString("Game Over!", margin(), 2 * margin() / 3);
        }
        
    }
    
    private void paintBackGround(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, panelWidth, panelHeight);
    }
    
    public BoardList generateLegalNextBoards(char player) {
        BoardList m = new BoardList();
        MoveList mList = generateLegalMoves();
        
        for (byte b : mList) {
            Board temp = newBoard(b, player);
            temp.playMove(b);
            m.add(temp);
        }
        
        return m;
    }
    
    byte getMove() {
        return move;
    }
    
    public void setMove(byte move) {
        this.move = move;
    }
    
    private MoveList generateLegalMoves() {
        MoveList returnMe = new MoveList();
        for (byte move = 0; move < xDimen; move++) {
            if (legalMove(move)) {
                returnMe.add(move);
            }
        }
        return returnMe;
    }
    
    public Board newBoard(byte move, char player) {
        Board newBoard = new Board();
        newBoard.setMove(move);
        newBoard.setPlayer(player);
        for (int col = 0; col < xDimen; col++) {
            for (int row = 0; row < yDimen; row++) {
                newBoard.board[col][row] = this.board[col][row];
            }
        }
        return newBoard;
    }
    
    private void setPlayer(char player) {
        whoseMove = player;
    }
    
    char getPlayer() {
        return whoseMove;
    }
    
    void train() {
        if (training == false) {
            training = true;
        } else {
            training = false;
        }
        
    }
}
