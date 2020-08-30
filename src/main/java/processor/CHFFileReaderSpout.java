package processor;

import event.CHFDataCreator;
import event.GVAPDataCreator;
import event.GVAPDataRecord;
import service.AbstractSpoutSync;

public class CHFFileReaderSpout extends AbstractSpoutSync {

    public void process() {
        System.out.println("CHFFileReaderSpout" + Thread.currentThread().getName());

        CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data//chfgvap/chf", "");


        chfDataCreator.read().stream().forEach(chfRecord -> {

            emit("CHFGVAPMergerBoltSync", chfRecord);
        });




    }

}
