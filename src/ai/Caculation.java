package ai;

import chess.Chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by baislsl on 17-2-21.
 */
public class Caculation {

    class CompareRecord{
        ArrayList<Integer> negativeRecord = new ArrayList<>();
        ArrayList<Integer> positiveRecord = new ArrayList<>();
        int sum;
        public CompareRecord(){
        }

        public void add(int n){
            sum += n;
            if(n > 0){
                positiveRecord.add(n);
            }
            else{
                negativeRecord.add(-n);
            }
        }

        public int getSmallestLoss(int div){
            negativeRecord.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2 - o1;
                }
            });
            positiveRecord.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2 - o1;
                }
            });



            int result = 0;
            if(!positiveRecord.isEmpty()){
                int preAlly, preEnemey;
                preAlly = div;
                for(int i = 0; i< negativeRecord.size(); i++){
                    if(negativeRecord.get(i) == div){
                        negativeRecord.remove((int)i);
                    }
                }
                int positive = positiveRecord.size(), negative= negativeRecord.size();

                while(true){

                    if(positive != 0){
                        result -= preAlly;
                        preEnemey = positiveRecord.get(--positive);
                    }
                    else break;

                    if(negative != 0){
                        result += preEnemey;
                        preAlly = negativeRecord.get(--negative);
                    }
                    else break;
                }

            }
            negativeRecord.add(div);
            return result;
        }
    }


    public CompareRecord[][] getCaculationMap(ArrayList<Chess> allies, ArrayList<Chess> enemies){

        CompareRecord[][] standard = new CompareRecord[9][10];

        for(int i=0;i<9;i++)
            for (int j=0;j<10;j++)
                standard[i][j] = new CompareRecord();

        generateOneSideResult(enemies, standard, 1);
        generateOneSideResult(allies, standard, -1);
        return standard;
    }

    public static CompareRecord[][] generateOneSideResult(ArrayList<Chess> chessList, CompareRecord[][] standard, int flag){
        for(Chess chess : chessList){
            if(chess == null) continue;

            ArrayList<Point> next = chess.getLogicalNext();
            for(Point point : next){
                standard[point.x][point.y].add(flag*chess.weight);;
            }

        }

        return standard;
    }

}
