import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class BgGui extends JPanel{

    Backgammon B;
    JFrame frame;
    JPanel buttons;
    JPanel[] panelGrid;
    JPanel dices;
    JPanel turn;
    JPanel bottom;
    JLabel turnlabel;
    JTextField turnfield;
    JButton new2game;
    JButton newcompgame;
    JButton newcompcomp;
    JButton simulator;
    JButton gen;



    BgGui(Backgammon B){
        this.B = B;
        this.setSize(650,600);
        panelGrid = new JPanel[26];
        this.setLayout(new GridLayout(2,13));

        //draw the gameboard and build new place objects
        boolean side= true;
        boolean color = true;
        boolean middle = false;
        int index = 23;
        for(int i = 0;i<24; i++){

            if (i==6||i==18){
                middle = true;
                int e=24;
                if(i==18)e=25;
                GameCell place = new GameCell(B,side,color,middle,e);
                this.add(place);
                panelGrid[e]=place;
                middle=false;
            } else

                if(i==12) {
                side = false;
                if (color) color = false;
                else color = true;
            }


            if(i<12) {
                GameCell place = new GameCell(B,side,color,middle,i);
                this.add(place);
                panelGrid[i]=place;
            }
            else {
                GameCell place = new GameCell(B,side,color,middle,index);
                panelGrid[index] = place;
                this.add(place);
                index--;
            }

            if (color) color = false;
                else color = true;

        }


        buttons = new JPanel();
        buttons.setSize(200,100);
        buttons.setBackground(new Color(96, 64, 32));

        new2game = new JButton("New Two-Player game");
        new2game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.setPieces(false,false);
            }
        });

        newcompgame = new JButton("New One-Player game");
        newcompgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.setPieces(true,false);
            }
        });

        newcompcomp = new JButton("New Simulation");
        newcompcomp.setEnabled(false);
        newcompcomp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.setPieces(true,true);
            }
        });
        simulator = new JButton("New 100 game Sim");
        simulator.setEnabled(false);
        simulator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.simulator(B.computerPlayWhite,100);
            }
        });
        gen = new JButton("gen");
        gen.setEnabled(false);
        gen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Genetics g = new Genetics(B);
                g.gA(100,2,100);
            }
        });


        JRadioButton easy = new JRadioButton("easy");
        //easy.setActionCommand("1");
        easy.setSelected(true);
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               B.level=1;
            }
        });

        JRadioButton medium = new JRadioButton("medium");
       // medium.setActionCommand("2");
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.level=2;
            }
        });

        JRadioButton hard = new JRadioButton("hard");
        //hard.setActionCommand("3");
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.level=3;
            }
        });

        JRadioButton harder = new JRadioButton("harder");
        //hard.setActionCommand("3");
        harder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.level=4;
            }
        });

        JRadioButton exharder = new JRadioButton("superhard");
        //hard.setActionCommand("3");
        exharder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                B.level=5;
            }
        });
        ButtonGroup level = new ButtonGroup();
        level.add(easy);
        level.add(medium);
        level.add(hard);
        level.add(harder);
        level.add(exharder);




        buttons.add(new2game);
        buttons.add(newcompgame);
        buttons.add(newcompcomp);
        buttons.add(simulator);
        buttons.add(gen);
        //buttons.add(easy);
       // buttons.add(medium);
        //buttons.add(hard);
       // buttons.add(harder);
       // buttons.add(exharder);

        dices = new JPanel();
        dices.setBackground(new Color(96, 64, 32));
        dices.setLayout(new FlowLayout());

        dices.add(B.dice1);
        dices.add(B.dice2);




        turn= new JPanel();
        turn.setBackground(new Color(96, 64, 32));
        turn.add(turnfield= new JTextField(5));
        turn.add(turnlabel = new JLabel());
        turnlabel.setText("תור:");
        turnfield.setBackground(Color.WHITE);



        bottom= new JPanel();
        bottom.setSize(200,100);
        bottom.setBackground(new Color(96, 64, 32));



        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        frame.add(this,BorderLayout.CENTER);
        frame.add(buttons,BorderLayout.NORTH);
        frame.add(dices,BorderLayout.WEST);
        frame.add(turn,BorderLayout.EAST);
        frame.add(bottom,BorderLayout.SOUTH);


        frame.setSize(1000,685);
        //frame.pack();
        frame.setLocation(150,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    }

    public void gameOverM(boolean turn){
        String message;
        if (turn) message = "white";
        else message = "black";
            JOptionPane.showMessageDialog(null,"Winner is: "+message + "!!  Game over","",1);

    }
    public void stuck(boolean turn){
        String message;
        if (turn) message = "White";
        else message = "Black";
        JOptionPane.showMessageDialog(null,message+ " can't move. turn changed. "  ,"",1);

    }


}
