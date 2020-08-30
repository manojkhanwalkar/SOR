package processor;

import event.*;
import service.AbstractSpoutSync;

public class GVAPFileReaderSpout extends AbstractSpoutSync {

    public void process() {
        System.out.println("GVAPFileReaderSpout" + Thread.currentThread().getName());

        GVAPDataCreator gvapDataCreator = new GVAPDataCreator("/home/manoj/data/chfgvap/gvap", "");


        var iter = gvapDataCreator.iterator();

        while (iter.hasNext()) {
            GVAPDataRecord record = iter.next();
            emit("SysOutBoltSync", record);


        }

    }




}
