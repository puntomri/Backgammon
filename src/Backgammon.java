import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Backgammon {

    private static BgGui bggui;

    Stack<Piece>[] board;
    boolean turn;
    boolean pieceinAir;
    private int inAirOrig;
    Dice dice1;
    Dice dice2;

    private boolean duble;
    boolean canPlay;
    Stack validMoves;
    private int movecounter;


    boolean computerPlaysBlack;
    boolean computerPlaysWhite;
    boolean messagesOn;
    int level;

    int blackwins;
    int whitewins;

    ComputerPlayer computerPlayBlack;
    ComputerPlayer computerPlayWhite;






    public Backgammon(){
        //8259226

       // computerPlayWhite = new ComputerPlayer(this,8,2,5,9,2,2,6);
        computerPlayBlack= new ComputerPlayer(this,9,2,6,9,1,2,2);
        computerPlayWhite= new ComputerPlayer(this,3,1,2,5,3,1,3);
        //computerPlayBlack = new ComputerPlayer(this,3,1,2,Scores,3,1,3);

        board =  new Stack [26];
       // whiteboard = new Stack[26];
       // blackboard = new Stack[26];
        validMoves = new Stack<Integer>();
       // pieceCanOut = false;
        dice1 = new Dice(this);
        dice2 = new Dice(this);
        messagesOn = true;

        //level =1;


        for(int i=0; i<26;i++){
            board[i] = new Stack<>();
           // if (i<24) blackboard[23-i] = board[i];
        }



        //setPieces();
       // board = whiteboard;
    }


    //starts a new game
    public void setPieces(boolean comp,boolean comp2){

        for(int i=0; i<26;i++){
            board[i].clear();
        }
        for (int i =0;i<2;i++) {
           board[0].add(new Piece(true));
           board[23].add(new Piece(false));
        }
       for (int i =0;i<5;i++) {
         board[11].add(new Piece(true));
          board[18].add(new Piece(true));
          board[12].add(new Piece(false));
          board[5].add(new Piece(false));
        }
        for (int i =0;i<3;i++) {
            board[16].add(new Piece(true));
            board[7].add(new Piece(false));
        }


        int turnchooser = (int) (Math.random()*2);
        if (turnchooser ==1) turn = true;
        else turn = false;


        //bggui.turnfield.setBackground(Color.WHITE);
        pieceinAir = false;
        canPlay = false;
        validMoves.clear();
        dice1.setDice(0);
        dice2.setDice(0);

        if (comp) computerPlaysBlack=true;
        else computerPlaysBlack=false;
        if (comp2) computerPlaysWhite=true;
        else computerPlaysWhite=false;



        for (int i =0;i<26;i++) {
            bggui.panelGrid[i].repaint();

        }
        turnchanger(!turn);
    }

    public void takeAPiece(int index){

        //check if this index can play a valid move. (if not, does nothing)
           if(!board[index].empty()&&turn==board[index].peek().white) {
           boolean allhome =allHome();


            if( findValidMoves(index)) {

                board[index].pop();
                bggui.panelGrid[index].repaint();


               if(allhome) {
                //if all pieces home (piece can out)
                   boolean lastcheck = true;
                   for (int i = 1;i<6; i++){
                       if((!turn&&!board[index+i].empty()&&board[index+i].peek().white==turn)||(turn&&!board[index-i].empty()&&board[index-i].peek().white==turn)){
                           lastcheck = false;
                       }
                   }
//
//

                   // System.out.println(lastcheck);
                   if (!dice1.played&&(index==dice1.value-1||index==24-dice1.value)){
                       dice1.a.setEnabled(false);
                       dice1.played = true;
                       movecounter--;

                   } else if(!dice2.played&&(index==dice2.value-1||index==24-dice2.value)){
                       dice2.a.setEnabled(false);
                       dice2.played = true;
                       movecounter--;
                   } else if (!dice1.played&&(dice1.value-1>index&&lastcheck)||(24-dice1.value<index&&lastcheck)) {
                       dice1.a.setEnabled(false);
                       dice1.played = true;
                       movecounter--;
                   }else if (!dice2.played&&(dice2.value-1>index&&lastcheck)||(24-dice2.value<index&&lastcheck)){
                       dice2.a.setEnabled(false);
                       dice2.played = true;
                       movecounter--;
                       } else if((turn&&(index+dice1.value<24||index+dice2.value<24))||(!turn&&(index-dice1.value>=0||index-dice2.value>=0))){
                       pieceinAir = true;

                   }


               }  else {
                   //normal case

                    pieceinAir = true;

               }



                 //check game over
                if (!gameover()) {

                    if (movecounter == 0) {

                        turnchanger(turn);
                    } else {

                        //change turns directly if stuck
                        if (!pieceinAir) stuckCheck();

                        //normal move
                        inAirOrig = index;
                        findValidMoves(index);


                    }

                } else {
                    if (!messagesOn) {
                        if (turn) {
                            whitewins++;
                           // System.out.println("white wins "+(whitewins+blackwins));
                        }
                        else {

                            blackwins++;
                            //System.out.println("black wins" + (whitewins+blackwins));
                        }
                    }
                    else bggui.gameOverM(turn);


                }


            }
        }
    }


    //place a piece in certain index, including all checks for move complete

    public void placeAPiece(int index){

        //cheks if move is valid
        if(validMoves.search(index)>= 0) {

            //eat!!
            if(board[index].size()==1&&board[index].peek().white!=turn){
                if(turn) board[24].add(board[index].pop());
                if(!turn) board[25].add(board[index].pop());
                bggui.panelGrid[25].repaint();
                bggui.panelGrid[24].repaint();

            }

            //do graphics
            board[index].add(new Piece(turn));
            bggui.panelGrid[index].repaint();
            pieceinAir = false;

            movecounter--;



            //remove correct dice from next move (both gui and "move")

            if(inAirOrig==25&&turn){
                if (!dice1.played&&dice1.value==index+1){
                    dice1.a.setEnabled(false);
                    dice1.played=true;
                } else if (!dice2.played&&dice2.value==index+1){
                    dice2.a.setEnabled(false);
                    dice2.played=true;
                }
            }   else if(inAirOrig==24&&!turn){
                if (!dice1.played&&dice1.value==24-index){
                    dice1.a.setEnabled(false);
                    dice1.played=true;
                } else if (!dice2.played&&dice2.value==24-index){
                    dice2.a.setEnabled(false);
                    dice2.played=true;
                }
            } else {

                int minus = 1;
                if (!turn) minus = -1;

                if (!dice1.played && dice1.value == (index - inAirOrig) * minus) {
                    dice1.a.setEnabled(false);
                    dice1.played = true;
                } else if (!dice2.played && dice2.value == (index - inAirOrig) * minus) {

                    dice2.a.setEnabled(false);
                    dice2.played = true;
                }
            }


                inAirOrig=-1;



            //change turns directly if stuck
            //calls turn changer if turn is over
            if(movecounter==0)turnchanger(turn);
            else stuckCheck();





        }
    }


    public void rolldices(boolean switched){



        if(!canPlay) {
            dice1.rollDice();
            dice2.rollDice();
            if (dice1.value == dice2.value) duble = true;
            else duble = false;
            movecounter = 2;
            canPlay = true;
            dice1.played=false;
            dice2.played=false;
            dice1.a.setEnabled(true);
            dice2.a.setEnabled(true);
            inAirOrig=-1;
            stuckCheck();
        }

    }




    //finds valid moves for any index and saves them at validmoves array. returns false if no valid moves
    public Boolean findValidMoves(int index){
        validMoves.clear();
        int opMove1 = -10;
        int opMove2 = -10;




        //return false (for stuckcheck)
        if((board[index].empty()&&!pieceinAir)||!board[index].empty()&&board[index].peek().white!=turn){

            return false;

        }

        //check if there are no pieces out (eaten)

            if ((turn && board[25].empty())&&inAirOrig!=25 || (!turn && board[24].empty()&&inAirOrig!=24)) {

                //normal case
                if (turn) {
                    if(!dice1.played)opMove1 = index + dice1.value;
                    if(!dice2.played)opMove2 = index + dice2.value;
                } else {
                    if(!dice1.played)opMove1 = index - dice1.value;
                    if(!dice2.played)opMove2 = index - dice2.value;
                }
            } else {
                //case a piece is "eaten"

                if (( turn && (index==25 || inAirOrig == 25)) || (!turn && (index==24 || inAirOrig==24)) ) {
                    if (turn) {
                        if(!dice1.played)opMove1 = dice1.value - 1;
                        if(!dice2.played)opMove2 = dice2.value - 1;
                    } else {
                        if(!dice1.played)opMove1 = 24-dice1.value;
                        if(!dice2.played)opMove2 = 24-dice2.value;
                    }
                } else return false;

            }

            //update valid moves for the spot/ normal case
        if(!allHome()) {

            if ((opMove1 < 24 && opMove1 >= 0)   &&   (board[opMove1].empty() || (board[opMove1].peek().white == turn) || (board[opMove1].size() == 1))) {
                validMoves.add(opMove1);
            }
            if ((opMove2 < 24 && opMove2 >= 0) && (board[opMove2].empty() || (board[opMove2].peek().white == turn) || (board[opMove2].size() == 1))) {
                validMoves.add(opMove2);
            }
        } else {
                //all home case
            boolean lastcheck = true;
            int ls = 1;
            if (turn) ls = -1;
            for (int i = 1;i<6; i++){
                if(!board[index+(i*ls)].empty()&&board[index+(i*ls)].peek().white==turn){
                    lastcheck = false;
                }
            }



            if(opMove1>23||(opMove1<0&&opMove1>-10)){
                if(lastcheck||(!turn&&index==dice1.value-1||turn&&index==24-dice1.value))   validMoves.add(opMove1);
            } else if(opMove1>=0&&opMove1<24) {
                if ((board[opMove1].empty() || (board[opMove1].peek().white == turn) || (board[opMove1].size() == 1))) {
                    validMoves.add(opMove1);
                }
            }


            if(opMove2>23||(opMove2<0&&opMove2>-10)){
                if(lastcheck||(!turn&&index==dice2.value-1||turn&&index==24-dice2.value)) validMoves.add(opMove2);
            } else if(opMove2>=0&&opMove1<24) {
                if ((board[opMove2].empty() || (board[opMove2].peek().white == turn) || (board[opMove2].size() == 1))) {
                    validMoves.add(opMove2);
                }


            }

            //update valid moves for the spot/ all home case
            /*try {
                if ((board[opMove1].empty() || (board[opMove1].peek().white == turn) || (board[opMove1].size() == 1))) {
                    validMoves.add(opMove1);
                }
                if ((board[opMove2].empty() || (board[opMove2].peek().white == turn) || (board[opMove2].size() == 1))) {
                    validMoves.add(opMove2);
                }
            } catch (ArrayIndexOutOfBoundsException e){

              if(opMove1>-10)validMoves.add(opMove1);
                             // pieceCanOut = true;
              if(opMove2>-10)validMoves.add(opMove2);

                             // pieceCanOut = true;

            }

*/

        }

        //System.out.println("for index" + index + "  ,size "+ board[index].size() + "valid moves:" + validMoves);

       if (validMoves.empty()) return false;
        else return true;
    }




    //checks if all pieces inside home, true if all pieces inside:
    public boolean allHome(){
        boolean allHome=true;

        if(!turn){
            for(int i=6;i<26;i++){
                if((!board[i].empty()&&board[i].peek().white==turn)||(pieceinAir&&inAirOrig>5)) allHome=false;
            }
        } else {
            for(int i=0;i<18;i++){
                if((!board[i].empty()&&board[i].peek().white==turn||(pieceinAir&&inAirOrig<18))) allHome=false;
            }
            if((!board[25].empty()&&board[25].peek().white==turn)||(pieceinAir&&inAirOrig==25)) allHome=false;

        }
        return allHome;
    }

    public boolean gameover(){
        boolean over = true;
        for (int i = 0;i<26;i++){
            if (!board[i].empty()&& board[i].peek().white==turn) over = false;

        }
        if (pieceinAir) over = false;


        if(over){
            //bggui.gameOverM(turn);
            for(int i=0; i<26;i++){
                board[i].clear();
                bggui.panelGrid[i].repaint();
            }

            //turn = true;
           // bggui.turnfield.setBackground(Color.WHITE);
            pieceinAir = false;
            inAirOrig =0;
            canPlay = false;
            validMoves.clear();
            dice1.setDice(0);
            dice2.setDice(0);
            duble=false;


            return true;
        }

        return false;

    }


    //change turns directly if stuck
    public void stuckCheck(){


        boolean stuck = true;
        for(int i=0; i<26;i++){
            if(findValidMoves(i)) stuck = false;
        }

        if (stuck) {
           // System.out.println("Stuck");
            if(duble){
                duble=false;
            }

            if (messagesOn) {
                bggui.stuck(turn);
            }

            dice1.a.setEnabled(false);
            dice2.a.setEnabled(false);
            //movesleft=0;
            turnchanger(turn);




        }
    }


    //change turns
    public void turnchanger(boolean turn){


        if (!duble){
            if (turn){
                this.turn = false;
                bggui.turnfield.setBackground(Color.BLACK);
            } else {
                this.turn= true;
                bggui.turnfield.setBackground(Color.WHITE);
            }

            canPlay=false;

            movecounter=2;
            validMoves.clear();


            if (turn&&computerPlaysBlack)  computerPlayBlack.play();
            if (!turn&&computerPlaysWhite)  computerPlayWhite.play();

            //if (turn&&computerPlaysBlack)  computerPlay(3,1,2,Scores,3,1,3);
            //case of a double in dices
        } else {
           dice1.played=false;
           dice2.played=false;
            dice1.a.setEnabled(true);
            dice2.a.setEnabled(true);
            duble = false;
            movecounter=2;
            stuckCheck();
            validMoves.clear();



        }



    }


    public boolean crossed(){

        int in =0; //last black cell
        for (int i=0;i<26;i++){
            if (!board[i].empty() && !board[i].peek().white) in = i;
        }

        for (int i=0;i<in;i++){
            if (!board[i].empty() && board[i].peek().white) return false;
        }
        if ((!turn&&!board[25].empty())||(turn&&!board[24].empty())) return false;


      //  System.out.println("******************crossed************************");
        return true;
    }








    public void computerPlayRandom(){
        boolean currentTurn = turn;
        rolldices(true);


        while(!gameover()&&currentTurn==turn) {
            ArrayList<AMove> movelist = new ArrayList<>();


            //create a move list with points
            for (int i = 25; i >= 0; i--) {
                validMoves.clear();
                if (!board[i].empty() && findValidMoves(i)) {

                    for (int k = 0; k <validMoves.size() ; k++) {

                        findValidMoves(i);

                        AMove thisMove = new AMove(i, (int) validMoves.get(k));
                        movelist.add(thisMove);


                    }
                }
            }



            AMove chosenMove;

            chosenMove = movelist.get((int)(Math.random()*movelist.size()));


            takeAPiece(chosenMove.startpoint);

            if (pieceinAir) placeAPiece(chosenMove.finishpoint);
        }


    }

    public int simulator(ComputerPlayer white,int games){


        computerPlayWhite = white;

        int score;
        messagesOn = false;

        for (int i = 0; i<games;i++){
         //System.out.println("Starting game: "+(i+1));
            setPieces(true,true);

        }

       System.out.println("Black wines: "+blackwins+ "  White wins: "+ whitewins);

        score = whitewins;
        blackwins =0;
        whitewins = 0;

        messagesOn = true;

        return score;

    }


    public static void main(String args[]){


        bggui = new BgGui(new Backgammon());



    }
}
