package connect4;

/**
 * randBot.java created by eberg on Apr 3, 2014 at 1:18:45 PM
 */
public class randBot {
    
    
    
    public byte getMove() {
        byte returnMe;
        returnMe = randByte();
        return returnMe;
    }

    byte randByte() {
        byte b = (byte) (Math.random() * Board.xDimen);
        return b;
    }
    
    public String toString() {
        String returnMe = "I am a randBot I make a random number";

        return returnMe;
    }
}
