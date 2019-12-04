package event;

public class MarketData implements Event {

    String symbol ;
    long date;
    double price;
    String tickId;

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
}
