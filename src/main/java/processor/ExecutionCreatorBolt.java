package processor;

import event.*;
import service.AbstractBolt;

public class ExecutionCreatorBolt extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("ExecutionCreatorBolt  " + Thread.currentThread().getName() + event);

        Order order = (Order) event;



            for (int i=0;i<5;i++)
            {
                Execution execution = Execution.createExecution(order,new MarketData());
                emit("ExecutionCreatorBolt" , execution);
            }








    }

}
