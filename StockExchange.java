import java.util.*;

public class StockExchange {
    //Implement Singleton design pattern
    private static StockExchange stockExchange;
    
    //Store all submitted buy orders and their shares that are filled
    private HashMap<Order, Integer> buyOrders = new HashMap<>();
    //Store all submitted sell orders and their shares that are filled
    private HashMap<Order, Integer> sellOrders = new HashMap<>();
    //Store all submitted orders
    private ArrayList<Order> orderRecords = new ArrayList<>();
    
    //Method to invoke the object that will be used across classes
    public static StockExchange getStockExchange(){
        if(stockExchange == null){
            stockExchange = new StockExchange();
        }
        return stockExchange;
    }

    public HashMap<Order, Integer> getBuyOrders(){
        return buyOrders;
    }

    public HashMap<Order, Integer> getSellOrders(){
        return sellOrders;
    }

    public ArrayList<Order> getOrderRecords(){
        return orderRecords;
    }

    public double[] getQuote(String orderName){
        //Array to contain bid, ask and last price of a stock
        double[] arr = new double[3];

        //Iterate through buyOrders to get bid price (highest price a buyer will pay)
        for(Map.Entry<Order, Integer> entry : buyOrders.entrySet()){
            Order currentOrder = entry.getKey();
            String currentName = currentOrder.getName();
            double currentPrice = currentOrder.getPrice();
            Status currentStatus = currentOrder.getStatus();
            //The order should not be filled yet
            if(orderName.equals(currentName) && currentStatus != Status.FILLED){
                arr[0] = Math.max(arr[0], currentPrice);
            }
        }

        arr[1] = Double.MAX_VALUE;
        //Iterate through sellOrders to get ask price (lowest price a seller will sell)
        for(Map.Entry<Order, Integer> entry : sellOrders.entrySet()){
            Order currentOrder = entry.getKey();
            String currentName = currentOrder.getName();
            double currentPrice = currentOrder.getPrice();
            Status currentStatus = currentOrder.getStatus();
            //The order should not be filled yet
            if(orderName.equals(currentName) && currentStatus != Status.FILLED){
                arr[1] = Math.min(arr[1], currentPrice);
            }
        }
        //If there is no order for sale
        if(arr[1] == Double.MAX_VALUE){
            arr[1] = 0.0;
        }

        //Iterate through orderRecords to get the latest price
        for(Order currentOrder : orderRecords){
            String currentName = currentOrder.getName();
            double currentPrice = currentOrder.getPrice();
            Status currentStatus = currentOrder.getStatus();
            //The order should be partially filled or completey filled
            if(orderName.equals(currentName) && currentStatus != Status.PENDING){
                arr[2] = currentPrice;
            }
        }
        return arr;
    }
}
