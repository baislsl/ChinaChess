package chess;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-18.
 */

public abstract class Chess {

    public static ArrayList<Chess> redChessList = new ArrayList<>(),
            blackChessList = new ArrayList<>();
    public int weight;
    public enum ID{
        RED, BLACK
    }
    public Point point;
    public boolean valid;
    public ID id;
    public Chess[][] map;
    ImageIcon  imageIcon;

    public Chess(){
        valid = false;
        point = new Point();
    }

    public Chess(Point point){
        valid = false;
        this.point = point;
    }

    public Chess(Point point, ID id, Chess[][] map){
        valid = false;
        this.point = point;
        this.id = id;
        this.map = map;
        if(id == ID.RED) redChessList.add(this);
        else blackChessList.add(this);
    }


    public Point getPoint(){
        return point;
    }

    public void setPoint(Point point){
        this.point.setLocation(point);
    }

    public void setPoint(int x, int y){
        this.point.x = x;
        this.point.y = y;
    }

    public  void moveTo(int x, int y){
        point = new Point(x,y);
    }

    public ImageIcon getImageIcon(){
        return imageIcon;
    }

    /**
     * @param x position X
     * @param y position Y
     * @return true if position (x,y) is inboard
     *         false is position (x,y) is outboard
     * the default board is 0<=x<=8 and 0<=y<=9
     * */
    public boolean isLegal(int x, int y){
        return x>=0 && x<=8 && y>=0 && y <= 9;
    }

    /**
     * @param x the target X
     * @param y hte target Y
     * @return whether the current moveChess can move to the target position
     *         false if the target position already exist a moveChess with the same ID
     *         true is target position exist has not moveChess or a moveChess with different ID
     * */
    public boolean canMoveTo(int x, int y){
        return isLegal(x, y);
    }

    /**
     * @return all the possible next step position of this moveChess,
     *          such position should be tested true by @method canMoveTo
     * */
    public ArrayList<Point> getNext(){
        ArrayList<Point> result = new ArrayList<>();
        for(Point point : this.getLogicalNext()){
            if(map[point.x][point.y] == null || !map[point.x][point.y].valid
                                             || map[point.x][point.y].id != this.id)
                result.add(point);
        }
        return result;
    }

    // 所有到达的均可以，　无论对方是不是自己人
    public abstract ArrayList<Point> getLogicalNext();

    public ArrayList<Point> getLogicalNext(Chess[][] map){
        Chess[][] temp = this.map;
        this.map = map;
        ArrayList<Point> ret = this.getLogicalNext();
        this.map = temp;
        return ret;

    }

    public ArrayList<Point> getNext(Chess[][] map){
        Chess[][] temp = this.map;
        this.map = map;
        ArrayList<Point> ret = this.getNext();
        this.map = temp;
        return ret;
    }


    public abstract String getName();

    // the map must only contain that the chess is still alive
    public abstract int importance(Chess[][] map);

    public boolean canBeIgnored(Chess chess){
        return chess == null || !chess.valid;
    }

}
