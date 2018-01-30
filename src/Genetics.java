import java.util.ArrayList;
import java.util.Collections;

public class Genetics {

    Backgammon game;
    ArrayList<ComputerPlayer> population;
    ArrayList<ComputerPlayer> newPopulation;

    Genetics(Backgammon game){

        this.game = game;
        population = new  ArrayList<>();
        newPopulation = new  ArrayList<>();

    }


    public void creatFirstGeneration(int size,int games){

        for (int i=0;i<size;i++){

            ComputerPlayer compWhite = new ComputerPlayer(game,(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10),(int)(Math.random()*10));

            compWhite.score = game.simulator(compWhite,games);

            population.add(compWhite);

           // System.out.println("Comp "+ i+":  "+ compWhite.eat+compWhite.open+compWhite.close+compWhite.build+compWhite.skip+compWhite.stayhome+compWhite.out+ "   Score:  "+compWhite.score);

        }



    }

    public void sort(ArrayList<ComputerPlayer> list){

        for (int i = list.size()-1; i >= 0 ; i--){
            for (int j = 0; j<i; j++){

                if(list.get(j).score >list.get(j+1).score){
                    Collections.swap(list,j,(j+1));

                }
            }
        }

    }




    public void reGenerate(ArrayList<ComputerPlayer> pop){


        // keep 10% best
        for (int i = pop.size()-1; i>=pop.size()/10*9; i--){
            ComputerPlayer compWhite = pop.get(i);
            newPopulation.add(compWhite);
        }


        //crossbreed
        for (int i = 0; i< pop.size()*9/10;i++){
            ComputerPlayer compWhite1 = pop.get((int)(Math.random()*pop.size()));
            ComputerPlayer compWhite2 = pop.get((int)(Math.random()*pop.size()));

            int[] chromosome = new int[7];

            for (int j=0;j<7;j++){

                int s = (int)(Math.random()*2);
                if (s==0) chromosome[j]= compWhite1.parameters[j];
                else chromosome[j] = compWhite2.parameters[j];

            }

            ComputerPlayer newComp = new ComputerPlayer(game,chromosome[0],chromosome[1],chromosome[2],chromosome[3],chromosome[4],chromosome[5],chromosome[6]);
            newPopulation.add(newComp);

        }


        //mutate Scores%
        for (int i = 0; i< newPopulation.size()*0.05;i++){
            int change = (int)(Math.random()-0.5);
            if (change >=0) change = 3;
            else change = -3;

            ComputerPlayer compWhite1 = pop.get((int)(Math.random()*pop.size()*9/10)+10);
            compWhite1.mutate((int)(Math.random()*7),change);



        }


    }

    public void reGenerateVer2(ArrayList<ComputerPlayer> pop){

        //create roulette array
        ArrayList<ComputerPlayer> roulette = new ArrayList<>();
        for (int i = 0; i< pop.size();i++){
            int relativescore =  pop.get(i).score - pop.get(0).score;
            System.out.println("no  " + i + "  rscore=  " + relativescore + "  score=  "+ pop.get(i).score);
            for (int j = 0;j<relativescore; j++){
                roulette.add(pop.get(i));
            }

        }


        //crossbreed with proportional probability from the roulette
        for (int i = 0; i< pop.size();i++){
            ComputerPlayer compWhite1 = roulette.get((int)(Math.random()*roulette.size()));
            ComputerPlayer compWhite2 = roulette.get((int)(Math.random()*roulette.size()));

            int[] chromosome = new int[7];

            for (int j=0;j<7;j++){

                int s = (int)(Math.random()*2);
                if (s==0) chromosome[j]= compWhite1.parameters[j];
                else chromosome[j] = compWhite2.parameters[j];

            }

            ComputerPlayer newComp = new ComputerPlayer(game,chromosome[0],chromosome[1],chromosome[2],chromosome[3],chromosome[4],chromosome[5],chromosome[6]);
            newPopulation.add(newComp);

        }


        //mutate Scores%
        for (int i = 0; i< newPopulation.size()*0.05;i++){
            int change = (int)(Math.random()-0.5);
            if (change >=0) change = 3;
            else change = -3;

            ComputerPlayer compWhite1 = pop.get((int)(Math.random()*pop.size()*9/10)+10);
            compWhite1.mutate((int)(Math.random()*7),change);



        }




    }


    public void gA(int size,int generations,int games){

        creatFirstGeneration(size,games);

        sort(population);
        for (int i = 0; i<population.size(); i++){
            ComputerPlayer compWhite = population.get(i);
            System.out.println("Comp "+ i+":  "+ compWhite.eat+compWhite.open+compWhite.close+compWhite.build+compWhite.skip+compWhite.stayhome+compWhite.out+ "   Score:  "+compWhite.score);
        }
        System.out.println("first generation************* Average:  "+ averageScore(population)+ "************* Average:  ");

        for( int k=0;k<generations-1;k++) {
            reGenerateVer2(population);

            population.clear();

            for (int i = 0; i < newPopulation.size(); i++) {

                ComputerPlayer compWhite = newPopulation.get(i);
                compWhite.score = game.simulator(compWhite,games);
                population.add(compWhite);

            }

            newPopulation.clear();

            sort(population);

            for (int i = 0; i < population.size(); i++) {
                ComputerPlayer compWhite = population.get(i);
                System.out.println("Comp " + i + ":  " + compWhite.eat + compWhite.open + compWhite.close + compWhite.build + compWhite.skip + compWhite.stayhome + compWhite.out + "   Score:  " + compWhite.score);
            }

            System.out.println("regeneration "+(k+2)+"  ************* Average:  " + averageScore(population) + "************* Average:  ");
        }

    }

    public int averageScore(ArrayList<ComputerPlayer> pop){

        int ave =0;
        for (int i=0;i<pop.size();i++){
            ave += pop.get(i).score;
        }
        ave = ave/pop.size();

        return ave;
    }

}
