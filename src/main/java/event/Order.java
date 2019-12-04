package event;

import util.IDGenerator;

public class Order implements Event {

    public enum OrderType { Mkt,Limit};
    private Order(){}

    String parentOrderId;
    String orderId;
    String symbol;
    double price;
    OrderType orderType;
    int qty;

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

    public static Order createParentOrder(String symbol, double price, OrderType orderType, int qty)
    {
        Order order = new Order();
        order.setOrderId(IDGenerator.getInstance().getId(IDGenerator.Type.Order));
        order.setParentOrderId(null);
        order.setPrice(price);
        order.setSymbol(symbol);
        order.setOrderType(orderType);
        order.setQty(qty);
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
}
