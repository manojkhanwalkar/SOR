package processor;

import event.Event;
import event.Order;
import event.OrderList;
import service.AbstractBolt;

public class OrderSlicerBolt extends AbstractBolt {

    public void process(Event event)
    {
        System.out.println("OrderSlicerBolt  " + Thread.currentThread().getName() + event);

        OrderList parentList = (OrderList) event;

        parentList.getOrders().stream().forEach(parentOrder->{

            for (int i=0;i<5;i++)
            {
                Order order = Order.createOrder(parentOrder,20);
                emit("OrderSlicerBolt" , order);
            }

        });






    }

}
