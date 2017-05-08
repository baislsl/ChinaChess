package AI;

import chess.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-25.
 */
public class EvalMarks {
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
    private final static int[][] jPosition = new int[][]{
            {14, 14, 12, 18, 16, 18, 12, 14, 14},
            {16, 20, 18, 24, 26, 24, 18, 20, 16},
            {12, 12, 12, 18, 18, 18, 12, 12, 12},
            {12, 18, 16, 22, 22, 22, 16, 18, 12},
            {12, 14, 12, 18, 18, 18, 12, 14, 12},
            {12, 16, 14, 20, 20, 20, 14, 16, 12},
            {6, 10, 8, 14, 14, 14, 8, 10, 6},
            {4, 8, 6, 14, 12, 14, 6, 8, 4},
            {8, 4, 8, 16, 8, 16, 8, 4, 8},
            {-2, 10, 6, 14, 12, 14, 6, 10, -2}
    };
    private final static int[][] mPosition = new int[][]{
            {4, 8, 16, 12, 4, 12, 16, 8, 4},
            {4, 10, 28, 16, 8, 16, 28, 10, 4},
            {12, 14, 16, 20, 18, 20, 16, 14, 12},
            {8, 24, 18, 24, 20, 24, 18, 24, 8},
            {6, 16, 14, 18, 16, 18, 14, 16, 6},
            {4, 12, 16, 14, 12, 14, 16, 12, 4},
            {2, 6, 8, 6, 10, 6, 8, 6, 2},
            {4, 2, 8, 8, 4, 8, 8, 2, 4},
            {0, 2, 4, 4, -2, 4, 4, 2, 0},
            {0, -4, 0, 0, 0, 0, 0, -4, 0}
    };

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

    // 走了后在再分析结果
    static int getEvalMark(ArrayList<Chess> allies, ArrayList<Chess> enemies, Chess[][] map){
        int ans = 0;
        for(Chess chess : allies)
            if(chess.valid) ans += chess.importance(map);
        for(Chess chess : enemies)
            if(chess.valid) ans -= chess.importance(map);
        return ans;

    }

    private static int getBeEatenMarks(ArrayList<Chess> allies, ArrayList<Chess> enemies, Chess[][] map){
        int max = 0;
        boolean[][] protect = new boolean[9][10];

        for(Chess ally : allies){
            if(!ally.valid) continue;
            ArrayList<Point> next =ally.getLogicalNext(map);
            for(Point point : next){
                protect[point.x][point.y] = true;
            }
        }

        for(Chess enemy : enemies){
            if(!enemy.valid) continue;
            ArrayList<Point> next  = enemy.getLogicalNext(map);
            for(Point point : next){
                Chess chess = map[point.x][point.y];
                if( chess!= null && chess.id != enemy.id && chess.valid){
                    if(!protect[point.x][point.y])
                        max = Math.max(max, chess.weight);
                    else
                        max = Math.max(max, chess.weight - enemy.weight);
                }
            }
        }

        return max;
    }

    private static int getFlexibilityMarks(ArrayList<Chess> allies, ArrayList<Chess> enemies, Chess[][] map){
        int sum = 0;
        for (Chess ally : allies){
            if(ally instanceof King) continue;
            if(ally.valid){
                ArrayList<Point> next = ally.getLogicalNext(map);
                sum += next.size()*ally.weight;
            }
        }

        for (Chess enemy : enemies){
            if(enemy instanceof King) continue;
            if(enemy.valid){
                ArrayList<Point> next =enemy.getLogicalNext(map);
                sum -= next.size()*enemy.weight;
            }
        }
        return sum;
    }

    private static int getPositionMarks(ArrayList<Chess> allies, ArrayList<Chess> enemies){
        int sum = 0;

        if (allies.get(0).id == Chess.ID.RED){
            for(Chess ally : allies){
                if(ally.valid){
                    sum += positionSwitcher(ally, ally.point.x,ally.point.y) * ally.weight;
                }
            }
        } else{
            for(Chess ally : allies){
                if(ally.valid){
                    sum += positionSwitcher(ally, 8 - ally.point.x,9 - ally.point.y) * ally.weight;
                }
            }
        }


        if (enemies.get(0).id == Chess.ID.RED){
            for(Chess ally : enemies){
                if(ally.valid){
                    sum -= positionSwitcher(ally, ally.point.x,ally.point.y) * ally.weight;
                }
            }
        } else{
            for(Chess ally : enemies){
                if(ally.valid){
                    sum -= positionSwitcher(ally, 8 - ally.point.x,9 - ally.point.y) * ally.weight;
                }
            }
        }

        return sum;
    }

    private static int positionSwitcher(Chess ally, int x, int y){
        if(ally instanceof Rook){
            return jPosition[y][x];
        }else if(ally instanceof Knight){
            return mPosition[y][x];
        }else if(ally instanceof Cannon){
            return pPosition[y][x];
        }else if(ally instanceof Pawn){
            return zPosition[y][x];
        }else{
            return 0;
        }
    }

    private static int getExistNumberMarks(ArrayList<Chess> allies, ArrayList<Chess> enemies){
        int sum = 0;

        for (Chess ally : allies){
            if(ally.valid)
                sum += ally.weight;
        }

        for (Chess enemy : enemies){
            if(enemy.valid){
                sum -= enemy.weight;
            }
        }

        return sum;
    }
}
