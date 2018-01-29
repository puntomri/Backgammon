import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Dice extends JPanel{

    JLabel a;
    int value;
    boolean played;
    Backgammon B;


    Dice(Backgammon B){
        this.B = B;
        played = false;


        Font f = new Font("fff",Font.BOLD,40);
        a = new JLabel("0");
        a.setFont(f);
        //a.setSize(30,30);
      // a.setLocation(25,125);
        add(a);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                B.rolldices(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


    }



    public void setDice(int value){

        this.value = value;
        if (value== 0)a.setText("0");
        else a.setText(""+value);
        played = false;
        setEnabled(true);

    }

    public int rollDice(){

        a.setEnabled(true);
        int newValue =((int) ((Math.random()*6)+1));
        setDice(newValue);
        return newValue;



    }



}

