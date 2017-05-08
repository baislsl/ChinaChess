package AI;

import record.Record;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by baislsl on 17-2-26.
 */



public class RecordStore {

    ArrayList<PowerRecord> list;
    private boolean isSorted = false;


    RecordStore(){
        list = new ArrayList<>();
    }

    public void add(PowerRecord record){
        list.add(record);
        isSorted = false;
    }


    public PowerRecord[] getPowerestPowerRecord(int candidateNumber){
        if(!isSorted){
            sort();
        }

        PowerRecord[] records = new PowerRecord[candidateNumber];
        for(int i=0;i<list.size()&&i<candidateNumber;i++)
            records[i] = list.get(i);
        return records;
    }

    // return the record with the biggest mark
    public PowerRecord getPowerestPowerRecord(){
        PowerRecord result = null;
        int max=Integer.MIN_VALUE;
        for(PowerRecord record : list){
            if(record.mark > max){
                max = record.mark;
                result = record;
            }
        }
        return result;
    }

    private void sort(){
        list.sort(new Comparator<PowerRecord>() {
            @Override
            public int compare(PowerRecord o1, PowerRecord o2) {
                if(o1.mark > o2.mark) return -1;
                else if(o1.mark < o2.mark) return 1;
                else  return 0;
            }
        });
        isSorted = true;
    }
}
