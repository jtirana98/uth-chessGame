package chessgame;


public class ChessGame {

    public static void main(String[] args) {
        boolean turn = true, won;
        int res;
        // TODO code application logic here
        //desmeuw gia na ftiaksw mia klasi TheBoard
        
        //loop paixnidiou
        TheBoard game = new TheBoard();
        System.out.println(game);
     
     for(int i = 0; i < 2; i++){
            if(turn){
                System.out.println("MAKE A MOVE: (move format Xsrc Ysrc Xdst Ydst * TypeIfprom)");
                //diavazw kinisi
                //tin elegxw
                //tin ektelw sto board
                //check if we win
                won = game.check();
                if(won){
                    System.out.println("ΕΧΑΣΕΣ");
                    break;
                }
                game.MakeMove("6 3 5 3");
                System.out.println(game);
                turn = false;
            }
            else{
               

                res = game.PCmove();
                if(res == -1){
                    System.out.println("Congratulations! you won!!");
                    break;
                }
                if(res == -2){
                    System.out.println("TIE");
                    break;      
                }
                turn = true;
                
                break;
            }
        }
    }
    
}
