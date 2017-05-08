package ai;


import chess.Chess;
import chess.King;
import record.Record;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by baislsl on 17-2-21.
 */

class RecordWithCount {
    Record record;
    int count;
    RecordWithCount(Record record, int count){
        this.record = record;
        this.count = count;
    }

}

public class Ai {



    // deep should be odd
    public static Record getComputerStep(ArrayList<Chess> allies, ArrayList<Chess> enemies, int deep){
        MoveRecord moveRecord = new MoveRecord();
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

        ArrayList<RecordWithCount> allyFirstStep = getStep(allies, enemies, map);

        Record result = null;
        int finalCost =Integer.MIN_VALUE;
        //第一轮计算筛选
        allyFirstStep.sort(new Comparator<RecordWithCount>() {
            @Override
            public int compare(RecordWithCount o1, RecordWithCount o2) {
                return o2.count - o1.count;
            }
        });
        //只选取前１０步比较　待改进
        for(int i=0;i<allyFirstStep.size()&&i<=9;i++){
            RecordWithCount firstStep =allyFirstStep.get(i);
            moveRecord.moveTo(firstStep.record.moveChess, firstStep.record.to, map);
            ArrayList<RecordWithCount> secondStep = getStep(enemies,allies,map);

            int max = Integer.MIN_VALUE;
            //find the biggest gain of the second step
            for(RecordWithCount second : secondStep){
                if(second.count > max)
                    max = second.count;
            }
            if(firstStep.count - max > finalCost){
                finalCost = firstStep.count - max;
                Chess chess = firstStep.record.moveChess;
                Point next = firstStep.record.to;
                result = new Record(chess.point.x, chess.point.y, next.x, next.y,chess, null);
            }

            moveRecord.callBack(map);

         }

        if(result == null) {
            System.out.println("have not way to step, one side win");
        }
        return result;


    }

    public static int benefit(int deep) {
        return 0;
    }

    public static ArrayList<RecordWithCount> getStep(
            ArrayList<Chess> allies, ArrayList<Chess> enemies, Chess[][] map){
        ArrayList<RecordWithCount> ans = new ArrayList<>();
        for(Chess ally : allies){
            if(!ally.valid) continue;

            //debug
            if(ally instanceof King){
                int a = 1;
            }

            Caculation.CompareRecord importance[][] = new Caculation().getCaculationMap(allies, enemies);
            for(Point next : ally.getNext()) {
                //moveRecord.moveTo(ally, next,map);
                int count = importance[next.x][next.y].getSmallestLoss(ally.weight);
                if(map[next.x][next.y]!=null && map[next.x][next.y].id != ally.id){
                    count += map[next.x][next.y].weight;
                }
                Record record = new Record(ally.point.x, ally.point.y, next.x, next.y, ally, null);
                ans.add(new RecordWithCount(record,count));
                //第一轮计算完毕

//                if(count > maxBenefit ||
//                        (count == maxBenefit && new Random(2).nextInt() == 1)
//                        ){
//                    result = new Record(ally.point.x, ally.point.y, next.x, next.y, ally);
//                    maxBenefit = count;
//                }

                //moveRecord.callBack(map);
            }
        }
        return ans;
    }



}
