package event;

import util.IDGenerator;

import java.util.Objects;

public class MarketData implements Event {

    String symbol ;
    long date;
    double price;
    String tickId;

    @Override
    public String getId() {
        return tickId;
    }



    public String getTickId() {
        return tickId;
    }

    public void setTickId(String tickId) {
        this.tickId = tickId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "symbol='" + symbol + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", tickId='" + tickId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketData that = (MarketData) o;
        return tickId.equals(that.tickId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickId);
    }

    public MarketData()
    {
        tickId = IDGenerator.getInstance().getId(IDGenerator.Type.MarketData);
    }
}
