package processor;

import event.*;
import service.AbstractBoltSync;

public class ExecutionCreatorBoltSync extends AbstractBoltSync {

    public void process(Event event)
    {
        System.out.println("ExecutionCreatorBoltSync  " + Thread.currentThread().getName() + event);

        Order order = (Order) event;



            for (int i=0;i<5;i++)
            {
                Execution execution = Execution.createExecution(order,new MarketData());
                emit("ExecutionCreatorBoltSync" , execution);
            }








    }

}
