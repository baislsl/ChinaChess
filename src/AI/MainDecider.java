package AI;

import chess.Chess;
import record.Record;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-25.
 */
public class MainDecider {
    private final static int DEPTH = 3;  //always be odd
    private final static int CANDIDATE_NUMBER = 10;

    public static Record getAiNextStep(ArrayList<Chess> allies, ArrayList<Chess> enemies){
        return getComputer(allies, enemies, DEPTH, CANDIDATE_NUMBER).record1;
    }

    private static PowerRecord getComputer(ArrayList<Chess> allies,
                                           ArrayList<Chess> enemies,
                                           int depth, int candidateNumbers){
        RecordStore recordStore = new RecordStore();
        ArrayList<Chess> newAllies = new ArrayList<>();
        ArrayList<Chess> newEnemies = new ArrayList<>();

        // delete all the moveChess with valid false, and create a new map
        Chess[][] map = new Chess[9][10];
        for(Chess ally : allies){
            if(ally.valid){
                map[ally.point.x][ally.point.y] = ally;
                newAllies.add(ally);
            }
        }
        for(Chess enemy : enemies){
            if(enemy.valid){
                map[enemy.point.x][enemy.point.y] = enemy;
                newEnemies.add(enemy);
            }
        }
        allies = newAllies; enemies = newEnemies;

        for(Chess ally : allies){
            ArrayList<Point> next = ally.getNext(map);
            for(Point point : next){
                PowerRecord powerRecord = new PowerRecord();
                Record record = new Record(ally.point.x, ally.point.y, point.x, point.y,
                        ally, map[point.x][point.y]);
                Chess del = map[point.x][point.y];
                conductRecord(record, map);
                if(del != null){
                    ArrayList<Chess> newChessList = new ArrayList<>();
                    for(Chess chess : enemies){
                        if(!chess.equals(del))
                            newChessList.add(chess);
                    }
                    enemies = newChessList;
                }
                for(Chess enemy : enemies){
                    if(!enemy.valid) continue;
                    ArrayList<Point> enemyNext = enemy.getNext(map);
                    for(Point enemyPoint : enemyNext){
                        Record enemyRecord = new Record(enemy.point.x, enemy.point.y, enemyPoint.x, enemyPoint.y,
                            enemy, map[enemyPoint.x][enemyPoint.y]);
                        conductRecord(enemyRecord, map);
                        int mark = EvalMarks.getEvalMark(allies,enemies,map);
                        if(mark < powerRecord.mark){
                            powerRecord.mark = mark;
                            powerRecord.record1 = record;
                            powerRecord.record2 = enemyRecord;
                        }

                        restoreRecord(enemyRecord, map);
                    }

                }
                restoreRecord(record, map);
                if(del != null){
                    enemies.add(del);
                    del.valid = true;
                }
                recordStore.add(powerRecord);
            }
        }// finished searching all the possible two move and stored the result into powerRecord list

        if(depth == 1){
            return recordStore.getPowerestPowerRecord();
        }else{
            PowerRecord[] step = recordStore.getPowerestPowerRecord(candidateNumbers);
            for(PowerRecord record : step){
                if(record == null)
                conductRecord(record.record1, map);
                conductRecord(record.record2, map);

                PowerRecord nextStep = getComputer(allies, enemies, depth - 1, candidateNumbers);
                record.mark = nextStep.mark;

                restoreRecord(record.record2, map);
                restoreRecord(record.record1, map);

            }
            if(depth == DEPTH)
            System.out.println("mark is " + recordStore.getPowerestPowerRecord().mark);
            return recordStore.getPowerestPowerRecord();
        }


    }

    private static void conductRecord(Record record, Chess[][] map){
        // 创建移动现场
        int fromX = record.from.x, fromY = record.from.y;
        int toX = record.to.x, toY = record.to.y;
        Chess chess = record.moveChess;
        map[fromX][fromY] = null;
        if(record.delChess != null)
            record.delChess.valid = false;
        map[toX][toY] = chess;
        chess.moveTo(toX, toY);
    }
    private static void restoreRecord(Record record, Chess[][] map){
        //恢复移动现场
        map[record.from.x][record.from.y] = record.moveChess;
        record.moveChess.moveTo(record.from.x, record.from.y);

        map[record.to.x][record.to.y] = record.delChess;
        if(record.delChess != null){
            record.delChess.valid = true;
            record.delChess.moveTo(record.to.x, record.to.y);
        }
    }

}
