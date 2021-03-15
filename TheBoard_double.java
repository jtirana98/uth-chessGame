package chessgame;

import static java.lang.Math.abs;
import java.util.*;


public class TheBoard_double {
    //exei enan pinaka 8 * 8 gia tis theseis
    private Piece [] teamWhite;
    private Piece [] teamBlack;

    private Spot [] board;

    public static final int SUMofSpots = 64;
    public static final int NumOfPieces = 16;
    public static final int Black = 0;
    public static final int White = 1;

   	private int Bking = 4;
   	private int Wking = 12;
   	private int BKnight1 = 1;
   	private int BKnight2 = 6;
   	private int WKnight1 = 9;
   	private int WKnight2 = 14;


    //when the game starts we initialize the pieces to the default spots(starting point)
    public TheBoard(){
    	board = new Spot[TheBoard.SUMofSpots];
    	teamWhite = new Piece[TheBoard.NumOfPieces];
    	teamBlack = new Piece[TheBoard.NumOfPieces];
    	
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


    public String CalculateNextMove(){
        return null;
    	//i apply alpha-beta algorithm



    }

     public void MakeMove(String move){
    	//format of move str x_sourcey_sourcex_desty_dest*type
    	//*type in case of promotion

    	//NA VALW ELEGXW GIA THN NOMIMOTITA TIS KINISIS??????????????

    	int x_s, y_s, x_d, y_d,  promotion;
    	Piece pioni;

    	x_s = Integer.parseInteger(move.charAt(0));
    	y_s = Integer.parseInteger(move.charAt(1));

    	x_d = Integer.parseInteger(move.charAt(2));
    	y_d = Integer.parseInteger(move.charAt(3));

    	pioni = board[x_s*8 + y_s].removePiece();
    	if(board[x_d*8 + y_d].hasPiece())
    		board[x_d*8 + y_d].whatPiece().kill();
    	
    	board[x_d*8 + y_d].addPiece(pioni);
    	pioni.setPosition(board[x_d*8 + y_d]);

    	if(move.length() > 4){ //promotion of pawn
    		promotion = Integer.parseInteger(move.charAt(5));
    		pioni.changeType(promotion);
    	}

    }


    private void UndoMove(String move){
    	int x_s, y_s, x_d, y_d, killed=-1;
    	Piece pioni, walkingDead;

    	x_s = Integer.parseInteger(move.charAt(0));
    	y_s = Integer.parseInteger(move.charAt(1));

    	x_d = Integer.parseInteger(move.charAt(2));
    	y_d = Integer.parseInteger(move.charAt(3));

    	pioni = board[x_d*8 + y_d].removePiece();
    	pioni.setPosition(board[x_s*8 + y_s]);
    	board[x_s*8 + y_s].addPiece(pioni);
    	

    	if(move.length() > 4){
    		if(move.charAt(5) == '*'){ //eixame promotion
    			pioni.changeType(Piece.PAWNS);
    			if(move.length() > 6)
    				killed = Integer.parseInteger(move.charAt(7));
    		}
    		else
    			killed = Integer.parseInteger(move.charAt(5));
    		
    		if(killed != -1){
    			if(pioni.getTeam() == White){
    				for(int i = 0 ; i < NumOfPieces; i++){
    					if((teamBlack[i].isAlive == false) && teamBlack[i].getType == killed){
    						walkingDead = teamBlack[i];
    						break;
    					}
    				}
    			}
    			else{
    				for(int i = 0 ; i < NumOfPieces; i++){
    					if((teamWhite[i].isAlive == false) && teamWhite[i].getType == killed){
    						walkingDead = teamWhite[i];
    						break;
    					}
    				}
    			}
    			walkingDead.reincarnation(board[x_d*8 + y_d]);
    		}
    	}
    }


    public List<String> PossibleMovesWhites(){  
    	//format x_sy_sx_d_y_d-KILLED/*PromotionTo
    	LinkedList<String> moves = new LinkedList<String>();

    	for(int i = 0; i < NumOfPieces; i++){
    		if(teamWhite[i].isAlive()){
    			switch (teamWhite[i].getType()){
    				case 0: moves.addAll(WhiteRookPossibleMoves(i));
    						break;
    				case 1: moves.addAll(WhiteKnightPossibleMoves(i));
    						break;
    				case 2: moves.addAll(WhiteBishopPossibleMoves(i));
    						break;
    				case 3: moves.addAll(WhiteQueenPossibleMoves(i));
    						break;
    				case 4: moves.addAll(WhiteKingPossibleMoves(i));
    						break;
    				case 5: moves.addAll(WhitePawnPossibleMoves(i));
    						break;

    			}
    		}
    	}

    	return moves;

    }


    private List<String> WhiteRookPossibleMoves(int pos){
    	Piece pioni = teamWhite[pos];
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
		    				if(board[tmp].whatPiece().getTeam() == Piece.White)
		    					x_continue = false;		
		    			}
		    			if(x_continue){
			    			if(board[tmp].hasPiece()){
			    				oldpioni = board[tmp].whatPiece();
			    				x_continue = false;
			    			}
			    			else
			    				oldpioni = null;

			    			board[tmp].addPiece(pioni);
			    			board[thesi].removePiece();
			    			if(WhiteKingSafe()){
			    				if(oldpioni != null){
			    					move = new String(x + " " + y + " " + x_tmp + " "+ y + "|" + oldpioni.getType());
			    					board[tmp].addPiece(oldpioni);
			    					oldpioni = null;
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
		    				if(board[tmp].whatPiece().getTeam() == Piece.White)
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

		    			board[tmp].addPiece(pioni);
		    			board[thesi].removePiece();
		    			if(WhiteKingSafe()){
		    				if(oldpioni != null){
		    					move = new String(x + " " + y + " " + (x) + " "+ (y_tmp) + "|" + oldpioni.getType());
		    					board[tmp].addPiece(oldpioni);
		    					oldpioni = null;
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



    private List<String> WhiteKnightPossibleMoves(int pos){
		Piece pioni = teamWhite[pos];
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
	    				if(board[tmp].whatPiece().getTeam() == Piece.White)
	    					continue; //cannot go there
	    				else
	    					oldpioni = board[tmp].whatPiece();
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			
	    			pioni.setPosition(board[tmp]); //mono gia ta knight to kanw auto, tha xreiastei sto KingSafe()

	    			if(WhiteKingSafe()){
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
	    			pioni.setPosition(board[thesi]);
	    			board[thesi].addPiece(pioni);
				}

    		}

    	}
    	return list;
    }


    private List<String> WhiteBishopPossibleMoves(int pos){
    	Piece pioni = teamWhite[pos];
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
	    				if(board[tmp].whatPiece().getTeam() == Piece.White)
	    					break; //cannot go there
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			if(WhiteKingSafe()){
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


    private List<String> WhiteQueenPossibleMoves(int pos){
    	Piece pioni = teamWhite[pos];
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
    	for(int i = -1; i <= 1; i = i++){
    		for(int j = -1 ; j <= 1; j = j++){
    			
    			if (i == 0 && j == 0)
    				continue;
    			
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
	    				if(board[tmp].whatPiece().getTeam() == Piece.White)
	    					break; //cannot go there
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			if(WhiteKingSafe()){
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



    private List<String> WhiteKingPossibleMoves(int pos){
    	Piece pioni = teamWhite[pos];
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
    				if(board[tmp].whatPiece().getTeam() == Piece.White)
    					continue; //cannot go there
    				else
    					oldpioni = board[tmp].whatPiece();
    			}
    			else
    				oldpioni = null;

    			board[tmp].addPiece(pioni);
    			board[thesi].removePiece();
    			if(WhiteKingSafe()){
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

    
    public List<String> WhitePawnPossibleMoves(int pos){ //pane apo katw pros ta panw ara pron mikrotera
    	Piece pioni = teamWhite[pos];
    	Piece oldpioni = null;
    	int thesi = pioni.getPosition().getPos();
    	int x = thesi/8;
    	int y = thesi%8;
    	int tmp;
    	String move;

    	LinkedList<String> list = new LinkedList<String>();


    	//x meiwnetai

    	//go forward
    	tmp = (x-1)*8 + y;
    	if(!board[tmp].hasPiece()){
    		board[tmp].addPiece(pioni);
    		board[thesi].removePiece();
    		if(x-1 == 0){ //promotion
    			for(int i = Piece.ROOKS; i <= Piece.QUEEN; i++){

    				pioni.changeType(i);
    				if(WhiteKingSafe()){
	    				move = new String(x + " " + y + " " + (x-1) + " "+ y + "*" + i);
	    				list.add(move);
	    			}

    			}
    			board[tmp].removePiece();
    			pioni.changeType(Piece.PAWNS);
    		}
    		else{ //one forward   			
	    		if(WhiteKingSafe()){
	    			move = new String(x + " " + y + " " + (x-1) + " "+ y);
	    			list.add(move);
	    		}
	    		board[tmp].removePiece();
	    	}

    		if(pioni.getPosition().getX() == 6){ //can go two forward
    			tmp = (x-2)*8 + y;
    			board[tmp].addPiece(pioni);
    			board[tmp - 8].removePiece();
    			if(WhiteKingSafe()){
    				move = new String(x + " " + y + " " + (x-2) + " "+ y);
    				list.add(move);
    			}
    			board[tmp].removePiece();
    		}
    		//recovery
    		board[thesi].addPiece(pioni);
    	}

    	//kill move
    	for(int i = -1; i <= 1; i = i + 2){//right kai left
    		tmp = (x-1)*8 + y + i;
    		if(board[tmp].hasPiece()){
    			if (board[tmp].whatPiece().getTeam() == TheBoard.Black) { //KILL IT
    				oldpioni = board[tmp].whatPiece();
    				board[tmp].addPiece(pioni);
    				board[thesi].removePiece();
    				if(x-1 == 0){ //promotion
		    			for(int j = Piece.ROOKS; j <= Piece.QUEEN; j++){

		    				pioni.changeType(j);
		    				if(WhiteKingSafe()){
			    				move = new String(x + " " + y + " " + (x-1) + " "+ (y+i) + "*" + j + "|" + oldpioni.getType());
			    					list.add(move);
			    			}

		    			}

    					pioni.changeType(Piece.PAWNS);
    				}
    				else if(WhiteKingSafe()){
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


     private boolean WhiteKingSafe(){ 
    	



    	
    }

//Note: dialeksa LinkedList gt se sugkrisi me tin ArrayList exei pio grigori eisagwgi, vevaia exei pio argi anazitisi omws outos
//i allos emeis tha ta diasxizoume siriaka

    public List<String> PossibleMovesBlacks(){ 
    	//format x_sy_sx_d_y_d|KILLED*PromotionTo
    	LinkedList<String> moves = new LinkedList<String>();

    	for(int i = 0; i < NumOfPieces; i++){
    		if(teamBlack[i].isAlive()){
    			switch (teamBlack[i].getType()){
    				case 0: moves.addAll(BlackRookPossibleMoves(i));
    						break;
    				case 1: moves.addAll(BlackKnightPossibleMoves(i));
    						break;
    				case 2: moves.addAll(BlackBishopPossibleMoves(i));
    						break;
    				case 3: moves.addAll(BlackQueenPossibleMoves(i));
    						break;
    				case 4: moves.addAll(BlackKingPossibleMoves(i));
    						break;
    				case 5: moves.addAll(BlackPawnPossibleMoves(i));
    						break;

    			}
    		}
    	}

    	return moves;

    }


    private List<String> BlackRookPossibleMoves(int pos){
    	Piece pioni = teamBlack[pos];
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
		    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
		    					x_continue = false;		
		    			}
		    			if(x_continue){
			    			if(board[tmp].hasPiece()){
			    				oldpioni = board[tmp].whatPiece();
			    				x_continue = false;
			    			}
			    			else
			    				oldpioni = null;
			    			board[tmp].addPiece(pioni);
			    			board[thesi].removePiece();
			    			if(BlackKingSafe()){
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
		    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
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

		    			board[tmp].addPiece(pioni);
		    			board[thesi].removePiece();
		    			if(BlackKingSafe()){
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

    private List<String> BlackKnightPossibleMoves(int pos){
    	Piece pioni = teamBlack[pos];
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
	    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
	    					continue; //cannot go there
	    				else
	    					oldpioni = board[tmp].whatPiece();
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			pioni.setPosition(board[tmp]); //mono gia ta knight to kanw auto, tha xreiastei sto KingSafe()
	    			if(WhiteKingSafe()){
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
	    			pioni.setPosition(board[thesi]);
	    			board[thesi].addPiece(pioni);
				}

    		}

    	}
    	return list;
    	
    }


    private List<String> BlackBishopPossibleMoves(int pos){
    	Piece pioni = teamBlack[pos];
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
	    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
	    					break; //cannot go there
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			if(WhiteKingSafe()){
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


    private List<String> BlackQueenPossibleMoves(int pos){
    	Piece pioni = teamBlack[pos];
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
    			
    			if (i == 0 && j == 0) 
    				continue;
    			

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
	    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
	    					break; //cannot go there
	    				else{
	    					oldpioni = board[tmp].whatPiece();
	    					forward = false;
	    				}
	    			}
	    			else
	    				oldpioni = null;

	    			board[tmp].addPiece(pioni);
	    			board[thesi].removePiece();
	    			if(BlackKingSafe()){
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



    private List<String> BlackKingPossibleMoves(int pos){
    	Piece pioni = teamBlack[pos];
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
    				if(board[tmp].whatPiece().getTeam() == Piece.Black)
    					continue; //cannot go there
    				else
    					oldpioni = board[tmp].whatPiece();
    			}
    			else
    				oldpioni = null;

    			board[tmp].addPiece(pioni);
    			board[thesi].removePiece();
    			if(BlackKingSafe()){
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

    
    private List<String> BlackPawnPossibleMoves(int pos){ //pane apo panw pros ta katw
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
    		board[tmp].addPiece(pioni);
    		board[thesi].removePiece();
    		if(x+1 == 0){ //promotion
    			for(int i = Piece.ROOKS; i <= Piece.QUEEN; i++){

    				pioni.changeType(i);
    				if(BlackKingSafe()){
	    				move = new String(x + " " + y + " " + (x+1) + " "+ y + "*" + i);
	    				list.add(move);
	    			}

    			}
    			board[tmp].removePiece();
    			pioni.changeType(Piece.PAWNS);
    		}
    		else{ //one forward   			
	    		if(BlackKingSafe()){
	    			move = new String(x + " " + y + " " + (x+1) + " "+ y);
	    			list.add(move);
	    		}
	    		board[tmp].removePiece();
	    	}

    		if(pioni.getPosition().getX() == 1){ //can go two forward
    			tmp = (x+2)*8 + y;
    			board[tmp].addPiece(pioni);
    			board[tmp - 8].removePiece();
    			if(BlackKingSafe()){
    				move = new String(x + " " + y + " " + (x+2) + " "+ y);
    				list.add(move);
    			}
    			board[tmp].removePiece();
    		}
    		//recovery
    		board[thesi].addPiece(pioni);
    	}

    	//kill move
    	for(int i = -1; i <= 1; i = i + 2){//right kai left
    		tmp = (x+1)*8 + y + i;
    		if(board[tmp].hasPiece()){
    			if (board[tmp].whatPiece().getTeam() == TheBoard.White) { //KILL IT
    				oldpioni = board[tmp].whatPiece();
    				board[tmp].addPiece(pioni);
    				board[thesi].removePiece();
    				if(x+1 == 0){ //promotion
		    			for(int j = Piece.ROOKS; j <= Piece.QUEEN; j++){

		    				pioni.changeType(j);
		    				if(BlackKingSafe()){
			    				move = new String(x + " " + y + " " + (x+1) + " "+ (y+i) + "*" + j + "|" + oldpioni.getType());
			    					list.add(move);
			    			}

		    			}

    					pioni.changeType(Piece.PAWNS);
    				}
    				else if(BlackKingSafe()){
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


    private boolean BlackKingSafe(){ //no move is legal if king is not safe



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
}