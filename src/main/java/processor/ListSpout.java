package processor;

import event.Order;
import event.OrderList;
import event.Symbols;
import sample.SampleEvent;
import service.AbstractSpout;

public class ListSpout extends AbstractSpout {

    public void process()
    {
        System.out.println("List Spout " + Thread.currentThread().getName());

      //  for (int i=0;i<10;i++) {

            OrderList list = OrderList.createParentList();

        for (Symbols symbol : Symbols.values()) {
            Order order = Order.createParentOrder(symbol.name(), 100.0, Order.OrderType.Mkt, 10000);
            list.addOrder(order);
        }
            emit("ListSpout", list);

      //  }
    }

}
