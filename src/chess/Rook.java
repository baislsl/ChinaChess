package chess;

import com.sun.corba.se.internal.Interceptors.PIORB;
import com.sun.org.apache.regexp.internal.RE;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @object “车”
 * 移动方式：左右上下直线走，不能跨越棋子
 * */
public class Rook extends Chess {
    private final static int[][] direct = {{0, 1}, {0,-1}, {1, 0}, {-1, 0}};
    private final static int[][] jPosition = new int[][]{
            {14, 14, 12, 18, 16, 18, 12, 14, 14},
            {16, 20, 18, 24, 26, 24, 18, 20, 16},
            {12, 12, 12, 18, 18, 18, 12, 12, 12},
            {12, 18, 16, 22, 22, 22, 16, 18, 12},
            {12, 14, 12, 18, 18, 18, 12, 14, 12},
            {12, 16, 14, 20, 20, 20, 14, 16, 12},
            {6, 10, 8, 14, 14, 14, 8, 10, 6},
            {4, 8, 6, 14, 12, 14, 6, 8, 4},
            {8, 4, 8, 16, 8, 16, 8, 4, 8},
            {-2, 10, 6, 14, 12, 14, 6, 10, -2}
    };

    public Rook(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 600;

        valid = true;
        switch (id){
            case BLACK:
                imageIcon = new ImageIcon("Resources/bche.png");
                break;

            default:    //RED
                imageIcon = new ImageIcon("Resources/rche.png");
                break;
        }

    }


    public ArrayList<Point> getLogicalNext(){
        ArrayList<Point> next = new ArrayList<>();
        for(int i=0;i<4;i++){
            int x = point.x + direct[i][0], y = point.y + direct[i][1];
            while(isLegal(x,y)){
                if(canBeIgnored(map[x][y])){
                    next.add(new Point(x, y));
                    x += direct[i][0]; y += direct[i][1];
                }else{
                    if(canMoveTo(x, y))
                        next.add(new Point(x, y));
                    break;
                }
            }
        }
        return next;
    }

    public String getName(){
        return "Rook";
    }
    public int importance(Chess[][] map){
        int ans = weight;
        if(this.id == ID.RED)
            ans += jPosition[point.y][point.x];
        else ans += jPosition[9-point.y][8-point.x];
        ArrayList<Point> next = this.getNext(map);
        int min, max;
        if(this.id == ID.RED){
            min = 0;
            max = 4;
        }else{
            min = 5;
            max = 9;
        }
        for(Point point : next){
            int y = point.y;
            if(y>=min && y<=max){
                ans += 15;
            }
        }

        return ans;
    }
}
