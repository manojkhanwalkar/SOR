package processor;

import event.Event;
import event.Order;
import event.OrderList;
import service.AbstractBoltSync;

public class OrderSlicerBoltSync extends AbstractBoltSync {

    public void process(Event event)
    {
        System.out.println("OrderSlicerBoltSync  " + Thread.currentThread().getName() + event);

        OrderList parentList = (OrderList) event;

        parentList.getOrders().stream().forEach(parentOrder->{

            for (int i=0;i<5;i++)
            {
                Order order = Order.createOrder(parentOrder,20);
                emit("OrderSlicerBoltSync" , order);
            }

        });






    }

}
