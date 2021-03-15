package chessgame;

import static java.lang.Math.abs;
import java.util.*;

class Mytype{
    public int score;
    public int Bscore;
    public int Wscore;
    public String move;

    public Mytype(int score, String move){
        this.score = score;
        this.move = new String(move);
    }

    public Mytype(int Bscore, int Wscore){
        this.Bscore = Bscore;
        this.Wscore = Wscore;
    }
}

public class TheBoard {
    //exei enan pinaka 8 * 8 gia tis theseis
    private Piece [] teamWhite;
    private Piece [] teamBlack;

    private Spot [] board;

    public static final int SUMofSpots = 64;
    public static final int NumOfPieces = 16;
    public static final int Black = 0;
    public static final int White = 1;
    public static final int MinValue = -100;
    public static final int MaxValue = 100;
    public static final int maxDepth = 2;

   	private int Bking = 4;
   	private int Wking = 12;
   	private int BKnight1 = 1;
   	private int BKnight2 = 6;
   	private int WKnight1 = 9;
   	private int WKnight2 = 14;

   	private int deiktis;
   	private int [] vimata;


    //when the game starts we initialize the pieces to the default spots(starting point)
    public TheBoard(){
    	board = new Spot[TheBoard.SUMofSpots];
    	teamWhite = new Piece[TheBoard.NumOfPieces];
    	teamBlack = new Piece[TheBoard.NumOfPieces];
    	

    	/***************************DOKIMES****************************/
    	deiktis = -1;
    	vimata = new int[]{8, 7, 3, 9, 9, 8, 2, 4, 1, 9, 8, 9, 9, 9, 3, 4};


    	/***************************DOKIMES****************************/
    	int w = 0;
    	int b = 0;
    	for(int i = 0; i < TheBoard.SUMofSpots; i++){
    		board[i] = new Spot(i/8, i%8);

    		//place the piece
    		
    		if(i/8 == 6){//white pawns
    			teamWhite[w] = new Piece(TheBoard.White, board[i], Piece.PAWNS);
    			board[i].addPiece(teamWhite[w]);
    			w++;
    		}

    		if(i/8 == 1){//black pawns
    			teamBlack[b] = new Piece(TheBoard.Black, board[i], Piece.PAWNS);
    			board[i].addPiece(teamBlack[b]);
    			b++;
    		}

    		if(i/8 == 7){ //white piecess
    			if(i%8 < 5){
    				teamWhite[w] = new Piece(TheBoard.White, board[i], i%8);
    				board[i].addPiece(teamWhite[w]);
    				w++;
    			}
    			else{
    				teamWhite[w] = new Piece(TheBoard.White, board[i], 7 - i%8);
    				board[i].addPiece(teamWhite[w]);
    				w++;
    			}
    		}

    		if(i/8 == 0){ //black pieces
    			if(i%8 < 5){
    				teamBlack[b] = new Piece(TheBoard.Black, board[i], i%8);
    				board[i].addPiece(teamBlack[b]);
    				b++;
    			}
    			else{
    				teamBlack[b] = new Piece(TheBoard.Black, board[i], 7 - i%8);
    				board[i].addPiece(teamBlack[b]);
    				b++;
    			}

    		}

    	}
    }

    //checks if white players have lost 
    public boolean check(){
    	//format x_sy_sx_d_y_d-KILLED/*PromotionTo
    	LinkedList<String> moves = new LinkedList<>();
    	Piece [] tmp;
		tmp = teamWhite;
    	for(int i = 0; i < NumOfPieces; i++){
    		if(tmp[i].isAlive()){
    			switch (tmp[i].getType()){
    				case Piece.ROOKS: moves.addAll(RookPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;
    				case Piece.KNIGHT: moves.addAll(KnightPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;
    				case Piece.BISHOPS: moves.addAll(BishopPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;
    				case Piece.QUEEN: moves.addAll(QueenPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;
    				case Piece.KING: moves.addAll(KingPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;
    				case Piece.PAWNS: moves.addAll(PawnPossibleMoves(i, White));
    						//System.out.println(moves);
    						if(!(moves.isEmpty()))
    							return false;
    						break;

    			}
    		}
    	}
    	System.out.println(moves);
    	return true;
    }


    public int PCmove(){
    	System.out.println("------------------------------------------------------------------------------------");
    	Mytype result = CalculateNextMove(maxDepth, "", Black, MinValue, MaxValue);
    	
    	System.out.println("MOVE FOUND: " + result.score + " - " + result.move);
    	
    	if(result.move.equals("LOST")){
    		return -1;
    	}
    	if(result.move.equals("TIE")){
    		return -2;
    	}

    	if(result.move.charAt(result.move.length() - 2) == '|')
    			MakeMove(result.move.substring(0, result.move.length() - 2));
    		else
    			MakeMove(result.move);

    	System.out.println(toString());
    	
    	return 1;
    }



    private Mytype CalculateNextMove(int depth, String move, int turn, int Calpha, int Cbeta){
    	LinkedList<String> list = (LinkedList<String>) PossibleMoves(turn);
    	ListIterator<String> it = list.listIterator();
    	int local;
    	String cmove, bmove = move;
    	Mytype result;

    	System.out.println("CALCULATE: d " + depth  + "a: " +  Calpha + "b: " + Cbeta);

    	if(depth == 0 || list.isEmpty()){
    		//NOTE: if empty == roua mat
    		System.out.println("END OF TREE: " + depth);
    		if(depth == maxDepth){
    			
    			if(!KingSafe(Black))//roua mat
    				return(new Mytype(MinValue, "LOST"));
    			else // pat
    				return(new Mytype(MinValue, "TIE"));

    		}
    		return(new Mytype(rating(list.size(), depth), move));
    	}



    	local = MaxValue*turn + MinValue*(1 - turn); //very small if black, very large if white
    	
    	while(it.hasNext()){
    		cmove = it.next();
    		System.out.println("move: " + cmove);
    		
    		if(cmove.charAt(cmove.length() - 2) == '|')
    			MakeMove(cmove.substring(0, cmove.length() - 2));
    		else
    			MakeMove(cmove);

    		System.out.println("starting: d " + depth + "move: " + cmove + "a: " +  Calpha + "b: " + Cbeta);
    		System.out.println(toString());
    		result = CalculateNextMove(depth - 1, cmove, 1 - turn, Calpha, Cbeta);
    		System.out.println("Returned :  " + depth + "score:  " + result.score + " move " + move);
    		if(turn == White){ //min
    			if(local > result.score){ //den vazw to ison gia na meiwsw ta vimata
    				local = result.score;
    				bmove = cmove;
    				if(local < Calpha){ //prunning, otidipote kai an vrei apo dw kai pera o pateras tha to aporipsei
    					UndoMove(cmove);
    					return(new Mytype(local, cmove));
    				}
    				Cbeta = local; //an to paidi m, vrei kati pio megalo apo auto den tha to dextw
    			}
    			UndoMove(cmove);
    		}
    		else{	//max
    			if(local < result.score){
    				local = result.score;
    				bmove = cmove;
    				if(local > Cbeta){ //prunning, this side of the tree
    					UndoMove(cmove);
    					return(new Mytype(local, cmove));
    				}
    				Calpha = local;
    			}
    			UndoMove(cmove);
    		}
    		System.out.println(toString());
    		System.out.println("depth: " + depth + " a: " + Calpha + " b: " + Cbeta); 		
    	}

    	//eftasa mexri to telos tis listas

    	return(new Mytype(local, bmove));
    }
/*
	private int evaluateMove(){
        
        deiktis++;

        if(deiktis == vimata.length)
        	deiktis = 0;

        return(vimata[deiktis]);

		
	}

*/
     public void MakeMove(String move){
    	//format of move str x_sourcey_sourcex_desty_dest*type
    	//*type in case of promotion

    	//NA VALW ELEGXW GIA THN NOMIMOTITA TIS KINISIS??????????????

    	int x_s, y_s, x_d, y_d,  promotion;
    	Piece pioni;

    	x_s = Character.getNumericValue(move.charAt(0));
    	y_s = Character.getNumericValue(move.charAt(2));

    	x_d = Character.getNumericValue(move.charAt(4));
    	y_d = Character.getNumericValue(move.charAt(6));

    	pioni = board[x_s*8 + y_s].removePiece();
    	if(board[x_d*8 + y_d].hasPiece())
    		board[x_d*8 + y_d].whatPiece().kill();
    	
    	board[x_d*8 + y_d].addPiece(pioni);

    	if(move.length() > 7){ //promotion of pawn
    		promotion = Character.getNumericValue(move.charAt(move.length() - 1));
    		pioni.changeType(promotion);
    	}

    }


    private void UndoMove(String move){
    	int x_s, y_s, x_d, y_d, killed=-1;
    	Piece pioni, walkingDead=null;

    	x_s = Character.getNumericValue(move.charAt(0));
    	y_s = Character.getNumericValue(move.charAt(2));

    	x_d = Character.getNumericValue(move.charAt(4));
    	y_d = Character.getNumericValue(move.charAt(6));

    	pioni = board[x_d*8 + y_d].removePiece();
    	board[x_s*8 + y_s].addPiece(pioni);
    	

    	if(move.length() > 7){
    		if(move.charAt(7) == '*'){ //eixame promotion
    			pioni.changeType(Piece.PAWNS);
    			if(move.length() > 9){
    				killed = Character.getNumericValue(move.charAt(move.length() - 1));

    			}
    		}
    		else
    			killed = Character.getNumericValue(move.charAt(move.length() - 1));
    		
    		System.out.println("/////////////////////////////////////// " + killed);
    		if(killed != -1){
    			if(pioni.getTeam() == White){
    				for(int i = 0 ; i < NumOfPieces; i++){
    					if((teamBlack[i].isAlive() == false) && teamBlack[i].getType() == killed){
    						walkingDead = teamBlack[i];
    						break;
    					}
    				}
    			}
    			else{
    				for(int i = 0 ; i < NumOfPieces; i++){
    					if((teamWhite[i].isAlive() == false) && teamWhite[i].getType() == killed){
    						walkingDead = teamWhite[i];
    						break;
    					}
    				}
    			}
    			if(walkingDead != null)
    				walkingDead.reincarnation(board[x_d*8 + y_d]);
    		}
    	}
    }


    public List<String> PossibleMoves(int tmpTurn){  
    	//format x_sy_sx_d_y_d-KILLED/*PromotionTo
    	LinkedList<String> moves = new LinkedList<>();
    	Piece [] tmp;

    	if(tmpTurn == White)
    		tmp = teamWhite;
    	else
    		tmp = teamBlack;

    	for(int i = 0; i < NumOfPieces; i++){
    		if(tmp[i].isAlive()){
    			switch (tmp[i].getType()){
    				case Piece.ROOKS: moves.addAll(RookPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;
    				case Piece.KNIGHT: moves.addAll(KnightPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;
    				case Piece.BISHOPS: moves.addAll(BishopPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;
    				case Piece.QUEEN: moves.addAll(QueenPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;
    				case Piece.KING: moves.addAll(KingPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;
    				case Piece.PAWNS: moves.addAll(PawnPossibleMoves(i, tmpTurn));
    						//System.out.println(moves);
    						break;

    			}
    		}
    	}
    	System.out.println(moves);
    	return moves;

    }


    private List<String> RookPossibleMoves(int pos, int tmpTurn){
       // System.out.println("ROOK: " + "TEAM: " +  tmpTurn + " pos " + pos );
    	Piece pioni;

    	if(tmpTurn == White)
    		pioni = teamWhite[pos];
    	else
    		pioni = teamBlack[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int x_tmp, y_tmp;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();

    	int i,j;
    	boolean x_continue, y_continue;
    	for(int k = -1; k <= 1; k = k + 2){
			i = 1;
    		j = 1;
    		x_continue = true;
    		y_continue = true;
    		while(x_continue || y_continue){
    			if(x_continue){
    				x_tmp = x + i*k;
    				tmp = x_tmp*8 + y;
    				if(x_tmp >= 8 || x_tmp < 0)
    					x_continue = false;
    				else{
    					if(board[tmp].hasPiece()){
		    				if(board[tmp].whatPiece().getTeam() == tmpTurn)
		    					x_continue = false;		
		    			}
		    			if(x_continue){
			    			if(board[tmp].hasPiece()){
			    				oldpioni = board[tmp].whatPiece();
			    				x_continue = false;
			    			}
			    			else
			    				oldpioni = null;

			    			board[thesi].removePiece();
			    			board[tmp].addPiece(pioni);
			    			if(KingSafe(tmpTurn)){
			    				if(oldpioni != null){
			    					move = new String(x + " " + y + " " + x_tmp + " "+ y + "|" + oldpioni.getType());
			    				}
			    				else
			    					move = new String(x + " " + y + " " + x_tmp + " "+ y);
			    				list.add(move);
			    			}
			    			//recover
			    			if (oldpioni != null) {
			    				board[tmp].addPiece(oldpioni);
			    			}
			    			else
	    						board[tmp].removePiece();
			    			board[thesi].addPiece(pioni);
	    				}
	    			}
    			}
    			if(y_continue){
    				y_tmp = y + j*k;
    				tmp = x*8 + y_tmp;
    				if(y_tmp >= 8 || y_tmp < 0)
    					y_continue = false;
    				else{
    					if(board[tmp].hasPiece()){
		    				if(board[tmp].whatPiece().getTeam() == tmpTurn)
		    					y_continue = false;
		    					i++;
		    					continue;		
		    			}

		    			if(board[tmp].hasPiece() && y_continue){
		    				oldpioni = board[tmp].whatPiece();
		    				y_continue = false;
		    			}
		    			else
		    				oldpioni = null;

		    			board[thesi].removePiece();
		    			board[tmp].addPiece(pioni);
		    			if(KingSafe(tmpTurn)){
		    				if(oldpioni != null){
		    					move = new String(x + " " + y + " " + (x) + " "+ (y_tmp) + "|" + oldpioni.getType());
		    				}
		    				else
		    					move = new String(x + " " + y + " " + (x) + " "+ (y_tmp));
		    				list.add(move);
		    			}
			    		//recover
		    			if (oldpioni != null) {
		    				board[tmp].addPiece(oldpioni);
		    			}
		    			else
	    					board[tmp].removePiece();
		    			
		    			board[thesi].addPiece(pioni);
    			}
    		}

    		i++;
    		j++;   		
    	}
    }
    	return list;
    	
    }



    private List<String> KnightPossibleMoves(int pos, int tmpTurn){
    	      //  System.out.println("KNIGHT: " + "TEAM: " +  tmpTurn + " pos " + pos );

		Piece pioni;

    	if(tmpTurn == White)
    		pioni = teamWhite[pos];
    	else
    		pioni = teamBlack[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int colour = board[thesi].getColour();
    	int []  coordinate_temp = new int [2];
    	int [] coordinate = new int [2];
    	int tmp;
    	String move;

    	coordinate[0] = thesi/8;
    	coordinate[1] = thesi%8;

    	LinkedList<String> list = new LinkedList<String>();


    	for(int i = -2; i <=2; i = i + 4){
    		for(int j = -1; j <=1; j++){
    			for(int k = 0; k < 2; k++){

	    			coordinate_temp[k] =  coordinate[k] + i;
	    			if(coordinate_temp[k] >= 8 || coordinate_temp[k] < 0)
	    				continue;
					coordinate_temp[abs(k-1)] = coordinate[abs(k-1)] + j;
					if(coordinate_temp[abs(k-1)] >= 8 ||coordinate_temp[abs(k-1)] < 0)
						continue;
					tmp = coordinate_temp[0]*8 + coordinate_temp[1];
					if(board[tmp].getColour() == colour)
						continue;

					if(board[tmp].hasPiece()){
	    				if(board[tmp].whatPiece().getTeam() == tmpTurn)
	    					continue; //cannot go there
	    				else
	    					oldpioni = board[tmp].whatPiece();
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();	
	    			
	    			if(KingSafe(tmpTurn)){
	    				if(oldpioni != null){
	    					move = new String(coordinate[0] + " " + coordinate[1] + " " + coordinate_temp[0] + " "+ coordinate_temp[1] + "|" + oldpioni.getType());
	    				}
	    				else
	    					move = new String(coordinate[0] + " " + coordinate[1] + " " + coordinate_temp[0] + " "+ coordinate_temp[1]);
	    				list.add(move);
	    			}
	    			//recover
	    			if (oldpioni != null) {
	    				board[tmp].addPiece(oldpioni);
	    			}
	    			else
	    				board[tmp].removePiece();
	    			board[thesi].addPiece(pioni);
				}

    		}

    	}
    	return list;
    }


    private List<String> BishopPossibleMoves(int pos, int tmpTurn){
    	Piece pioni;

    	if(tmpTurn == White)
    		pioni = teamWhite[pos];
    	else
    		pioni = teamBlack[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int x_tmp, y_tmp;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();

    	int vima;
    	boolean forward;
    	for(int i = -1; i <= 1; i = i + 2){
    		for(int j = -1 ; j <= 1; j = j + 2){
    			vima = 1;
    			forward = true;
    			while(forward){
    				x_tmp = x + vima*i;
    				y_tmp = y + vima*j;
    				tmp = x_tmp*8 + y_tmp;
    				if (x_tmp >= 8 || x_tmp < 0 || y_tmp >= 8 || y_tmp < 0 ){
    					break;
    				}
    				if(board[tmp].hasPiece()){
	    				if(board[tmp].whatPiece().getTeam() == tmpTurn)
	    					break; //cannot go there
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[thesi].removePiece();
	    			board[tmp].addPiece(pioni);
	    			if(KingSafe(tmpTurn)){
	    				if(oldpioni != null){
	    					move = new String(x + " " + y + " " + x_tmp + " "+ y_tmp + "|" + oldpioni.getType());
	    				}
	    				else
	    					move = new String(x + " " + y + " " + x_tmp + " "+ y_tmp);
	    				list.add(move);
	    			}
	    			//recover
	    			
	    			if (oldpioni != null) {
	    				board[tmp].addPiece(oldpioni);
	    			}
	    			else
	    				board[tmp].removePiece();
	    			board[thesi].addPiece(pioni);
	    			vima++;		
    			}

    		}
    	}
    	return list;
    }


    private List<String> QueenPossibleMoves(int pos, int tmpTurn){
    	        //System.out.println("QUEEN: " + "TEAM: " +  tmpTurn + " pos " + pos );

    	Piece pioni;

    	if(tmpTurn == White)
    		pioni = teamWhite[pos];
    	else
    		pioni = teamBlack[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int x_tmp, y_tmp;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();

    	int vima;
    	boolean forward;
    	for(int i = -1; i <= 1; i++){
    		for(int j = -1 ; j <= 1; j++){
    			
    			if (i == 0 && j == 0)
    				continue;
    			
    			vima = 1;
    			forward = true;
    			while(forward){
    				x_tmp = x + vima*i;
    				y_tmp = y + vima*j;
    				tmp = x_tmp*8 + y_tmp;
    				if (x_tmp >= 8 || x_tmp < 0 || y_tmp >= 8 || y_tmp < 0 ){
    					forward = false;
    					break;
    				}
    				if(board[tmp].hasPiece()){
	    				if(board[tmp].whatPiece().getTeam() == tmpTurn){
	    					forward = false;
	    					break; //cannot go there
	    				}
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[thesi].removePiece();
	    			board[tmp].addPiece(pioni);
	    			if(KingSafe(tmpTurn)){
	    				if(oldpioni != null){
	    					move = new String(x + " " + y + " " + x_tmp + " "+ y_tmp + "|" + oldpioni.getType());
	    				}
	    				else
	    					move = new String(x + " " + y + " " + x_tmp + " "+ y_tmp);
	    				list.add(move);
	    			}
	    			//recover
	    			
	    			if (oldpioni != null) {
	    				board[tmp].addPiece(oldpioni);
	    			}
	    			else
	    				board[tmp].removePiece();

	    			board[thesi].addPiece(pioni);
	    			vima++;		
    			}

    		}
    	}
    	return list;

    }



    private List<String> KingPossibleMoves(int pos, int tmpTurn){
    	      //  System.out.println("KING: " + "TEAM: " +  tmpTurn + " pos " + pos );

    	Piece pioni;

    	if(tmpTurn == White)
    		pioni = teamWhite[pos];
    	else
    		pioni = teamBlack[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();

    	for(int i  = -1; i < 2; i++){
    		if(x + i >= 8 || x + i < 0)
    			continue; //out of border
    		
    		for(int j = -1; j < 2; j++){
    			if(i == 0 && j == 0)
    				continue; //same position
    			if(y + j >= 8 || y + j < 0)
    				continue; //out of border

    			tmp = (i+x)*8 + (j+y);
    			if(board[tmp].hasPiece()){
    				if(board[tmp].whatPiece().getTeam() == tmpTurn)
    					continue; //cannot go there
    				else
    					oldpioni = board[tmp].whatPiece();
    			}
    			else
    				oldpioni = null;

    			board[thesi].removePiece();
    			board[tmp].addPiece(pioni);
    			if(KingSafe(tmpTurn)){
    				if(oldpioni != null){
    					move = new String(x + " " + y + " " + (x+i) + " "+ (y+j) + "|" + oldpioni.getType());
    				}
    				else
    					move = new String(x + " " + y + " " + (x+i) + " "+ (y+j));
    				list.add(move);
    			}
    			//recover
    			if (oldpioni != null) {
    				board[tmp].addPiece(oldpioni);
    			}
    			else
	    			board[tmp].removePiece();

				board[thesi].addPiece(pioni);
    		}
    	}
    	return list;
    }

    
    public List<String> PawnPossibleMoves(int pos, int tmpTurn){ //pane apo katw pros ta panw ara pron mikrotera
    	        //System.out.println("PAWN: " + "TEAM: " +  tmpTurn + " pos " + pos );

    	Piece pioni;
    	LinkedList<String> list = new LinkedList<String>();

    	if(tmpTurn == Black)
    		return(BlackPawnPossibleMoves(pos, tmpTurn));
    	else
    		pioni = teamWhite[pos];

    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int tmp;
    	String move;
    	//x meiwnetai

    	//go forward
    	tmp = (x-1)*8 + y;
    	if(!board[tmp].hasPiece()){
    		board[thesi].removePiece();
    		board[tmp].addPiece(pioni);	
    		if(x-1 == 0){ //promotion
    			for(int i = Piece.ROOKS; i <= Piece.QUEEN; i++){

    				pioni.changeType(i);
    				if(KingSafe(tmpTurn)){
	    				move = new String(x + " " + y + " " + (x-1) + " "+ y + "*" + i);
	    				list.add(move);
	    			}

    			}
    			board[tmp].removePiece();
    			pioni.setPosition(board[thesi]);
    			pioni.changeType(Piece.PAWNS);
    		}
    		else{ //one forward   			
	    		if(KingSafe(tmpTurn)){
	    			move = new String(x + " " + y + " " + (x-1) + " "+ y);
	    			list.add(move);
	    		}
	    		board[tmp].removePiece();
	    		board[thesi].addPiece(pioni);
	    	}

    		if(pioni.getPosition().getX() == 6 && !board[(x-2)*8 + y].hasPiece()){ //can go two forward
    			tmp = (x-2)*8 + y;
    			board[thesi].removePiece();
    			board[tmp].addPiece(pioni);
    			if(KingSafe(tmpTurn)){
    				move = new String(x + " " + y + " " + (x-2) + " "+ y);
    				list.add(move);
    			}
    			board[tmp].removePiece();
                        board[thesi].addPiece(pioni);
    		}
    		//recovery
    		
    	}

    	//kill move
    	for(int i = -1; i <= 1; i = i + 2){//right kai left
    		if(y + i < 0 || y + i >= 8)
    			continue;

    		tmp = (x-1)*8 + y + i;
    		if(board[tmp].hasPiece()){
    			if (board[tmp].whatPiece().getTeam() == TheBoard.Black) { //KILL IT
    				oldpioni = board[tmp].whatPiece();
    				board[thesi].removePiece();
    				board[tmp].addPiece(pioni);
    				if(x-1 == 0){ //promotion
		    			for(int j = Piece.ROOKS; j <= Piece.QUEEN; j++){

		    				pioni.changeType(j);
		    				if(KingSafe(tmpTurn)){
			    				move = new String(x + " " + y + " " + (x-1) + " "+ (y+i) + "*" + j + "|" + oldpioni.getType());
			    					list.add(move);
			    			}

		    			}

    					pioni.changeType(Piece.PAWNS);
    				}
    				else if(KingSafe(tmpTurn)){
    					move = new String(x + " " + y + " " + (x-1) + " "+ (y+i) + "|" + oldpioni.getType());
    					list.add(move);    				
    				}

    				//recovery
    				board[tmp].addPiece(oldpioni);
    				board[thesi].addPiece(pioni);
    			}
    		}	
    	}

    	//en pasan
    	//TODO
    	return list;
    }

    private List<String> BlackPawnPossibleMoves(int pos, int tmpTurn){ //pane apo panw pros ta katw
       	Piece pioni = teamBlack[pos];
    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();

    	//x auksanetai

    	//go forward
    	tmp = (x+1)*8 + y;
    	if(!board[tmp].hasPiece()){
    		board[thesi].removePiece();
    		board[tmp].addPiece(pioni);
    		if(x+1 == 0){ //promotion
    			for(int i = Piece.ROOKS; i <= Piece.QUEEN; i++){

    				pioni.changeType(i);
    				if(KingSafe(tmpTurn)){
    					//System.out.println("New Move");
	    				move = new String(x + " " + y + " " + (x+1) + " "+ y + "*" + i);
	    				list.add(move);
	    			}

    			}
    			board[tmp].removePiece();
    			pioni.setPosition(board[thesi]);
    			pioni.changeType(Piece.PAWNS);
    		}
    		else{ //one forward   			
	    		if(KingSafe(tmpTurn)){
	    			//System.out.println("New Move");
	    			move = new String(x + " " + y + " " + (x+1) + " "+ y);
	    			list.add(move);
	    		}
	    		board[tmp].removePiece();
                        board[thesi].addPiece(pioni);
                        
	    	}

    		if(pioni.getPosition().getX() == 1 && !board[(x+2)*8 + y].hasPiece()){ //can go two forward
    			tmp = (x+2)*8 + y;
    			board[thesi].removePiece();
    			board[tmp].addPiece(pioni);
    			if(KingSafe(tmpTurn)){
    				//System.out.println("New Move");
    				move = new String(x + " " + y + " " + (x+2) + " "+ y);
    				list.add(move);
    			}
    			board[tmp].removePiece();
                        board[thesi].addPiece(pioni);
    		}
    		//recovery
    		
    	}

    	//kill move
    	for(int i = -1; i <= 1; i = i + 2){//right kai left
    		if(y + i < 0 || y + i >= 8)
    			continue;
    		
    		tmp = (x+1)*8 + y + i;
    		if(board[tmp].hasPiece()){
    			if (board[tmp].whatPiece().getTeam() == TheBoard.White) { //KILL IT
    				oldpioni = board[tmp].whatPiece();
    				board[thesi].removePiece();
    				board[tmp].addPiece(pioni);
    				if(x+1 == 0){ //promotion
		    			for(int j = Piece.ROOKS; j <= Piece.QUEEN; j++){

		    				pioni.changeType(j);
		    				if(KingSafe(tmpTurn)){
		    					//System.out.println("New Move");
			    				move = new String(x + " " + y + " " + (x+1) + " "+ (y+i) + "*" + j + "|" + oldpioni.getType());
			    					list.add(move);
			    			}

		    			}

    					pioni.changeType(Piece.PAWNS);
    				}
    				else if(KingSafe(tmpTurn)){
    					//System.out.println("New Move");
    					move = new String(x + " " + y + " " + (x+1) + " "+ (y+i) + "|" + oldpioni.getType());
    					list.add(move);    				
    				}

    				//recovery
    				board[tmp].addPiece(oldpioni);
    				board[thesi].addPiece(pioni);
    			}
    		}	
    	}
    	//en pasan
    	//TODO
    	return list;
    }

    boolean KingSafe(int tmpTurn){
    	Piece [] pioni;
    	int king;
    	int x, y, thesi;
    	int x_tmp, y_tmp, tmp, flag = 0, dest, dest_x, dest_y, danger;

    	if(tmpTurn == White){
    		danger = -1;
    		king = Wking;
    		thesi = teamWhite[king].getPosition().getPos();
    		pioni = teamBlack;
    	}
    	else{
    		danger = 1;
    		king = Bking;
    		thesi = teamBlack[king].getPosition().getPos();
    		pioni = teamWhite;
    	}

    	x = thesi / 8;
    	y = thesi % 8;

    	for(int i = 0; i < NumOfPieces; i++){
    		flag = 0;
    		if(pioni[i].getPosition() != null && i != king){
    			switch (pioni[i].getType()){
    				case Piece.QUEEN:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;

    					if(Math.abs(x_tmp - x) == Math.abs(y_tmp - y)){  //apeilw diagwnia
                            dest_x = (x > x_tmp) ? 1 : -1;
    						dest_y = (y > y_tmp) ? 1 : -1;

    						for(int j = 1; j <= Math.abs(x - x_tmp); j++){
    							if((x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y) >= SUMofSpots || (x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y) < 0){
    								flag = 1;
    								break;
    							}

    							if(board[(x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y)].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}
    					else
    						flag = 1;

    					if(flag == 0){
    						System.out.println("queen threat king1");
    						return false;
    					}

    					if(x_tmp == x){
    						dest = (y > y_tmp) ? 1 : -1;
    						for(int j = 1; j <= Math.abs(y - y_tmp); j++){
    							if(x_tmp*8 + y_tmp+(j*dest) >= SUMofSpots || x_tmp*8 + y_tmp+(j*dest) < 0){
    								flag = 1;
    								break;
    							}

    							if(board[x_tmp*8 + y_tmp+(j*dest)].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}

    					else if(y_tmp == y){
    						dest = (x > x_tmp)? 1 : -1;
    						for(int j = 1; j <= Math.abs(x - x_tmp); j++){
    							if((x_tmp+(j*dest))*8 + y_tmp >= SUMofSpots || (x_tmp+(j*dest))*8 + y_tmp < 0){
    								flag = 1;
    								break;
    							}
    							if(board[(x_tmp+(j*dest))*8 + y_tmp].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}
    					else
    						flag = 1;

    					if(flag == 0){
    						System.out.println("queen threat king2");
    						return false;
    					}

    					break;
    				case Piece.ROOKS:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;
    					if(x_tmp == x){
                            dest = (y > y_tmp) ? 1 : -1;
    						for(int j = 1; j <= Math.abs(y - y_tmp); j++){
    							if(x_tmp*8 + y_tmp+(j*dest) >= SUMofSpots || x_tmp*8 + y_tmp+(j*dest)< 0){
    								flag = 1;
    								break;
    							}
    							if(board[x_tmp*8 + y_tmp+(j*dest)].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}

    					else if(y_tmp == y){
    						dest_x = (x > x_tmp) ? 1 : -1;
    						for(int j = 1; j <= Math.abs(x - x_tmp); j++){
    							if((x_tmp+(j*dest))*8 + y_tmp >= SUMofSpots || (x_tmp+(j*dest))*8 + y_tmp< 0){
    								flag = 1;
    								break;
    							}
    							if(board[(x_tmp+(j*dest))*8 + y_tmp].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}
    					else
    						flag = 1;

    					if(flag == 0){
    						System.out.println("rook threat king");
    						return false;
    					}
    					
    					break;
    				case Piece.BISHOPS:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;
    					if(Math.abs(x_tmp - x) == Math.abs(y_tmp - y)){
    						dest_x = (x > x_tmp) ? 1 : -1;
                            dest_y = (y > y_tmp) ? 1 : -1;

    						for(int j = 1; j <= Math.abs(x - x_tmp); j++){
    							if((x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y) >= SUMofSpots ||(x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y)< 0){
    								flag = 1;
    								break;
    							}
    							if(board[(x_tmp+(j*dest_x))*8 + y_tmp+(j*dest_y)].hasPiece()){
    								flag = 1;
    								break;
    							}
    						}
    					}
    					else
    						flag = 1;

    					if(flag == 0){
    						System.out.println("bishop threat king");
    						return false;
    					}
    					break;
    				case Piece.KING:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;
    					if((Math.abs(x_tmp-x) == 1) && (Math.abs(y_tmp-y) == 1)){
    						System.out.println("king threat king");
    						return false;
    					}
    					break;
    				case Piece.KNIGHT:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;
    					if((Math.abs(x_tmp-x) == 2) && (Math.abs(y_tmp-y) == 2) && (pioni[i].getPosition().getColour() != board[tmp].getPos())){
    						System.out.println("knight threat king");
    						return false;
    					}
    					break;
    				case Piece.PAWNS:
    					tmp = pioni[i].getPosition().getPos();
    					x_tmp = tmp/8;
    					y_tmp = tmp%8;
    					if(x != x_tmp){
    						dest = (x - x_tmp)/(x - x_tmp);
	    					if((Math.abs(x_tmp-x) == Math.abs(y_tmp-y)) && (Math.abs(y_tmp-y) == 1) && dest==danger){
	    						System.out.println("pawn threat king");
	    						return false;
	    					}
	    				}
    			}
    		}
    	}
        
        return true;

    }

	@Override
	public String toString(){
		StringBuffer str = new StringBuffer("{ \n");
		Piece pioni;


		for(int i = 0; i < TheBoard.SUMofSpots; i++){
			if(board[i].hasPiece()){
				pioni = board[i].whatPiece();
				str.append(pioni.toString() + " ");
			}
			else{
				str.append(" N "); //N for Nothing
			}
			if(i%8 == 7)
				str.append("\n");
		}
		str.append(" }");
		return str.toString();
	}

	/************************************** MOVE EVALUATION *****************************************************/

	private int rating(int movelen, int depth){
		int counter=0;
		Mytype material=rateMaterial();
        



	}

	private Mytype rateMaterial(){
		int counterB=0, counterW=0;
		int WnumofBishops=0, BnumofBishops=0;

		//black pieces
		for(int i = 0; i < NumOfPieces; i++){
			if(teamBlack[i].isAlive()){
    			switch (teamBlack[i].getType()){
    				case Piece.ROOKS: counterB = counterB + 500;
    						break;
    				case Piece.KNIGHT: counterB = counterB + 300;
    						break;
    				case Piece.BISHOPS: BnumofBishops++;
    						break;
    				case Piece.QUEEN: counterB = counterB + 900;
    						break;
    				case Piece.PAWNS: counterB = counterB + 100;
    						break;
    				default: 
    					continue;
    			}
    		}

    		if(teamWhite[i].isAlive()){
    			switch (teamWhite[i].getType()){
    				case Piece.ROOKS: counterW = counterW - 500;
    						break;
    				case Piece.KNIGHT: counterW = counterW - 300;
    						break;
    				case Piece.BISHOPS: WnumofBishops++;
    						break;
    				case Piece.QUEEN: counterW = counterW - 900;
    						break;
    				case Piece.PAWNS: counterW = counterW - 100;
    						break;
    				default: 
    					continue;
    			}
    		}
    	}
		
		if (BnumofBishops>=2) {
		            counterB+=300*BnumofBishops;
		} 
		else
			counterB = counterB + 250*BnumofBishops; //0 or 1

		if (WnumofBishops>=2) {
		            counterW = counterW - 300*WnumofBishops;
		} 
		else
			counterW = counterW - 250*WnumofBishops; //0 or 1


		return (new Mytype(counterB, counterW));

	}

}