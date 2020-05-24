package persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import event.Event;
import event.Execution;
import event.MarketData;
import event.Order;
import util.IDGenerator;

import java.util.Objects;


@DynamoDBTable(tableName="Execution")

public class ExecutionRecord   {



    @DynamoDBAttribute(attributeName="symbol")
    String symbol;

    @DynamoDBHashKey(attributeName = "execId")
    String execId;


    @DynamoDBAttribute(attributeName = "orderId")
    String orderId;


    @DynamoDBAttribute(attributeName="price")
    double price;

    public ExecutionRecord(Execution execution) {

        symbol= execution.getSymbol();
        execId = execution.getExecId();
        orderId = execution.getOrderId();
        price = execution.getPrice();
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

    public ExecutionRecord()
    {

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
        ExecutionRecord execution = (ExecutionRecord) o;
        return execId.equals(execution.execId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(execId);
    }
}
