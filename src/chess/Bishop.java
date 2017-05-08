package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @object “相” 或 “象”
 * 移动方式：沿着 “田” 字对顶点进行移动，如果 “田” 中间有棋子就不能移动
 * */
public class Bishop extends Chess {
    private final static int[][] direct = {{2, 2}, {2,-2}, {-2, 2}, {-2, -2}};
    private final int yMin, yMax;

    public Bishop(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 90;

        valid = true;
        switch (id){
            case BLACK:
                yMin = 0;yMax = 4;
                imageIcon = new ImageIcon("Resources/bxiang.png");
                break;

            default:    //id is RED
                yMin = 5;yMax = 9;
                imageIcon = new ImageIcon("Resources/rxiang.png");
                break;
        }

    }

    public ArrayList<Point> getLogicalNext(){
        int x = point.x, y = point.y;
        ArrayList<Point> next = new ArrayList<>(4);
        for(int i=0;i<4;i++){
            int dx = x + direct[i][0], dy = y + direct[i][1];
            if(canMoveTo(dx, dy) && canBeIgnored(map[x + direct[i][0]/2][y + direct[i][1]/2])){
                next.add(new Point(dx, dy));
            }
        }
        return next;
    }

    /**
     * override the method in moveChess to change the default board
     * id is BLACK  --> the default board is 0<=x<=8 and 0<=y<=4
     * id is RED    --> the default board is 0<=x<=8 and 5<=y<=9
     * */
    @Override
    public boolean isLegal(int x, int y){
        return x>=0 && x<=8 && y>= yMin && y<= yMax;
    }

    public String getName(){
        return "Bishop";
    }

    public int importance(Chess[][] map){
        int ans = this.weight;
        ArrayList<Point> next = getLogicalNext(map);
        for(Point point : next){
            if(map[point.x][point.y] instanceof Bishop)
                ans += 15;
        }



        return ans;

    }
}
