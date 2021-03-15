package chessgame;

//tha mou dinei stoixeia gia tin thesi
public class Spot {
    private int x;
    private int y;
    private int pos;
    private int colour; //1 for white AND  0 for black, mporei na voithisi meta stin apeikonisi
    private boolean exists; //is there any piece?
    private Piece type;

    public static final int Black = 0;
    public static final int White = 1;

    public Spot(int x, int y){
    	this.x = x;
    	this.y = y;
        pos = x*8 + y;
    	this.exists = false;

        if(x % 2 == 0){ //mallon tha xreiastei gia tin apeikonisi
            colour = y % 2;
        }
        else{
            colour = (y+1) % 2;
        }
    }

    public Spot(int x, int y, Piece kommati){
    	this.x = x;
        this.y = y;
        pos = x*8 + y;
    	exists = true;
    	type = kommati;

        if(x % 2 == 0){ //mallon tha xreiastei gia tin apeikonisi
            colour = y % 2;
        }
        else{
            colour = (y+1) % 2;
        }
    }

    public Piece  removePiece(){
    	Piece returnValue;

        exists = false;
    	returnValue = type;
        type.setPosition(null);
        type = null;

        return returnValue;

    }

    public void addPiece(Piece kommati){
    	exists = true;
    	type = kommati;
        type.setPosition(this);
    }

    public boolean hasPiece(){
        return exists;
    }

    public Piece whatPiece(){
        return type;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getPos(){
        return pos;
    }

    public int getColour(){
        return colour;
    }


}
