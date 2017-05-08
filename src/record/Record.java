package record;

import chess.Chess;

import java.awt.*;

public class Record {
    public Point from, to;
    public Chess moveChess, delChess;

    public Record(int formX, int formY, int toX, int toY, Chess moveChess, Chess delChess){
        this.from = new Point(formX, formY);
        this.to = new Point(toX, toY);
        this.moveChess = moveChess;
        if(delChess == null || !delChess.valid) this.delChess = null;
        else this.delChess = delChess;
    }

    public Record(int formX, int formY, int toX, int toY){
        this(formX, formY, toX, toY, null, null);
    }
}
