package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @object “马”
 * 移动方式： 沿“日”字对角线移动， 如果对应方向下一个格子上已经有棋子， 该方向就不能移动
 * */
public class Knight extends Chess {
    private final static int[][] direct = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private final static int[][] mPosition = new int[][]{
            {4, 8, 16, 12, 4, 12, 16, 8, 4},
            {4, 10, 28, 16, 8, 16, 28, 10, 4},
            {12, 14, 16, 20, 18, 20, 16, 14, 12},
            {8, 24, 18, 24, 20, 24, 18, 24, 8},
            {6, 16, 14, 18, 16, 18, 14, 16, 6},
            {4, 12, 16, 14, 12, 14, 16, 12, 4},
            {2, 6, 8, 6, 10, 6, 8, 6, 2},
            {4, 2, 8, 8, 4, 8, 8, 2, 4},
            {0, 2, 4, 4, -2, 4, 4, 2, 0},
            {0, -4, 0, 0, 0, 0, 0, -4, 0}
    };

    public Knight(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 150;


        valid = true;
        switch (id){
            case BLACK:
                imageIcon = new ImageIcon("Resources/bma.png");
                break;

            default:    //RED
                imageIcon = new ImageIcon("Resources/rma.png");
                break;
        }

    }

    public ArrayList<Point> getLogicalNext(){
        ArrayList<Point> next = new ArrayList<>(8);
        int x, y;
        for(int i=0;i<4;i++){
            int dx = point.x + direct[i][0], dy = point.y + direct[i][1];
            if(isLegal(dx, dy) && canBeIgnored(map[dx][dy])){
                x = dx + direct[i][0] + direct[i][1];
                y = dy + direct[i][0] + direct[i][1];
                if(canMoveTo(x, y))
                    next.add(new Point(x,y));

                x = dx + direct[i][0] - direct[i][1];
                y = dy - direct[i][0] + direct[i][1];
                if(canMoveTo(x, y))
                    next.add(new Point(x,y));
            }
        }
        return next;
    }

    public String getName(){
        return "Knight";
    }

    public int importance(Chess[][] map){
        int ans = weight;
        if(this.id == ID.RED)
            ans += mPosition[point.y][point.x];
        else ans += mPosition[9-point.y][8-point.x];
        for(int i=0;i<4;i++){
            if(isLegal(point.x+direct[i][0], point.y+direct[i][1])){
                Chess chess = map[point.x+direct[i][0]][point.y+direct[i][1]];
                if(chess == null)
                    ans += 20;
            }
        }

        return ans;
    }

}
