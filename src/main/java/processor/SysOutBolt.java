package processor;

import event.Event;
import event.Execution;
import event.MarketData;
import event.Order;
import service.AbstractBolt;

public class SysOutBolt extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("SysOutBolt  " + Thread.currentThread().getName() + event);




    }

}
