package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-18.
 */

/**
 * @object 选中框，分为红色和蓝色两种
 * */
public class SelectBox extends Chess {
    public SelectBox(int x, int y,boolean isBlue){
        super(new Point(x, y));
        imageIcon = isBlue  ? new ImageIcon("res/blueSelected2.png")
                            : new ImageIcon("res/redSelected.png");
        valid = true;
    }
    public SelectBox(Point point, boolean isBlue){
        super(point);
        imageIcon = isBlue  ? new ImageIcon("res/blueSelected2.png")
                            : new ImageIcon("res/redSelected.png");
        valid = true;
    }

    /**
     * @param dx the distance of move in x direction
     * @param dy the distance of move in y direction
     * @return true, the movement is always success because
     *         both x and y direction is a loop in this part
     * */
    public boolean move(int dx, int dy){
        setPoint((point.x + dx + 9)%9, (point.y +dy + 10)%10);
        return true;
    }

    public ArrayList<Point> getLogicalNext(){
        return null;
    }
    public String getName(){
        return null;
    }
    public int importance(Chess[][] map){return 0;}
}
