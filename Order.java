import java.util.*;

public abstract class Order{

    static final StockExchange stockExchange = StockExchange.getStockExchange();
    static final HashMap<Order, Integer> sellRecords = stockExchange.getSellOrders();
    static final HashMap<Order, Integer> buyRecords = stockExchange.getBuyOrders();

    private String name;
    private double price;
    private int quantity;
    private Side side;
    private Status status;
    private Type type;

    protected Order(String name, double price, int quantity, Side side, Type type){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.type = type;
        status = Status.PENDING;
    }

    public abstract void buy(String name, double price, int quantity);

    public abstract void sell(String name, double price, int quantity);

    public abstract String toString();

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public Side getSide(){
        return side;
    }

    public Status getStatus(){
        return status;
    }

    public Type getType(){
        return type;
    }

    public void setPrice(double newPrice){
        price = newPrice;
    }

    public boolean fillOrder(Order target, Order current){
        //target is the order that will be processed
        HashMap<Order, Integer> targetRecords;
        HashMap<Order, Integer> currentRecords;

        //Switch the records accordingly to update and be matched
        if(target.getSide() == Side.BUY){
            targetRecords = buyRecords;
            currentRecords = sellRecords;
        } else{
            targetRecords = sellRecords;
            currentRecords = buyRecords;
        }

        int currentQuantity = current.getQuantity();
        int currentFilled = currentRecords.get(current);
        int available = currentQuantity - currentFilled;

        //If there is no share available, target order is not processed
        if(available == 0){
            return false;
        }

        int targetQuantity = target.getQuantity();
        int targetFilled = targetRecords.get(target);
        int requiredQuantity = targetQuantity - targetFilled;
        
        if(requiredQuantity == available){
            targetRecords.put(target, targetQuantity);
            target.status = Status.FILLED;
            currentRecords.put(current, currentQuantity);
            current.status = Status.FILLED;
        } else if(requiredQuantity > available){
            targetRecords.put(target, targetFilled + available);
            target.status = Status.PARTIAL;
            currentRecords.put(current, currentQuantity);
            current.status = Status.FILLED;
        } else{
            targetRecords.put(target, targetQuantity);
            target.status = Status.FILLED;
            currentRecords.put(current, currentFilled + requiredQuantity);
            current.status = Status.PARTIAL;
        }
        return true;
    }
}