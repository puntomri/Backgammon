import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameCell extends JPanel{
    Backgammon B;
    boolean side;
    boolean triangleColor;
    boolean middle;
    int index;



    GameCell(Backgammon B,boolean side,boolean color,boolean middle,int index){
        this.side = side;
        this.middle=middle;
        this.triangleColor = color;
        this.index = index;
        this.setSize(50,300);
        this.setBackground( new Color(210, 166, 121));
        this.B=B;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(B.canPlay) {
                    if (!B.pieceinAir) B.takeAPiece(index);
                    else B.placeAPiece(index);
                } else {
                    JOptionPane.showMessageDialog(null,"New turn, Roll dices","",1);
                }
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




    @Override
    public void paint(Graphics g){

        super.paint(g);

        //pattern triangle draw:
        if (side){
            if(triangleColor){
                g.setColor(Color.GRAY);
                g.fillPolygon(new int[]{25, 0, 50}, new int[]{250, 0, 0}, 3);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillPolygon(new int[]{25, 0, 50}, new int[]{250, 0, 0}, 3);
            }
        } else{
            if(triangleColor){
                g.setColor(Color.GRAY);
                g.fillPolygon(new int[]{25, 0, 50}, new int[]{50, 300, 300}, 3);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillPolygon(new int[]{25, 0, 50}, new int[]{50, 300, 300}, 3);
            }
            //g.drawLine(25,0,50,250);
           // g.drawLine(25,0,0,250);
            //g.drawLine(0,250,50,250);
        }
        if (middle){
            g.setColor(new Color(96, 64, 32));
            g.fillRect(0,0,50,300);
        }

        //g.setColor(Color.white);
        //g.drawString(index+"",25,100);

        //piece draw:
            if(!B.board[index].empty()) {

                int s = B.board[index].size(); //3
                Color maincolor;
                Color framecolor;
                if (B.board[index].peek().white) {
                    maincolor = (Color.WHITE);
                    framecolor = (Color.BLACK);
                } else {
                    maincolor = (Color.BLACK);
                    framecolor = (Color.WHITE);
                }

                if(side){
                        for (int i = 0; i < s; i++) {
                            if (i<5) {
                                g.setColor(maincolor);
                                g.fillOval(0, (i * 50), 50, 50); //first five pieces
                                g.setColor(framecolor);
                                g.drawOval(0, (i * 50), 50, 50); //first five pieces


                            } else {
                                g.setColor(maincolor);
                                g.fillOval(0, (i * 50)-225, 50, 50); //first five pieces
                                g.setColor(framecolor);
                                g.drawOval(0, (i * 50)-225, 50, 50); //first five pieces

                            }
                        }
                } else {

                    for (int i = 0; i < s; i++) {
                        if (i<5) {
                            g.setColor(maincolor);
                            g.fillOval(0, 250-(i * 50), 50, 50); //first five pieces
                            g.setColor(framecolor);
                            g.drawOval(0, 250-(i * 50), 50, 50); //next five pieces

                        } else {
                            g.setColor(maincolor);
                            g.fillOval(0, 475-(i * 50), 50, 50); //first five pieces
                            g.setColor(framecolor);
                            g.drawOval(0, 475-(i * 50), 50, 50); //next five pieces

                        }
                    }

                }
            }












    }

}
