package processor;

import event.Event;
import event.OrderList;

import service.AbstractBolt;

public class ListSlicerBolt extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("ListSlicerBolt  " + Thread.currentThread().getName() + event);

        OrderList parentList = (OrderList) event;

        for (int i=0;i<5;i++)
        {
            OrderList list = OrderList.slice(parentList,20);
            emit("ListSlicerBolt" , list);
        }




    }

}
