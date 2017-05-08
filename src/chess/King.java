package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-18.
 */

/**
 * @obejct “将” 或 “帅”
 * 移动方式: 左右上下移动， 每次只能移动一步， 不能移动出对应的 “田”字区域
 * */
public class King extends Chess {
    private final static int[][] direct = {{0, 1}, {0,-1}, {1, 0}, {-1, 0}};
    private final int yMin, yMax;

    public King(Point point, ID id, Chess[][] map){
        super(point, id, map);

        weight = 1500;

        valid = true;
        switch (id){
            case BLACK:
                imageIcon = new ImageIcon("Resources/bjiang.png");
                yMin = 0;
                yMax = 2;
                break;

            default:    //id is RED
                imageIcon = new ImageIcon("Resources/rshuai.png");
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


        // 飞将
        int dy = id == ID.RED ? -1 : 1;
        y += dy;
        while(y>=0&&y<=9){
            if(!canBeIgnored(map[x][y])){
                if(map[x][y] instanceof King){
                    next.add(new Point(x, y));
                }
                break;
            }
            y += dy;
        }
        return next;
    }

    /**
     * override the method in moveChess to change the default board
     * id is BLACK  --> the default board is 3<=x<=5 and 0<=y<=2
     * id is RED    --> the default board is 3<=x<=5 and 7<=y<=9
     * */
    @Override
    public boolean  isLegal(int x, int y){
        return x>=3 && x<=5 && y>= yMin && y<= yMax;
    }

    public String getName(){
        return "King";
    }

    public int importance(Chess[][] map){
        return weight;

    }


}
