/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package connect4;

import java.util.HashMap;

/**
 *
 * @author ethan
 */
public class RoteLearner {
//    
//    Board currentBoard;
//    BoardTree Tree;
    
    public static HashMap lossTable = new HashMap<String, Board>();
    
    
    
    public RoteLearner() {
//        currentBoard = start;
//        Tree = new BoardTree(start);
    }
//    
//    public void buildTree(Board b) {
//        Tree.getRoot().buildTree(b);
//    }
    
    public void onLoss(Board loss) {
        lossTable.put(loss.toString(), null);
    }
    
    public byte playMove(Board currentBoard) {
        BoardList list = currentBoard.generateLegalNextBoards(currentBoard.getPlayer());
        byte move = 0;
        for (Board m : list) {
            if (checkChildren(m)) {
                return m.getMove();
            }
            System.out.println("M is" + m.getMove());
             move = m.getMove();
        }
        System.out.println("learning current board");
        onLoss(currentBoard);
        
        return move;
    }

    private boolean checkChildren(Board currentBoard) { 
        BoardList list = currentBoard.generateLegalNextBoards(currentBoard.getPlayer());
        //System.out.println("****************************");
        //System.out.println(currentBoard.toString());
        for (Board m : list) {
            //System.out.println(m.toString());
            if(lossTable.containsKey(m.toString())) {
                return false;
            }
        }
        System.out.println("Returned True");
        return true;
    }
    
}
