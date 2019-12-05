package processor;

import event.Event;
import service.AbstractBoltSync;

public class SysOutBoltSync extends AbstractBoltSync {

    public void process(Event event)
    {
        System.out.println("SysOutBoltSync  " + Thread.currentThread().getName() + event);




    }

}
