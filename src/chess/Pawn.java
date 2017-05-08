package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-19.
 */

/**
 * @obejct “兵”　或　“卒”
 * 移动方式：没跨越“楚河汉界”前，只能对方阵型方向向前移动，每次移动一步
 *          跨越“楚河汉界”后，每次可以左右前移动一步，但不能先后走
 * */
public class Pawn extends Chess{
    private final int dy;
    private final int boundary;
    private final static int[][] zPosition = new int[][]{
            {0, 3, 6, 9, 12, 9, 6, 3, 0},
            {18, 36, 56, 80, 120, 80, 56, 36, 18},
            {14, 26, 42, 60, 80, 60, 42, 26, 14},
            {10, 20, 30, 34, 40, 34, 30, 20, 10},
            {6, 12, 18, 18, 20, 18, 18, 12, 6},
            {2, 0, 8, 0, 8, 0, 8, 0, 2},
            {0, 0, -2, 0, 4, 0, -2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

     public Pawn(Point point, Chess.ID id, Chess[][] map){
         super(point, id, map);

         weight = 30;

         this.valid = true;
         if(id == ID.RED){
             dy = -1;
             boundary = 5;
             imageIcon = new ImageIcon("Resources/rbing.png");
         }else{
             dy = 1;
             boundary = 4;
             imageIcon = new ImageIcon("Resources/bzu.png");
         }
     }

    public ArrayList<Point> getLogicalNext(){
        ArrayList<Point> next = new ArrayList<>(3);
        int x = point.x, y = point.y;

        if(canMoveTo(x, y + dy))
            next.add(new Point(x, y + dy));
        if(dy*(point.y - boundary) > 0){
            if(canMoveTo(x - 1, y))
                next.add(new Point(x - 1, y));
            if(canMoveTo(x + 1, y))
                next.add(new Point(x + 1, y));
        }
        return next;
    }

    public String getName(){
        return "Pawn";
    }

    public int importance(Chess[][] map){
        int ans = weight;
        if(this.id == ID.RED)
            ans += zPosition[point.y][point.x];
        else ans += zPosition[9-point.y][8-point.x];

        return ans;
    }

}
