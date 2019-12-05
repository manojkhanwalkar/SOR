package event;

import util.IDGenerator;

import java.util.Objects;

public class Order implements Event {

    public enum OrderType { Mkt,Limit};
    private Order(){}

    String parentOrderId;
    String orderId;
    String symbol;
    double price;
    OrderType orderType;
    int qty;
    String listId;

    @Override
    public String getId() {
        return orderId;
    }

    @Override
    public String getParentId() {
        if (parentOrderId!=null)
            return parentOrderId;
        else if (listId!=null)
            return listId;
        else
        {
            System.out.println("Both parent order id and list id are null ");
            return null;
        }

    }



    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public static Order createParentOrder(String symbol, double price, OrderType orderType, int qty, String listId)
    {
        Order order = new Order();
        order.setOrderId(IDGenerator.getInstance().getId(IDGenerator.Type.Order));
        order.setParentOrderId(null);
        order.setPrice(price);
        order.setSymbol(symbol);
        order.setOrderType(orderType);
        order.setQty(qty);
        order.setListId(listId);
        return order;
    }

    public static Order createOrder(Order parentOrder, int slicePercentage)
    {

        Order order = new Order();
        order.setPrice(parentOrder.price);
        order.setSymbol(parentOrder.getSymbol());
        order.setParentOrderId(parentOrder.getOrderId());
        order.setOrderId(IDGenerator.getInstance().getId(IDGenerator.Type.Order));
        order.setOrderType(parentOrder.getOrderType());
        order.setQty((parentOrder.getQty()*slicePercentage)/100);
        return order;

    }

    @Override
    public String toString() {
        return "Order{" +
                "parentOrderId='" + parentOrderId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", orderType=" + orderType +
                ", qty=" + qty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
