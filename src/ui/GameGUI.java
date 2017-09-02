package ui;

import chess.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-18.
 */

/**
 * the UI part of the game, draw all the component to the screen
 * */
public class GameGUI extends JPanel {

    private final static Point BLACK_BEGIN = new Point(74,40);
    private final static Point RED_BEGIN = new Point(74,407);
    private final static int SIZE = 74;
    private final static int RADIUS = 28;


    public Chess.ID currentId = Chess.ID.RED;
    final ImageIcon background = new ImageIcon("res/background.png");
    Chess[][] map;
    ArrayList<SelectBox> selectBox;
    SelectBox redBox;

    public GameGUI(Chess[][] map, ArrayList<SelectBox> selectBox, SelectBox redBox){
        this.map = map;
        this.selectBox = selectBox;
        this.redBox = redBox;

    }

    private void drawChess(Chess chess, Graphics g){
        if(chess != null && chess.valid) {
            int x = chess.getPoint().x;
            int y = chess.getPoint().y;

            if(y < 4){  //the moveChess in the blackChessList part
                x = x*SIZE + BLACK_BEGIN.x;
                y = y*SIZE + BLACK_BEGIN.y;
            }else{      //the moveChess in the redChessList part
                x = x*SIZE + RED_BEGIN.x;
                y = (y - 5)*SIZE + RED_BEGIN.y;
            }
            ImageIcon imageIcon = chess.getImageIcon();
            g.drawImage(imageIcon.getImage(), x - RADIUS, y - RADIUS, null);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background.getImage(),0,0,this);
        drawChess(redBox, g);
        for(SelectBox box : selectBox){
            drawChess(box, g);
        }
        for(int x=0;x<9;x++){
            for(int y=0;y<10;y++){
                drawChess(map[x][y], g);
            }
        }

        ImageIcon imageIcon = currentId == Chess.ID.RED
                ? new ImageIcon("res/red.png")
                : new ImageIcon("res/black.png");
        g.drawImage(imageIcon.getImage(), 10, 356, this);

    }

    public int getBackgroundWidth(){
        return background.getIconWidth();
    }
    public int getBackgroundHeight(){
        return background.getIconHeight();
    }
}

