package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @object “士” 或 “仕”
 * 移动方式：沿着对角线进行移动
 * */
public class Mandarin extends Chess {
    private final static int[][] direct = {{1, 1}, {1,-1}, {-1, 1}, {-1, -1}};
    private final int yMin, yMax;

    public Mandarin(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 90;


        valid = true;
        switch (id){
            case BLACK:
                imageIcon = new ImageIcon("Resources/bshi.png");
                yMin = 0;
                yMax = 2;
                break;

            default:    //RED
                imageIcon = new ImageIcon("Resources/rshi.png");
                yMin = 7;
                yMax = 9;
                break;
        }

    }


    public ArrayList<Point> getLogicalNext(){
        int x = point.x, y = point.y;
        ArrayList<Point> next = new ArrayList<>(4);
        for(int i=0;i<4;i++){
            if(canMoveTo(x + direct[i][0], y + direct[i][1]))
                next.add(new Point(x + direct[i][0], y + direct[i][1]));
        }
        return next;
    }

    /**
     * override the method in moveChess to change the default board
     * id is BLACK  --> the default board is 3<=x<=5 and 0<=y<=2
     * id is RED    --> the default board is 3<=x<=5 and 7<=y<=9
     * */
    @Override
    public boolean isLegal(int x, int y){
        return x>=3 && x<=5 && y>= yMin && y<= yMax;
    }

    public String getName(){
        return "Mandarin";
    }

    public int importance(Chess[][] map){
        int ans = this.weight;
        ArrayList<Point> next = getLogicalNext();
        for(Point point : next){
            if(map[point.x][point.y] instanceof Mandarin)
                ans += 15;
        }
        return ans;
    }

}
