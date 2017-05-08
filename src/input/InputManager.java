package input;

import AI.EvalMarks;
import AI.MainDecider;
import chess.Chess;
import chess.SelectBox;
import record.Record;
import record.RecordSaver;
import ui.GameGUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.*;

/**
 * Created by baislsl on 17-2-18.
 */

public class InputManager implements KeyListener{
    final static boolean useAI = true;
    Chess[][] map;
    GameGUI gameGUI;
    ArrayList<SelectBox> selectBoxes;
    SelectBox redBox;
    RecordSaver recordSaver = new RecordSaver("record.xml");

    boolean chosen  = false;
    public Chess.ID player = Chess.ID.RED;
    Chess selectedChess;

    public InputManager(Chess[][] map, GameGUI gameGUI, ArrayList<SelectBox> selectBox, SelectBox redBox){
        this.map = map;
        this.gameGUI = gameGUI;
        this.redBox = redBox;
        this.selectBoxes = selectBox;
        gameGUI.currentId = player;
    }

    public void conductRecord(Record record){
        conductRecord(record.from.x, record.from.y, record.to.x, record.to.y);
    }
    public void conductRecord(int fromX, int fromY, int toX, int toY){
        Chess chess = map[fromX][fromY];
        map[fromX][fromY] = null;
        if(map[toX][toY] != null)
            map[toX][toY].valid = false;
        map[toX][toY] = chess;
        chess.moveTo(toX, toY);
        selectBoxes.clear();
        chosen = false;
        player = (player == Chess.ID.RED ? Chess.ID.BLACK : Chess.ID.RED);
        gameGUI.currentId = player;

    }

    /**
     * in the beginning, we haven't chosen any moveChess yet(condition N)
     * then we should first choose it(the first condition, called A)
     * and them move the cursor to the position it can reach and push enter(condition B)
     * if in A, we want to delete the our choice, we just need to move the cursor to a position
     * the chosen moveChess can not reach and push enter again(condition C)
     * */
    private boolean enter(){
        Point currentPoint = redBox.getPoint();
        int x = currentPoint.x, y = currentPoint.y;

        if(chosen){
            // in condition A
            for(SelectBox box : selectBoxes){
                if(box.point.x == x && box.point.y == y){
                    // in condition B

                    // record this step
                    Record record = new Record(selectedChess.point.x, selectedChess.point.y,
                            x, y, selectedChess, map[x][y]);
                    recordSaver.addRecord(record);
                    conductRecord(record);
                    if(useAI) {
                        gameGUI.repaint();
                        useAI();
                    }
                    return true;
                }
            }

            //  in condition C, not choose the correct box, thus clear all the blue selected boxes
            selectBoxes.clear();
            chosen = false;
            return true;

        }else{
            // in condition N
            System.out.println(x + " " + y);
            if(map[x][y]!=null && map[x][y].valid && map[x][y].id == player){
                selectedChess = map[x][y];
                for(Point point : selectedChess.getNext()){
                    selectBoxes.add(new SelectBox(point, true));
                }
                if(selectBoxes.isEmpty()) return false;
                else {
                    // jump to condition A
                    chosen = true;
                    return true;
                }
            }else{
                return false;
            }
        }
    }

    private void useAI(){
        Record record = player == Chess.ID.BLACK
                ? MainDecider.getAiNextStep(Chess.blackChessList, Chess.redChessList)
                : MainDecider.getAiNextStep(Chess.redChessList, Chess.blackChessList);
        recordSaver.addRecord(record);
        conductRecord(record);
    }

    /**
     * retract
     * */
    public void retract(){
        if(recordSaver.isEmpty()){
            System.out.println("No record!");
            return;
        }
        Record record = recordSaver.popLastRecord();

        map[record.from.x][record.from.y] = record.moveChess;
        record.moveChess.moveTo(record.from.x, record.from.y);

        map[record.to.x][record.to.y] = record.delChess;
        if(record.delChess != null){
            record.delChess.valid = true;
            record.delChess.moveTo(record.to.x, record.to.y);
        }
        player = player == Chess.ID.RED ? Chess.ID.BLACK : Chess.ID.RED;
        gameGUI.currentId = player;
        selectBoxes.clear();

        chosen = false;

    }

    public void save(){
        recordSaver.save();
    }

    public void keyTyped(KeyEvent e){}


    public void keyPressed(KeyEvent e){
        boolean success;    // the input is valid or not
        switch (e.getKeyCode()){
            case VK_S:case VK_DOWN:
                success = redBox.move(0, 1);
                break;

            case VK_W:case VK_UP:
                success = redBox.move(0, -1);
                break;

            case VK_D:case VK_RIGHT:
                success = redBox.move(1, 0);
                break;

            case VK_A:case VK_LEFT:
                success = redBox.move(-1, 0);
                break;

            case VK_ENTER:
                success = enter();
                break;

            default:
                success = false;
        }
        if(success){
            gameGUI.repaint();
        }
    }


    public void keyReleased(KeyEvent e){}


}
