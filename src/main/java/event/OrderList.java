package event;

import util.IDGenerator;

import java.util.ArrayList;
import java.util.List;

public class OrderList implements Event {

    String parentListId ;
    String listId;

    List<Order> orders;

    public String getParentListId() {
        return parentListId;
    }

    public void setParentListId(String parentListId) {
        this.parentListId = parentListId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order)
    {
        if (orders==null)
        {
            orders = new ArrayList<>();
        }
        this.orders.add(order);
    }

    private OrderList()
    {

    }

    public static OrderList createParentList()
    {
        OrderList list = new OrderList();
        list.setParentListId(null);
        list.setListId(IDGenerator.getInstance().getId(IDGenerator.Type.List));
        return list;

    }

    public static OrderList slice(OrderList parentList , int slicePercentage)
    {

        OrderList list = new OrderList();
        list.setParentListId(parentList.getListId());

        parentList.getOrders().forEach(o->{
            Order order = Order.createOrder(o,slicePercentage);
            list.addOrder(order);
        });

        return list;

    }

    @Override
    public String toString() {
        return "OrderList{" +
                "parentListId='" + parentListId + '\'' +
                ", listId='" + listId + '\'' +
                ", orders=" + orders +
                '}';
    }
}
