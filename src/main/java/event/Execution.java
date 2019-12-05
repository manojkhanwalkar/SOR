package event;

import util.IDGenerator;

import java.util.Objects;

public class Execution implements Event  {

    String symbol;
    String execId;
    String orderId;
    double price;


    @Override
    public String getId() {
        return execId;
    }

    @Override
    public String getParentId()
    {
        return orderId;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private Execution()
    {

    }

    public static Execution createExecution(Order order , MarketData marketData)
    {
        Execution execution = new Execution();
        execution.setExecId(IDGenerator.getInstance().getId(IDGenerator.Type.Execution));
        execution.setOrderId(order.getOrderId());
        execution.setSymbol(order.getSymbol());
        execution.setPrice(order.getPrice());
        return execution;

    }

    @Override
    public String toString() {
        return "Execution{" +
                "symbol='" + symbol + '\'' +
                ", execId='" + execId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Execution execution = (Execution) o;
        return execId.equals(execution.execId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(execId);
    }
}
