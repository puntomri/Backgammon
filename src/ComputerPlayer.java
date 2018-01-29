import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ComputerPlayer {

    Backgammon game;

    boolean lives;
    int score;


    int eat;
    int open;
    int close;
    int build;
    int skip;
    int stayhome;
    int out;
    int[] parameters;




    ComputerPlayer(Backgammon game,int eat, int open, int close, int build, int skip, int stayhome, int out) {

        lives = true;
        parameters= new int[7];

        this.game = game;

        this.eat = eat;
        parameters[0]=this.eat;
        this.open = open;
        parameters[1]=this.open;
        this.close = close;
        parameters[2]=this.close;
        this.build = build;
        parameters[3]=this.build;
        this.skip = skip;
        parameters[4]=this.skip;
        this.stayhome = stayhome;
        parameters[5]=this.stayhome;
        this.out = out;
        parameters[6]=this.out;
    }


    public void play(){

        boolean currentTurn = game.turn;
        game.rolldices(true);

        while(!game.gameover()&&currentTurn==game.turn) {
            ArrayList<AMove> movelist = new ArrayList<>();


            //create a move list with points
            for (int i = 25; i >= 0; i--) {
                game.validMoves.clear();
                if (!game.board[i].empty() && game.findValidMoves(i)) {

                    int mc = game.validMoves.size();

                    for (int k = 0; k < mc; k++) {

                        game.findValidMoves(i);

                        AMove thisMove = new AMove(i, (int) game.validMoves.get(k));

                        int s = thisMove.startpoint;
                        int f = thisMove.finishpoint;
                        int p = 0;
                        int mekadem;
                        int rmekadem;
                        if (!game.turn) {
                            mekadem = f + 1;
                            rmekadem = 24 - f;
                        } else {
                            rmekadem = f + 1;
                            mekadem = 24 - f;
                        }

                        // scores normal play (not all home)

                        if (((!game.turn && f >= 0) || (game.turn && f <= 23)) && !game.crossed()) {
                            //eat  3
                            if (!game.board[f].empty() && game.board[f].peek().white != game.turn) p += eat * mekadem;
                            // open 1
                            if (game.board[f].empty() || game.board[f].peek().white != game.turn) p -= open * rmekadem;
                            // close 2
                            if (!game.board[f].empty() && game.board[f].peek().white == game.turn && game.board[f].size() == 1)
                                p += close * rmekadem;


                            int om;
                            if (!game.turn) om = game.dice1.value + game.dice2.value - (s - f);
                            else om = -(game.dice1.value + game.dice2.value - (f - s));

                            //build a new house points Scores
                            if (!game.dice1.played && !game.dice2.played && ((!game.turn && f + om < 24) || (game.turn && f + om >= 0))) {

                                if (game.dice1.value != game.dice2.value && (game.board[f].size() == 0 || (game.board[f].peek().white != game.turn)) && game.board[s].size() > 2 && !game.board[f + om].empty() && game.board[f + om].peek().white == game.turn && game.board[f + om].size() > 2) {
                                    p += build * rmekadem; //points for new house build

                                }
                                if (game.dice1.value == game.dice2.value && (game.board[f].size() == 0 || (game.board[f].peek().white != game.turn)) && game.board[s].size() != 3 && game.board[s].size() != 1) {
                                    p += build * rmekadem; //points for new house build if double
                                    if (game.board[s].size() == 2) p -= build/2 * rmekadem;
                                }
                            }

                            //skip points 2.Scores
                            if (!game.dice1.played && !game.dice2.played) {
                                int target;
                                target = (f - om);


                                if (((!game.turn && target >= 0) || (game.turn && target <= 23)) && !game.board[target].empty() && game.board[target].peek().white == game.turn) {
                                    p += skip * rmekadem; //points for skip
                                    // System.out.println("skip!!!  " + s + " to " + f + " and to" + (f - om) );
                                }
                            }

                            if (game.board[s].size() == 2) p -= open * rmekadem; //startpoint has 2 blacks / 1
                            if (game.board[s].size() == 1) p += close/2 * rmekadem; //startpoint has 1 black / 1

                            if ((!game.turn && s < 6) || (game.turn && s > 17))
                                p -= stayhome * rmekadem; //dont move when already home... /1

                            //points after cross / out 3
                        } else if (game.crossed()) {

                            if ((!game.turn && f < 0) || (game.turn && f > 23)) {
                                p = out * 24; //take out pieces - all home case
                            } else {
                                if ((!game.turn && s > 5) || (game.turn && s < 18)) {
                                    p = mekadem;//running home
                                    if ((!game.turn && f == 5) || (game.turn && f == 18)) p = 25;
                                } else {  //insert hume at index Scores / Scores
                                    if (game.board[f].empty()) p += 5;
                                }
                            }

                        } else if ((!game.turn && f < 0 || game.turn && f > 23) && !game.crossed()) {//points hen all home but white inside

                            if (game.board[s].size() == 2) p -= open * rmekadem; //startpoint has 2 blacks
                            if (game.board[s].size() == 1) p += close * rmekadem; //startpoint has 1 black

                        }


                        //ads the points to the move object
                        thisMove.points = p;
                        movelist.add(thisMove);


                    }
                }
            }

            AMove chosenMove;
            if(!game.turn) chosenMove = movelist.get(0);
            else chosenMove = movelist.get(movelist.size()-1);

            for (int i = 0; i < movelist.size(); i++) {

                if (movelist.get(i).points > chosenMove.points) chosenMove = movelist.get(i);

            }


            game.takeAPiece(chosenMove.startpoint);


            if (game.pieceinAir) game.placeAPiece(chosenMove.finishpoint);



        }


    }


    public void mutate(int gen, int changesize){

        parameters[gen]+=changesize;

        if (parameters[gen]>9)parameters[gen]=9;
        if (parameters[gen]<0)parameters[gen]=0;


    }

}
