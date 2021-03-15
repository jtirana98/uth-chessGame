package chessgame;


public class Piece {
    private int colour; //0: for black, 1: for white
    private boolean isAlive;
    private Spot position;
    private int type;

    public static final int Black = 0;
    public static final int White = 1;
    
    public static final int ROOKS = 0;
    public static final int KNIGHT = 1;
    public static final int BISHOPS = 2;
    public static final int QUEEN = 3;
    public static final int KING = 4;
    public static final int PAWNS = 5;
  

    public Piece(int team, Spot position, int figure){
    	colour = team;
    	isAlive = true;
    	this.position = position;
    	type = figure;
    }


    public int  getTeam(){
    	return colour;
    }

    public int getType(){
    	return type;
    }

    public boolean isAlive(){
    	return (isAlive);
    }

    public Spot getPosition(){
    	if(isAlive)
    		return position;
    	else
    		return null;
    }

    public void setPosition(Spot pos){
    	position = pos;
    }

    public void changeType(int i){
    	type = i;
    }

    public void kill(){
    	isAlive = false;
    	position = null;
    }

    public void reincarnation(Spot pos){
    	isAlive = true;
    	position = pos;

    	pos.addPiece(this);
    }

    @Override
    public String toString(){
    	StringBuffer str = new StringBuffer();

    	if(colour == Piece.Black)
    		str.append("B");
    	else
    		str.append("W");
    	

    	return (str.append(type)).toString();
    }

}
