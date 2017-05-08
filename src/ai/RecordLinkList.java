package ai;

import record.Record;

/**
 * Created by baislsl on 17-2-21.
 */
public class RecordLinkList {
    Record record;
    Record next;

    RecordLinkList(){
        this.record = null;
        this.next = null;
    }

    RecordLinkList(Record record){
        this.record = record;
        this.next = null;
    }

    RecordLinkList(Record record, Record next){
        this.record = record;
        this.next = next;
    }

    void append(Record record){

    }
}
