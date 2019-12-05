package processor;

import event.Event;
import event.OrderList;

import service.AbstractBoltSync;

public class ListSlicerBoltSync extends AbstractBoltSync {

    public void process(Event event)
    {
        System.out.println("ListSlicerBoltSync  " + Thread.currentThread().getName() + event);

        OrderList parentList = (OrderList) event;

        for (int i=0;i<5;i++)
        {
            OrderList list = OrderList.slice(parentList,20);
            emit("ListSlicerBoltSync" , list);
        }




    }

}
