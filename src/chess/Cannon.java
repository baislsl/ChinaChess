package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @object “炮” 或 “砲”
 * 两种移动方式: ①不吃棋子， 直线移动，不能跨越任何棋
 *             ②吃棋子， 直线方向， 吃掉跨越一个棋子后的下一个棋子
 * */
public class Cannon extends Chess {
    private final static int[][] direct = {{0, 1}, {0,-1}, {1, 0}, {-1, 0}};
    private final static int[][] pPosition = new int[][]{
            {6, 4, 0, -10, -12, -10, 0, 4, 6},
            {2, 2, 0, -4, -14, -4, 0, 2, 2},
            {2, 2, 0, -10, -8, -10, 0, 2, 2},
            {0, 0, -2, 4, 10, 4, -2, 0, 0},
            {0, 0, 0, 2, 8, 2, 0, 0, 0},
            {-2, 0, 4, 2, 6, 2, 4, 0, -2},
            {0, 0, 0, 2, 4, 2, 0, 0, 0},
            {4, 0, 8, 6, 10, 6, 8, 0, 4},
            {0, 2, 4, 6, 6, 6, 4, 2, 0},
            {0, 0, 2, 6, 6, 6, 2, 0, 0}
    };
    public Cannon(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 300;

        valid = true;
        switch (id){
            case BLACK:
                imageIcon = new ImageIcon("Resources/bpao.png");
                break;

            default:    //RED
                imageIcon = new ImageIcon("Resources/rpao.png");
                break;
        }

    }

    public ArrayList<Point> getLogicalNext(){
        ArrayList<Point> next = new ArrayList<>(4);
        for(int i=0;i<4;i++){
            // do not eat another moveChess
            int x = point.x + direct[i][0], y = point.y + direct[i][1];
            for(;isLegal(x, y)&&canBeIgnored(map[x][y]); x+=direct[i][0], y+=direct[i][1])
                next.add(new Point(x, y));

            if(isLegal(x, y)){
                do{
                    x += direct[i][0];
                    y += direct[i][1];
                }while(isLegal(x, y) && canBeIgnored(map[x][y]));
                //pass through one moveChess and eat another moveChess
                if(canMoveTo(x, y))
                    next.add(new Point(x, y));
            }
        }
        return next;
    }

    public String getName(){
        return "Cannon";
    }

    public int importance(Chess[][] map){
        int ans = weight;
        if(this.id == ID.RED)
            ans += pPosition[point.y][point.x];
        else ans += pPosition[9-point.y][8-point.x];

        int count=0;
        for(int x=0;x<8;x++){
            if(map[x][point.y]!=null && map[x][point.y].id != this.id)
                ans += Math.sqrt(map[x][point.y].weight);
            if(++count >= 3) break;
        }
        for(int y=0;y<9;y++){
            if(map[point.x][y]!=null && map[point.x][y].id != this.id){
                ans += Math.sqrt(map[point.x][y].weight);
            }
            if(++count >= 3) break;
        }
        return ans;
    }
}
