package ai;

import chess.Chess;

import java.awt.*;
import java.util.Stack;

/**
 * Created by baislsl on 17-2-21.
 */
public class MoveRecord {



    private class Step {
        Chess current, override;
        Point from, to;

        Step(Chess current, Chess override, Point from, Point to){
            this.current = current;
            this.override = override;
            this.from = from;
            this.to = to;
        }
    }

    Stack<Step> stack = new Stack<>();

    public void moveTo(Chess chess, Point point, Chess[][] map){

        stack.push(new Step(chess, map[point.x][point.y], chess.getPoint(), point));
        map[chess.point.x][chess.point.y] = null;
        chess.moveTo(point.x, point.y);
        if(map[point.x][point.y] != null)
            map[point.x][point.y].valid = false;
        map[point.x][point.y] = chess;


    }

    public void callBack(Chess[][] map){
        if(stack.isEmpty()) return;
        Step step = stack.pop();
        map[step.from.x][step.from.y] = step.current;

        map[step.to.x][step.to.y] = step.override;
        step.current.moveTo(step.from.x, step.from.y);
        if(step.override != null) {
            step.override.moveTo(step.to.x, step.to.y);
            step.override.valid = true;
        }
    }

}
